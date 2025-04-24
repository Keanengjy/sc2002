package person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import project.ApplicationStatus;
import project.Enquiry;
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

    public void createProject(Project project) {
        project.setManager(this);
        project.setVisibility(false);
        managedProjects.add(project);
    }

    public void editProject(String projectName, String newName) {
        for (Project project : managedProjects) {
            if (project.getProjectName().equals(projectName)) {
                project.setProjectName(newName);
                break;
            }
        }
    }

    public void deleteProject(String projectName) {
        //remove obj
        managedProjects.removeIf(project -> project.getProjectName().equals(projectName));
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

    // public void approveOfficer(Project projectName) {
    //     System.out.println(pendingApprovals.size() + " pending approvals.");
    //     HDBOfficer officer = pendingApprovals.get(projectName.getProjectName());
    //     if (officer != null) {
    //         officer.setRegisteredProject(projectName);  // assuming this setter exists
    //         officer.setRegisteredProjectStatus(ApplicationStatus.Successful);  // if you have a status attribute
    //         pendingApprovals.remove(projectName.getProjectName()); // remove from pending after approval
    //     } else {
    //         System.out.println("No pending officer found for project " + projectName);
    //     }
    // }

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
        return this.pendingApprovalOfficers;
    }
    public void addPendingApprovalOfficer(String officerName) {
        if (!pendingApprovalOfficers.contains(officerName)) {
            pendingApprovalOfficers.add(officerName);
        }
    }
}