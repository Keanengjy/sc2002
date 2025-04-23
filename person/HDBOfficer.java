package person;

import project.ApplicationStatus;
import person.HDBManager;
import project.Project;
import project.UserRole;
import java.util.Map;

public class HDBOfficer extends AbstractUser {
    private String projects;
    private String registeredProject;
    private ApplicationStatus registeredProjectStatus;

    public HDBOfficer(String name, String NRIC, int age, String maritalStatus, String password, String projects) {
        super(name, NRIC, age, maritalStatus, password);
        this.projects = projects;
        this.registeredProjectStatus = ApplicationStatus.Pending;
    }

    public void setRegisteredProjectStatus(ApplicationStatus status) {
        this.registeredProjectStatus = status;
    }

    public ApplicationStatus getRegisteredProjectStatus() {
        return this.registeredProjectStatus;
    }

    public void setRegisteredProject(String project) {
        this.registeredProject = project;
    }

    public String getRegisteredProject() {
        return registeredProject;
    }

    public void registerProject(Project p) {

        this.registeredProjectStatus = ApplicationStatus.Pending;
        p.getManager().applicationDecision(p.getProjectName(), this);
        System.out.println(name + " registering for project: " + p.getProjectName() + ". Awaiting approval.");
    }

    public void getProjectDetails() {
        // if (registeredProjectStatus == ApplicationStatus.Approved) {
        //     HDBManager manager = new HDBManager();
        //     manager.approveOfficer();
        // }
    }
    public void applyEnquiryReply(String enquiry) {}
    public void updateFlatCount() {}
    public void generateReceipt() {}
    public void updateApplicationList() {}
    public void updateInfo() {}

    public void viewStatus() {
        System.out.println("Status: " + registeredProjectStatus);
    }

    @Override
    public UserRole getRole() {
        return UserRole.HDBOfficer;
    }

    @Override
    public boolean checkEligibility(Project project) {
        // Example rule: Officers are eligible if they are logged in and have any project assigned

        return this.loggedIn && registeredProject != null && registeredProject.isEmpty();
    }

    public boolean isEmpty(){
        return registeredProject == null;
    }
}
