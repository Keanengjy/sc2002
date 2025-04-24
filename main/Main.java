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

    /* ----------------------- Applicant ------------------------- */
    private static void applicantMenu(Applicant a) {
        while (true) {
            System.out.printf("%nApplicant %s | Status: %s%n",
                    a.getName(), a.getApplicationStatus());
            System.out.println("[1] List projects");
            System.out.println("[2] Apply for a project");
            System.out.println("[3] Book flat (if Successful)");
            System.out.println("[4] Logout");
            System.out.print("Choice: ");
            String c = sc.nextLine().trim();
            if ("4".equals(c))
                break;

            switch (c) {
                case "1" -> ObjectCreate.projectList.forEach(p -> System.out.println(p.getProjectName() + " " +
                        p.getAvailableFlatsByType()));
                case "2" -> applyFlow(a);
                case "3" -> bookingFlow(a);
            }
        }
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

        FlatType type = FlatType.valueOf(a.getSelectedFlat());
        HDBFlat dummy = new HDBFlat(type, 0, "");
        Application.bookFlat(dummy, a, off);
    }

    /* ----------------------- Officer --------------------------- */
    private static void officerMenu(HDBOfficer o) {
        System.out.printf("%nOfficer %s logged in%n", o.getName());
        while (true) {
            System.out.println("[1] View applications");
            System.out.println("[2] Logout");
            System.out.print("Choice: ");
            String choice = sc.nextLine().trim();

            if ("1".equals(choice)) {
                while (true) {
                    System.out.println("[1] List all projects & stock");
                    System.out.println("[2] List all applications");
                    System.out.println("[3] Logout");
                    System.out.print("Choice: ");
                    String subChoice = sc.nextLine().trim();

                    if ("1".equals(subChoice)) {
                        // List all projects and their available flats
                        ObjectCreate.projectList.forEach(
                                p -> System.out.println(p.getProjectName() + " " + p.getAvailableFlatsByType()));
                    } else if ("2".equals(subChoice)) {
                        // List all applications for the officer to review
                        System.out.println("\n--- Viewing All Applications ---");
                        for (Application app : Application.applicationRegistry) {
                            Applicant applicant = app.getApplicant();
                            Project project = app.getProject();
                            ApplicationStatus status = app.getStatus();

                            System.out.printf("Applicant: %-20s | Project: %-25s | Status: %-12s%n",
                                    applicant.getName(), project.getProjectName(), status);

                            // Show selected flat if the status is Booked
                            if (status == ApplicationStatus.Booked) {
                                HDBFlat selectedFlat = app.getSelectedFlat(); // Get selected flat directly from the
                                                                              // Application
                                System.out.println("  - Selected Flat: " + selectedFlat.getFlatType());
                            }

                            // If the application status is "Successful", allow booking
                            if (status == ApplicationStatus.Successful) {
                                System.out.println("Do you want to book a flat for this applicant? (yes/no): ");
                                String response = sc.nextLine().trim();

                                if ("yes".equalsIgnoreCase(response)) {
                                    // Attempt to book the flat for this applicant
                                    boolean success = o.updateFlatCount(project,
                                            FlatType.valueOf(applicant.getSelectedFlat()), app);

                                    if (success) {
                                        System.out.println("Flat successfully booked for " + applicant.getName() + ".");
                                        applicant.setApplicationStatus(ApplicationStatus.Booked);
                                    } else {
                                        System.out.println("Failed to book flat for " + applicant.getName()
                                                + ". No more units available.");
                                    }
                                }
                            }
                        }
                    } else if ("3".equals(subChoice)) {
                        // Logout
                        break;
                    } else {
                        System.out.println("Invalid choice. Please try again.");
                    }
                }
            } else if ("2".equals(choice)) {
                break;
            }
        }
    }

    /* ----------------------- Manager --------------------------- */
    private static void managerMenu(HDBManager m) {
        System.out.printf("%nManager %s logged in%n", m.getName());
        while (true) {
            System.out.println("[1] List all projects & stock");
            System.out.println("[2] Logout");
            if ("2".equals(sc.nextLine().trim()))
                break;
            ObjectCreate.projectList
                    .forEach(p -> System.out.println(p.getProjectName() + " " + p.getAvailableFlatsByType()));
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

    // //Book flats

    // // 1) Print initial stock
    // System.out.println("Initial stock: " + p.getFlats());

    // // 2) Extract HDBFlat keys by type

    // // 3) Book three 2-room units
    // for (int i = 1; i <= 3; i++) {
    // boolean ok = officer.updateFlatCount(p, FlatType.TwoRoom);
    // System.out.printf(" Attempt %d to book 2-room: %s%n",
    // i, ok ? "SUCCESS" : "FAILED");
    // }

    // // 4) Book three 3-room units
    // for (int i = 1; i <= 3; i++) {
    // boolean ok = officer.updateFlatCount(p, FlatType.ThreeRoom);
    // System.out.printf(" Attempt %d to book 3-room: %s%n",
    // i, ok ? "SUCCESS" : "FAILED");
    // }

    // // 5) Print final stock
    // System.out.println("\nFinal stock: " + p.getFlats());

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