package people;

import java.util.List;
import project.Project;

enum UserRole {
    Applicant,
    HDBOfficer,
    HDBManager
}

enum Visibility {
    on,
    off
}


public class HDBManager extends AbstractUser {
    private List<HDBOfficer> HDBOfficersUnder;
    private List<Project> managedProjects;
    private List<String> pendingApprovals;

    public void createProject() {}
    public void deleteProject(String name) {}
    public void toggleProjectVisibility(boolean visibility) {}
    public void toggleApplication(String decision) {}
    public void replyEnquiry(String reply) {}
    public void approveOfficer() {}

    @Override
    public UserRole getRole() {
        return UserRole.HDBManager;
    }

    @Override
    public boolean checkEligibility(Project project) {
        // Managers always have access, or could be based on a rule
        return super.checkEligibility(project);
    }
}