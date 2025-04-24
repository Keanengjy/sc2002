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
    public static void approveOfficersFlow(HDBManager m) {
        System.out.println("\n--- Approving Officer Registration ---");

        // Check if there are any officers pending approval
        if (m.getPendingApprovalOfficers().isEmpty()) {
            System.out.println("No pending officer registration requests.");
            return;
        }

        List<String> toRemove = new ArrayList<>();

        for (String Name : m.getPendingApprovalOfficers()) {
            HDBOfficer officer = ObjectCreate.officerMap.get(Name);
            if (officer == null) {
                toRemove.add(Name);
                continue;
            }

            System.out.print("Approve this officer " + Name + "? (yes/no): ");
            if ("yes".equalsIgnoreCase(sc.nextLine().trim())) {
                m.approveOfficer(officer); // change status only
                System.out.println("Officer " + Name + " approved.");
            } else {
                System.out.println("Officer " + Name + " rejected.");
            }
            toRemove.add(Name); // mark for deletion
        }

        // remove after the loop ‑‑ safe
        m.getPendingApprovalOfficers().removeAll(toRemove);
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

    /* ----- View and Book Flats for Applicants ------- */
    private static void viewAndBookApplications(HDBOfficer o) {
        System.out.println("\n--- Viewing All Applications ---");
        for (Application app : Application.applicationRegistry) {

            Applicant applicant = app.getApplicant();
            Project project = app.getProject();
            ApplicationStatus st = app.getStatus();

            System.out.printf("Applicant: %-20s | Project: %-25s | Status: %-12s%n",
                    applicant.getName(), project.getProjectName(), st);

            if (st != ApplicationStatus.Successful)
                continue;

            /* ask if officer wants to book */
            System.out.print("Book a flat for this applicant? (yes/no): ");
            if (!sc.nextLine().trim().equalsIgnoreCase("yes"))
                continue;

            /* choose flat type */
            System.out.print("Enter flat type to book (2/3): ");
            FlatType type = switch (sc.nextLine().trim()) {
                case "2" -> FlatType.TwoRoom;
                case "3" -> FlatType.ThreeRoom;
                default -> null;
            };
            if (type == null) {
                System.out.println("Invalid flat type.");
                continue;
            }

            /* ---- update applicant & application via updateInfo ---- */
            boolean infoOK = o.updateInfo(applicant.getNRIC(),
                    ObjectCreate.applicantMap,
                    project,
                    type,
                    app);
            if (!infoOK) {
                System.out.println("Booking failed (unit not available).");
                continue;
            }

            /* ---- decrement stock ---------------------------------- */
            boolean stockOK = o.updateFlatCount(project, type, app);
            if (stockOK) {
                System.out.println("Flat booked for " + applicant.getName() + ".");
                System.out.println(project.getFlats());

            } else {
                System.out.println("Booking failed while updating stock.");
                // roll back statuses if needed
                applicant.setApplicationStatus(ApplicationStatus.Successful);
                app.setStatus(ApplicationStatus.Successful);
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
            System.out.println("[5] Withdraw my application"); // ← new option
            System.out.println("[6] Submit enquiry"); // ← new option
            System.out.println("[7] Edit enquiry"); // ← new option
            System.out.println("[8] Delete enquiry"); // ← new option
            System.out.println("[9] View all enquiries"); // ← new option
            System.out.println("[10] Logout"); // updated to reflect new option
            System.out.print("Choice: ");
            String c = sc.nextLine().trim();
            if ("10".equals(c)) // updated to reflect new option
                break;

            switch (c) {
                case "1" -> ObjectCreate.projectList.forEach(p -> System.out.println(p.getProjectName() + " " +
                        p.getFlats()));
                case "2" -> applyFlow(a);
                case "3" -> bookingFlow(a);
                case "4" -> a.viewProject();
                case "5" -> {
                    Application app = Application.findApplicationForApplicant(a);
                    if (app == null) {
                        System.out.println("No application found to withdraw.");
                        return;
                    }
                    app.withdrawProject(); // method you provided in Application
                    Application.applicationRegistry.remove(app);
                } // remove from pending list
                case "6" -> {           // in applicantMenu, for example
                    Enquiry e = a.submitEnquiry();
                    if (e != null) {
                        // do anything else (notify officers, etc.)
                    }
                }
                case "7" -> a.editEnquiry(sc);
                case "8" -> {                       // Delete an enquiry
                    System.out.print("Enter enquiry ID to delete: ");
                    int id;
                    try {
                        id = Integer.parseInt(sc.nextLine().trim());
                    } catch (NumberFormatException ex) {
                        System.out.println("Invalid ID.");
                        break;
                    }
                
                    boolean ok = a.deleteEnquiry(id);   // calls your method
                    if (ok) {
                        System.out.println("Enquiry deleted successfully.");

                    }
                }
                case "9" -> {
                    System.out.println(Enquiry.viewAllEnquiries(ObjectCreate.enquiryList));
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    /* ----------------------- Officer menu --------------------------- */
    private static void officerMenu(HDBOfficer o) {
        System.out.printf("%nOfficer %s logged in%n", o.getName());
    
        officerLoop: while (true) {
            System.out.println("""
                    [1] List all projects & stock
                    [2] List / manage applications
                    [3] Apply for a project
                    [4] Applicant functions
                    [5] Get Application by NRIC
                    [6] Generate Receipt for Booked Application
                    [7] Reply to Enquiries
                    [8] View all enquiries
                    [9] Logout""");
            System.out.print("Choice: ");
            String sel = sc.nextLine().trim();
    
            switch (sel) {
    
                case "1" -> ObjectCreate.projectList.forEach(p -> 
                        System.out.println(p.getProjectName() + " " + p.getFlats()));
    
                case "2" -> {
                    // Manage and book applications
                    viewAndBookApplications(o);
                }
    
                case "3" -> { // Officer applies for a project
                    System.out.print("Enter project name to apply: ");
                    String projectName = sc.nextLine().trim();
                    Project project = ObjectCreate.projectList.stream()
                            .filter(p -> p.getProjectName().equals(projectName))
                            .findFirst()
                            .orElse(null);
                    if (project != null) {
                        o.registerProject(project);
                        System.out.println("You have applied for " + project.getProjectName() + ".");
                    } else {
                        System.out.println("Project not found.");
                    }
                }
    
                case "4" -> applicantMenu(o); // Up-cast implicit
    
                case "5" -> { 
                    // Get application details by NRIC
                    System.out.print("Enter the NRIC of the applicant: ");
                    String nric = sc.nextLine().trim();
    
                    // Assume you have an application object
                    Application app = Application.applicationRegistry.stream()
                            .filter(a -> a.getApplicant().getNRIC().equals(nric))
                            .findFirst()
                            .orElse(null);
                    
                    if (app != null) {
                        // Call getApplication to retrieve project and flat details
                        String[] appDetails = o.getApplication(nric, ObjectCreate.applicantMap, app);
                        
                        if (appDetails != null) {
                            System.out.printf("Applicant's Project: %s%n", appDetails[0]);
                            System.out.printf("Selected Flat: %s%n", appDetails[1]);
                        } else {
                            System.out.println("No application found for this NRIC.");
                        }
                    } else {
                        System.out.println("No application found for this NRIC.");
                    }
                }
    
                case "6" -> { 
                    // Generate receipt for a booked application
                    System.out.print("Enter the NRIC of the applicant to generate a receipt: ");
                    String nric = sc.nextLine().trim();
    
                    // Find the application for the given NRIC
                    Application app = Application.applicationRegistry.stream()
                            .filter(a -> a.getApplicant().getNRIC().equals(nric))
                            .findFirst()
                            .orElse(null);
                    
                    if (app != null && app.getStatus() == ApplicationStatus.Booked) {
                        // Generate and display the receipt
                        Applicant applicant = app.getApplicant();
                        Project project = app.getProject();
                        String receipt = o.generateReceipt(applicant, project, app);
                        System.out.println(receipt);  // Print the generated receipt
                    } else {
                        System.out.println("No booked application found for this NRIC.");
                    }
                }

                case "7" -> o.replyEnquiries(ObjectCreate.enquiryList, sc);
                
                case "8" -> {
                    System.out.println(Enquiry.viewAllEnquiries(ObjectCreate.enquiryList));
                }
                case "9" -> {
                    // Logout
                    System.out.println("Logging out...");
                    break officerLoop; // exit the loop to log out
                }
                
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
                    [4] View all applications
                    [5] Create a new project
                    [6] Edit a project
                    [7] Delete a project
                    [8] Toggle project visibility
                    [9] View all enquiries
                    [10] Reply to enquiries
                    [11] Logout""");
            System.out.print("Choice: ");
            String ch = sc.nextLine().trim();
    
            switch (ch) {
                case "1" -> // List all projects and stock
                    ObjectCreate.projectList
                            .forEach(p -> System.out.println(p.getProjectName() + " " + p.getFlats()));
    
                case "2" -> approveApplicantsFlow(m);
    
                case "3" -> approveOfficersFlow(m);
    
                case "4" -> { 
                    // Display all applications
                    System.out.println("\n--- Viewing All Applications ---");
    
                    // Iterate over all applications in the application registry
                    for (Application app : Application.applicationRegistry) {
                        Applicant applicant = app.getApplicant();
                        Project project = app.getProject();
                        ApplicationStatus status = app.getStatus();
                        HDBFlat flat = app.getSelectedFlat(); // Flat might be null if not booked
    
                        // Display details for each application
                        System.out.printf("Applicant: %-20s | Project: %-25s | Status: %-12s%n", 
                                applicant.getName(), project.getProjectName(), status);
    
                        // If flat is booked, show the flat type
                        if (status == ApplicationStatus.Booked && flat != null) {
                            System.out.println("  - Selected Flat: " + flat.getFlatType());
                        } else if (status != ApplicationStatus.Booked) {
                            System.out.println("  - No flat booked yet.");
                        }
    
                        System.out.println("--------------------------------------------------");
                    }
                }
    
                case "5" -> { 
                    // Create a new project
                    System.out.print("Enter project name: ");
                    String projectName = sc.nextLine().trim();
                    
                    System.out.print("Enter neighborhood: ");
                    String neighborhood = sc.nextLine().trim();
                    
                    System.out.print("Enter project ID: ");
                    String projectID = sc.nextLine().trim();
                    
                    ApplicationStatus applicationStatus = ApplicationStatus.Pending;
                
                    System.out.print("Enter visibility (true/false): ");
                    boolean visibility = Boolean.parseBoolean(sc.nextLine().trim());
                
                    System.out.print("Enter application opening date (YYYY-MM-DD): ");
                    String applicationOpeningDate = sc.nextLine().trim();
                    
                    System.out.print("Enter application closing date (YYYY-MM-DD): ");
                    String applicationClosingDate = sc.nextLine().trim();
                
                    // Assuming we need to initialize available flats and their quantities (for simplicity)
                    Map<HDBFlat, Integer> availableFlats = new HashMap<>();
                    System.out.print("Enter quantity for 2-room flats: ");
                    int twoRoomQty = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Enter quantity for 3-room flats: ");
                    int threeRoomQty = Integer.parseInt(sc.nextLine().trim());
                
                    // You can create flat objects (this is just a simple example)
                    HDBFlat twoRoomFlat = new HDBFlat(FlatType.TwoRoom, 0, neighborhood); // Modify constructor as needed
                    HDBFlat threeRoomFlat = new HDBFlat(FlatType.ThreeRoom, 0, neighborhood);
                    
                    availableFlats.put(twoRoomFlat, twoRoomQty);
                    availableFlats.put(threeRoomFlat, threeRoomQty);
                
                    // List of officers assigned to the project
                    List<HDBOfficer> officers = new ArrayList<>();
                
                    System.out.print("Enter the number of available officer slots (max 10): ");
                    int availableOfficerSlots = Integer.parseInt(sc.nextLine().trim());

                    // Ensure the value does not exceed 10
                    if (availableOfficerSlots > 10) {
                        System.out.println("Maximum number of officer slots is 10. Setting to 10.");
                        availableOfficerSlots = 10;
                    }
                
                    // Now we create the project
                    
                    m.createProject(projectName, neighborhood, projectID, applicationStatus,
                    visibility, applicationOpeningDate, applicationClosingDate,
                    availableFlats, officers, availableOfficerSlots, 
                    twoRoomQty, threeRoomQty);
                    System.out.println("New project created successfully!");
                }
    
                case "6" -> { 
                    // Edit a project
                    System.out.print("Enter the project name to edit: ");
                    String projectName = sc.nextLine().trim();
                    System.out.print("Enter new project name: ");
                    String newName = sc.nextLine().trim();
                    m.editProject(projectName, newName);
                    System.out.println("Project name updated successfully!");
                }
    
                case "7" -> { 
                    // Delete a project
                    System.out.print("Enter the project name to delete: ");
                    String projectName = sc.nextLine().trim();
                    m.deleteProject(projectName);
                    System.out.println("Project deleted successfully!");
                }
    
                case "8" -> { 
                    // Toggle project visibility
                    System.out.print("Enter the project name to toggle visibility: ");
                    String projectName = sc.nextLine().trim();
                    System.out.print("Enter visibility (true/false): ");
                    boolean isVisible = Boolean.parseBoolean(sc.nextLine().trim());
                    m.toggleProjectVisibility(Visibility.ON, isVisible);
                    System.out.println("Project visibility updated!");
                }
    
                case "9" -> {
                    // View all enquiries
                    System.out.println(Enquiry.viewAllEnquiries(ObjectCreate.enquiryList));
                }
                case "10" -> {
                    // Reply to enquiries
                    m.replyEnquiries(ObjectCreate.enquiryList, sc);
                }
                case "11" -> {
                    System.out.println("Logging out...");
                    return; // exit the loop to log out
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

}