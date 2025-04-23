package person;

import project.ApplicationStatus;
import project.FlatType;
import project.Visibility;
import person.MaritalStatus;
import project.UserRole;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import project.Project;


public class HDBManager extends AbstractUser {
    private List<HDBOfficer> HDBOfficersUnder;
    private String managedProjects;
    private Map<String, HDBOfficer> pendingApprovals;

    public HDBManager(String name, String NRIC, int age, String maritalStatus, String password) {
        super(name, NRIC, age, maritalStatus, password);
        this.pendingApprovals = new HashMap<>();
        this.HDBOfficersUnder = new ArrayList<>();
        this.managedProjects = "";
    }

    public void createProject() {}
    public void deleteProject(String name) {}
    public void toggleProjectVisibility(boolean visibility) {}
    public void toggleApplication(String decision) {}
    public void replyEnquiry(String reply) {}
    public void approveOfficer() {}

    public void applicationDecision(String projectName, HDBOfficer officer) {
        pendingApprovals.put(projectName, officer);
    }
    
    public void processPendingApprovals() {
        for (Map.Entry<String, HDBOfficer> entry : pendingApprovals.entrySet()) {
            String project = entry.getKey();
            HDBOfficer officer = entry.getValue();

            System.out.println("Pending approval for project: " + project + " - Officer: " + officer.getName());
        }
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
}