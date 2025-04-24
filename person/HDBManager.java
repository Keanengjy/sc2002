package person;

import java.util.ArrayList;
import java.util.List;
import project.ApplicationStatus;
import project.Enquiry;
import project.Project;
import project.UserRole;
import project.Visibility;


public class HDBManager extends AbstractUser {
    private String HDBOfficersUnder;
    private List<Project> managedProjects;
    private List<String> pendingApprovals;
    private List<String> pendingApprovalOfficers;

    public HDBManager(String name, String NRIC, int age, String maritalStatus, String password) {
        super(name, NRIC, age, maritalStatus, password);
        this.pendingApprovals = new ArrayList<>();
        this.pendingApprovalOfficers = new ArrayList<>();
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

    public void approveOfficer(HDBOfficer officer, Project project) {
        if (officer != null && project != null) {
            // Approve the officer for the project
            project.addOfficer(officer);
            // Update available slots
            // Here we would update the available slots count
            System.out.println("Officer " + officer.getName() + " approved for project " + project.getProjectName());
            // Remove from pending approvals
            pendingApprovalOfficers.remove(officer.getNRIC());
        }
    }

    public void applicationDecision(String decision, int applicationId) {
        //pendingApprovals.put(projectName, officer);
        ApplicationStatus status;
        if (decision.equalsIgnoreCase("Approved")) {
            status = ApplicationStatus.Successful;
        } else {
            status = ApplicationStatus.Unsuccessful;
        }
        System.out.println("Application " + applicationId + " status updated to " + status);
        // Remove from pending approvals
        pendingApprovals.remove(applicationId);
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

    public List<String> getPendingApprovals() {
        return this.pendingApprovals;
    }
    public void setPendingApprovals(List<String> approvals) {
        this.pendingApprovals = approvals;
    }

    public List<String> getPendingApprovalOfficers() {
        return this.pendingApprovalOfficers;
    }
    public void setPendingApprovalOfficers(List<String> officers) {
        this.pendingApprovalOfficers = officers;
    }
}