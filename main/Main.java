package main;

import project.ApplicationStatus;
import project.Enquiry;
import project.FlatType;
import project.HDBFlat;
import project.Visibility;
import person.MaritalStatus;

import fileops.FileOps;
import fileops.ObjectCreate;
import person.Applicant;
import person.Application;
import person.HDBManager;
import person.HDBOfficer;
import project.Project;
import person.Application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            runConsole(); // everything previously in main is moved here
        } catch (Exception ex) {
            System.err.println("\n--- An unexpected error occurred ---");
            ex.printStackTrace(); // full stack trace to stderr
        }
    }

    /* all the existing login/menu code goes here */
    private static void runConsole() {

        buildPasswordMap(); // ← same as before

        while (true) {
            try {
                System.out.print("\nEnter NRIC / OfficerName / ManagerName (or 'exit'): ");
                String id = sc.nextLine().trim();
                if ("exit".equalsIgnoreCase(id))
                    break;

                if (!passwords.containsKey(id)) {
                    System.out.println("User not found.");
                    continue;
                }
                System.out.print("Password: ");
                if (!passwords.get(id).equals(sc.nextLine().trim())) {
                    System.out.println("Wrong password.");
                    continue;
                }

                if (ObjectCreate.applicantMap.containsKey(id)) {
                    applicantMenu(ObjectCreate.applicantMap.get(id));
                } else if (ObjectCreate.officerMap.containsKey(id)) {
                    officerMenu(ObjectCreate.officerMap.get(id));
                } else if (ObjectCreate.managerMap.containsKey(id)) {
                    managerMenu(ObjectCreate.managerMap.get(id));
                } else {
                    System.out.println("Role not recognised.");
                }
            } catch (Exception inner) {
                System.err.println("Error: " + inner.getMessage());
                inner.printStackTrace(); // print but keep console running
            }
        }
        System.out.println("Goodbye!");

    }

    private static final Map<String, String> passwords = new HashMap<>();

    private static void buildPasswordMap() {
        ObjectCreate.applicantMap.values()
                .forEach(a -> passwords.put(a.getNRIC(), a.getPassword()));
        ObjectCreate.officerMap.values()
                .forEach(o -> passwords.put(o.getName(), o.getPassword()));
        ObjectCreate.managerMap.values()
                .forEach(m -> passwords.put(m.getName(), m.getPassword()));
    }

    /* ------------ approve / reject pending applicant apps ---------------- */
    private static void approveApplicantsFlow(HDBManager mgr) {
        List<Application> pending = Application.applicationRegistry.stream()
                .filter(app -> app.getStatus() == ApplicationStatus.Pending)
                .toList();

        if (pending.isEmpty()) {
            System.out.println("No pending applications.");
            return;
        }

        for (int i = 0; i < pending.size(); i++) {
            Application app = pending.get(i);
            System.out.printf("[%d] %s | %s | FlatType: %s%n",
                    i, app.getApplicant().getName(),
                    app.getProject().getProjectName(),
                    app.getSelectedFlat() == null ? "N/A" : app.getSelectedFlat().getFlatType());
        }

        System.out.print("Select # to approve/reject (or -1 to cancel): ");
        int idx = Integer.parseInt(sc.nextLine());
        if (idx < 0 || idx >= pending.size())
            return;

        Application chosen = pending.get(idx);
        System.out.print("Approve this application? (yes/no): ");
        boolean approve = sc.nextLine().trim().equalsIgnoreCase("yes");

        mgr.approveApplicant(chosen); // your business method

        System.out.println(approve ? "Application approved." : "Application rejected.");
    }

    /* ------------ approve / reject officer project‑registration ---------- */
    private static void approveOfficersFlow(HDBManager mgr) {

        // collect all pending officer‑name strings across projects
        List<Project> withPending = ObjectCreate.projectList.stream()
                .filter(p -> !mgr.getPendingApprovalOfficers().isEmpty())
                .toList();

        if (withPending.isEmpty()) {
            System.out.println("No pending officer registration requests.");
            return;
        }

        int idx = 0;
        Map<Integer, Map.Entry<Project, String>> indexMap = new HashMap<>();

        for (Project p : withPending) {
            for (String offName : mgr.getPendingApprovalOfficers()) {
                System.out.printf("[%d] Officer %-12s | Project %-20s%n",
                        idx, offName, p.getProjectName());
                indexMap.put(idx++, Map.entry(p, offName));
            }
        }

        System.out.print("Select # to approve/reject (or -1 to cancel): ");
        int choice = Integer.parseInt(sc.nextLine());
        if (!indexMap.containsKey(choice))
            return;

        var entry = indexMap.get(choice);
        Project project = entry.getKey();
        String offName = entry.getValue();

        // look‑up the HDBOfficer object from global map (null if missing)
        HDBOfficer officer = ObjectCreate.officerMap.get(offName);

        System.out.print("Approve this officer? (yes/no): ");
        boolean approve = sc.nextLine().trim().equalsIgnoreCase("yes");

        // your manager’s method now gets a String or object? adapt accordingly
        mgr.approveOfficer(project); // <‑ pass officer object

        if (approve && officer != null) {
            project.getOfficers().add(officer); // officially add to project
        }
        // remove from pending list regardless of outcome
        mgr.getPendingApprovalOfficers().remove(offName);

        System.out.println(approve ? "Officer approved." : "Officer rejected.");
    }

    private static void applyFlow(Applicant a) {
        if (a.getAppliedProject() != null) {
            System.out.println("Already applied to " + a.getAppliedProject());
            return;
        }
        Project chosen = pickProject();
        if (chosen == null)
            return;
        FlatType type = pickFlatType();
        if (type == null)
            return;

        Application.applyProject(chosen, a, type);
    }

    private static void bookingFlow(Applicant a) {
        if (a.getApplicationStatus() != ApplicationStatus.Successful) {
            System.out.println("Application not Successful yet.");
            return;
        }
        Project pr = ObjectCreate.projectList.stream()
                .filter(p -> p.getProjectName().equals(a.getAppliedProject()))
                .findFirst().orElse(null);
        if (pr == null) {
            System.out.println("Project not found.");
            return;
        }

        HDBOfficer off = pr.getOfficers().isEmpty()
                ? null
                : pr.getOfficers().get(0);
        if (off == null) {
            System.out.println("No officer assigned.");
            return;
        }

        Application appObj = Application.findApplicationForApplicant(a);
        if (appObj == null) {
            System.out.println("No application found.");
            return;
        }

        System.out.print("Choose flat type to book (2/3): ");
        String in = sc.nextLine().trim();
        FlatType type = switch (in) {
            case "2" -> FlatType.TwoRoom;
            case "3" -> FlatType.ThreeRoom;
            default -> null;
        };

        if (type == null) {
            System.out.println("Invalid choice.");
            return;
        }

        // build minimal placeholder just to carry the enum
        HDBFlat mockFlat = new HDBFlat(type, 0.0, "");

        // attempt booking via officer
        boolean ok = Application.bookFlat(mockFlat, a, off); // or off.updateFlatCount(...)
        System.out.println(ok ? "Flat booked!" : "Booking failed (no units).");

    }

      /* ----- extracted method for clarity (same logic you had) ------- */
    private static void viewAndBookApplications(HDBOfficer o) {
        System.out.println("\n--- Viewing All Applications ---");
        for (Application app : Application.applicationRegistry) {
            Applicant applicant = app.getApplicant();
            Project   project   = app.getProject();
            ApplicationStatus st= app.getStatus();

            System.out.printf("Applicant: %-20s | Project: %-25s | Status: %-12s%n",
                            applicant.getName(), project.getProjectName(), st);

            if (st == ApplicationStatus.Booked && app.getSelectedFlat() != null) {
                System.out.println("  - Selected Flat: " +
                                app.getSelectedFlat().getFlatType());
            }

            if (st == ApplicationStatus.Successful) {
                System.out.print("Book a flat for this applicant? (yes/no): ");
                if (sc.nextLine().trim().equalsIgnoreCase("yes")) {

                    HDBFlat chosen = app.getSelectedFlat();
                    if (chosen == null) {
                        System.out.println("No flat selected for this application.");
                        continue;
                    }

                    boolean ok = o.updateFlatCount(project,
                                                chosen.getFlatType(),
                                                app);
                    if (ok) {
                        applicant.setApplicationStatus(ApplicationStatus.Booked);
                        System.out.println("Flat booked for " + applicant.getName() + ".");
                    } else {
                        System.out.println("Booking failed (no units).");
                    }
                }
            }
        }
    }

    /* ----------------------- Applicant ------------------------- */
    private static void applicantMenu(Applicant a) {
        while (true) {
            System.out.printf("%nApplicant %s | Status: %s%n",
                              a.getName(), a.getApplicationStatus());
            System.out.println("[1] List projects");
            System.out.println("[2] Apply for a project");
            System.out.println("[3] Book flat (if Successful)");
            System.out.println("[4] View my application");
            System.out.println("[5] Withdraw my application");   // ← new option
            System.out.println("[6] Logout");
            System.out.print("Choice: ");
            String c = sc.nextLine().trim();
            if ("6".equals(c)) break;
    
            switch (c) {
                case "1" -> ObjectCreate.projectList.forEach(p ->
                         System.out.println(p.getProjectName() + " " +
                                            p.getAvailableFlatsByType()));
                case "2" -> applyFlow(a);
                case "3" -> bookingFlow(a);
                case "4" -> a.viewProject();
                case "5" ->     {Application app = Application.findApplicationForApplicant(a);
                                if (app == null) {System.out.println("No application found to withdraw.");return;}
                                app.withdrawProject();               // method you provided in Application
                                Application.applicationRegistry.remove(app);}  // remove from pending list
                default  -> System.out.println("Invalid choice.");
            }
        }
    }
   
    /* ----------------------- Officer menu --------------------------- */
    private static void officerMenu(HDBOfficer o) {
        System.out.printf("%nOfficer %s logged in%n", o.getName());

        officerLoop:
        while (true) {
            System.out.println("""
                [1] List all projects & stock
                [2] List / manage applications
                [3] Applicant functions
                [4] Logout""");
            System.out.print("Choice: ");
            String sel = sc.nextLine().trim();

            switch (sel) {

                case "1" -> ObjectCreate.projectList.forEach(p ->
                            System.out.println(p.getProjectName() + " " +
                                            p.getAvailableFlatsByType()));

                case "2" -> viewAndBookApplications(o);   // extracted block

                case "3" -> {if (ObjectCreate.officerMap.containsKey(o.getName())) {        // officer logged in
                                    HDBOfficer officer = ObjectCreate.officerMap.get(o.getName());
                                
                                    /* up‑cast the officer to Applicant */
                                    Applicant officerAsApplicant = (Applicant) officer;   // implicit up‑cast
                                
                                    /* you can now call applicant‑only code with that reference */
                                    applicantMenu(officerAsApplicant);        // reuse normal applicant flow}
                                
                                applicantMenu(officerAsApplicant);}}             // officer acts as applicant

                case "4" -> { break officerLoop; }

                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }






    /* ----------------------- Manager --------------------------- */
    private static void managerMenu(HDBManager m) {
        System.out.printf("%nManager %s logged in%n", m.getName());

        while (true) {
            System.out.println("""
                    [1] List all projects & stock
                    [2] Approve / Reject applicant applications
                    [3] Approve / Reject officer registration
                    [4] Logout""");
            System.out.print("Choice: ");
            String ch = sc.nextLine().trim();

            switch (ch) {
                case "1" -> // list projects
                    ObjectCreate.projectList
                            .forEach(p -> System.out.println(p.getProjectName() + " " + p.getAvailableFlatsByType()));

                case "2" -> approveApplicantsFlow(m);

                case "3" -> approveOfficersFlow(m);

                case "4" -> {
                    return;
                }

                default -> System.out.println("Invalid choice.");
            }
        }
    }

    /* ---------------- utility pickers -------------------------- */
    private static Project pickProject() {
        List<Project> list = ObjectCreate.projectList;
        if (list.isEmpty()) {
            System.out.println("No projects.");
            return null;
        }
        for (int i = 0; i < list.size(); i++)
            System.out.println("[" + i + "] " + list.get(i).getProjectName());
        System.out.print("Choose project: ");
        int choice = Integer.parseInt(sc.nextLine());
        if (choice < 0 || choice >= list.size())
            return null; // corrected variable name from idx to choice
        return list.get(choice); // corrected variable name from idx to choice
    }

    private static FlatType pickFlatType() {
        System.out.print("Flat type (2/3): ");
        return switch (sc.nextLine().trim()) {
            case "2" -> FlatType.TwoRoom;
            case "3" -> FlatType.ThreeRoom;
            default -> null;
        };
    }

    // Test Enquiry reply

    // Map<String,String> msg = new HashMap<>();
    // msg.put("Applicant123", "When is key collection?");
    // Enquiry enquiry = new Enquiry(msg, 1, 123, null, 0);

    // // officer.replyEnquiry(enquiry);

    // // // 3. print stored reply to verify
    // // System.out.println("\n=== Stored in Enquiry object ===");
    // // System.out.println("Responder ID : " + enquiry.getResponderID());
    // // System.out.println("Reply : " + enquiry.getResponse());

    // Initialize applicantMap

    // String[] result = officer.getApplication("S1234567A",
    // ObjectCreate.applicantMap);

    // if (result != null) {
    // System.out.println("Applied project: " + result[0]);
    // System.out.println("Selected flat : " + result[1]);
    // } else {
    // System.out.println("NRIC not found.");
    // }

    // Write to file example
    // data.add(List.of("John Doe", "12345678A"));
    // FileOps.writeFile("ApplicantList", data);

}