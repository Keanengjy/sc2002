package person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import fileops.ObjectCreate;
import project.ApplicationStatus;
import project.Enquiry;
import project.HDBFlat;
import project.Project;
import project.UserRole;
import project.Visibility;


public class HDBManager extends AbstractUser {
    private String HDBOfficersUnder;
    private List<Project> managedProjects;
    private Map<String, HDBOfficer> pendingApprovals = new HashMap<>();
    private static List<String> pendingApprovalOfficers = new ArrayList<>();

    public HDBManager(String name, String NRIC, int age, String maritalStatus, String password) {
        super(name, NRIC, age, maritalStatus, password);
        this.pendingApprovals = new HashMap<>();
        HDBManager.pendingApprovalOfficers = new ArrayList<>();
        this.managedProjects = new ArrayList<>();
        this.HDBOfficersUnder = "";
    }

    public void createProject(String projectName, String neighborhood, String projectID, 
                            ApplicationStatus applicationStatus, boolean visibility, 
                            String applicationOpeningDate, String applicationClosingDate,
                            Map<HDBFlat, Integer> availableFlats, List<HDBOfficer> officers, 
                            int availableOfficerSlots, int twoRoomQty, int threeRoomQty) {

        // Create a new Project object using the constructor
        Project newProject = new Project(projectName, neighborhood, this, projectID, applicationStatus,
                                        visibility, applicationOpeningDate, applicationClosingDate,
                                        availableFlats, officers, availableOfficerSlots, 
                                        twoRoomQty, threeRoomQty);

        // Add the new project to the project list (assuming ObjectCreate.projectList holds the projects)
        ObjectCreate.projectList.add(newProject);
        managedProjects.add(newProject);
        newProject.setManager(this);
        
        System.out.println("New project created and added to the project list successfully!");
    }


    public void editProject(String projectName, String newName) {
        // Iterate through the list of managed projects
        for (Project project : managedProjects) {
            if (project.getProjectName().equals(projectName)) {
                // Set the new project name
                project.setProjectName(newName);
                
                // If the project is in the global project list (e.g., ObjectCreate.projectList), update it there as well
                for (Project p : ObjectCreate.projectList) {
                    if (p.getProjectName().equals(projectName)) {
                        // Update the project name in the global project list
                        p.setProjectName(newName);
                        break;  // Stop searching once the project is found and updated
                    }
                }
                System.out.println("Project name updated successfully.");
                break; // Exit the loop once the project is found and updated
            }
        }
    }
    

    public void deleteProject(String projectName) {
        // Remove from the managedProjects list
        managedProjects.removeIf(project -> project.getProjectName().equals(projectName));
        
        // Remove from the global project list (ObjectCreate.projectList)
        ObjectCreate.projectList.removeIf(project -> project.getProjectName().equals(projectName));
        
        System.out.println("Project deleted successfully.");
    }
    

    public void toggleProjectVisibility(Visibility visibility, boolean isVisible) {
        for (Project project : managedProjects) {
            project.setVisibility(isVisible);
        }
    }
    
    public void replyEnquiry(Enquiry enquiry, String reply) {
        //do smth
        enquiry.setResponse(reply);
        enquiry.setResponderID(Integer.parseInt(this.getNRIC()));
    }

    public void approveOfficer(HDBOfficer officer) {

        officer.setRegisteredProjectStatus(ApplicationStatus.Successful); // Approve officer's registration
        // HDBManager.pendingApprovalOfficers.remove(officer.getName());  // Remove from pending list
    }

    public void applicationDecision(String projectName, HDBOfficer officer) {
        pendingApprovals.put(projectName, officer);
    }

    public void approveApplicant(Application app) {

        if (app == null) {
            System.out.println("No application supplied.");
            return;
        }
        // 1) flip statuses
        app.setStatus(ApplicationStatus.Successful);
        app.getApplicant().setApplicationStatus(ApplicationStatus.Successful);


        // // 2) remove from pending list (if still there)
        // Application.applicationRegistry.remove(app);

        System.out.printf("Manager %s approved application of %s for project \"%s\".%n",
                          name,
                          app.getApplicant().getName(),
                          app.getProject().getProjectName());
    }

        public void replyEnquiries(List<Enquiry> list, Scanner sc) {

        // collect unanswered enquiries
        List<Enquiry> pending = list.stream()
                .filter(e -> e.getResponse() == null || e.getResponse().isBlank())
                .toList();
    
        if (pending.isEmpty()) {
            System.out.println("No enquiries awaiting reply.");
            return;
        }
    
        System.out.println("\n--- Unanswered Enquiries ---");
        for (int i = 0; i < pending.size(); i++) {
            Enquiry e = pending.get(i);
            String msg = e.getMessage().values().iterator().next(); // single entry
            System.out.printf("[%d] ID:%d  From:%s  \"%s\"%n",
                    i, e.getEnquiryID(), e.getSenderID(), msg);
        }
    
        System.out.print("Select enquiry number to reply (or -1 to cancel): ");
        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException ex) {
            System.out.println("Invalid input.");
            return;
        }
        if (choice < 0 || choice >= pending.size()) return;
    
        Enquiry target = pending.get(choice);
    
        System.out.print("Enter your reply: ");
        String response = sc.nextLine().trim();
        if (response.isBlank()) {
            System.out.println("Reply not sent (empty).");
            return;
        }
    
        target.setResponse(response);
        System.out.println("Reply sent to enquiry " + target.getEnquiryID());
    }
    

    @Override
    public UserRole getRole() {
        return UserRole.HDBManager;
    }

    @Override
    public boolean checkEligibility(Project project) {
        // Managers always have access, or could be based on a rule
        return true;
    }

    public String getHDBOfficersUnder() {
        return this.HDBOfficersUnder;
    }

    public void setHDBOfficersUnder(String officers) {
        this.HDBOfficersUnder = officers;
    }

    
    public List<Project> getManagedProjects() {
        return this.managedProjects;
    }

    public void setManagedProjects(List<Project> projects) {
        this.managedProjects = projects;
    }

    public Map<String, HDBOfficer> getPendingApprovals() {
        return this.pendingApprovals;
    }
    public void setPendingApprovals(Map<String, HDBOfficer> approvals) {
        this.pendingApprovals = approvals;
    }

    public List<String> getPendingApprovalOfficers() {
        return HDBManager.pendingApprovalOfficers;
    }
    public void addPendingApprovalOfficer(String officerName) {
        if (!pendingApprovalOfficers.contains(officerName)) {
            pendingApprovalOfficers.add(officerName);
        }
    }
}