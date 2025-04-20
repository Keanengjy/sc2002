package people;

import project.Project;

enum UserRole {
    Applicant,
    HDBOfficer,
    HDBManager
}

class HDBOfficer extends AbstractUser {
    private String registeredProjects;
    private String registeredProjectStatus;

    public void registerProject(Project p) {}
    public void getProjectDetails() {}
    public void applyEnquiryReply(String enquiry) {}
    public void updateFlatCount() {}
    public void generateReceipt() {}
    public void updateApplicationList() {}
    public void updateInfo() {}
    public void viewStatus() {}

    @Override
    public UserRole getRole() {
        return UserRole.HDBOfficer;
    }

@Override
public boolean checkEligibility(Project project) {
    // Example rule: Officers are eligible if they are logged in and have any project assigned
    return this.loggedIn && registeredProjects != null && !registeredProjects.isEmpty();
}
