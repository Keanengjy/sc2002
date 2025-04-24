package person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.ApplicationStatus;
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
        this.HDBOfficersUnder = "";
        this.managedProjects = new ArrayList<>();
        this.pendingApprovals = new ArrayList<>();
        this.pendingApprovalOfficers = new ArrayList<>();
        //this.setRole(UserRole.HDBManager);
    }

    public void createProject() {
        // Create a new project and add it to managed projects
        Project project = new Project();
        project.setManager(this.getNRIC());
        project.setVisibility(Visibility.off);
        managedProjects.add(project);
    }

    public void editProject(String projectName, String string) {
        for (Project project : managedProjects) {
            if (project.getProjectName().equals(projectName)) {
                project.setProjectName(string);
                break;
            }
        }
    }

    public void deleteProject(String projectName, String string) {
        managedProjects.removeIf(project -> project.getProjectName().equals(projectName));
    }

    public void toggleProjectVisibility(Visibility visibility, Boolean bool) {
        for (Project project : managedProjects) {
            if (project.getManager().equals(this.getNRIC())) {
                project.setVisibility(visibility);
            }
        }
    }

    public void replyEnquiry(String reply) {
        //ADD METHOD
    }
    
    public void approveOfficer() {
        if (!pendingApprovalOfficers.isEmpty()) {
            String officerInfo = pendingApprovalOfficers.remove(0);
            // Add to officers under this manager
            if (HDBOfficersUnder.isEmpty()) {
                HDBOfficersUnder = officerInfo;
            } else {
                HDBOfficersUnder += "," + officerInfo;
            }
            
            // Update available slots in the project
            String projectName = officerInfo.split(":")[1];
            for (Project project : managedProjects) {
                if (project.getProjectName().equals(projectName)) {
                    project.setAvailableHDBOfficerSlots(project.getAvailableHDBOfficerSlots() - 1);
                    break;
                }
            }
        }
    }

    public void applicationDecision(String decision, String string) {
        // Find the applicant's application
        ApplicationStatus status;
        if (decision.equalsIgnoreCase("Approved")) {
            status = ApplicationStatus.Approved;
        } else {
            status = ApplicationStatus.Rejected;
        }
        
        // Remove from pending approvals
        pendingApprovals.remove(string);
    }
    /* 
    public void processPendingApprovals() {
        for (Map.Entry<String, HDBOfficer> entry : pendingApprovals.entrySet()) {
            String project = entry.getKey();
            HDBOfficer officer = entry.getValue();

            System.out.println("Pending approval for project: " + project + " - Officer: " + officer.getName());
        }
    }
    */

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