package person;

import project.ApplicationStatus;
import project.FlatType;
import project.Visibility;
import person.MaritalStatus;
import project.UserRole;
import project.HDBFlat;
import project.Project;

public class Applicant extends AbstractUser {
    private String appliedProject;
    private String applicationStatus;
    private HDBFlat selectedFlat;
    private String maritalStatus; // Add maritalStatus field

    public Applicant(String name, String NRIC, int age, String maritalStatus, String password) {
        super(name, NRIC, age, maritalStatus, password); // Call to parent constructor
        this.appliedProject = "";
        this.applicationStatus = "";
        this.selectedFlat = null;

    }

    public String getMaritalStatus() { // Update return type for getter
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) { // Update setter for maritalStatus
        this.maritalStatus = maritalStatus;
    }

    public void viewProject() {
        System.out.println("Applied Project: " + appliedProject);

    }

    public void applyProject() {
        System.out.println("Applying for project: " + appliedProject);
    }

    public void bookFlat() {
    }

    public boolean withdrawApplication() {
        return true;
    }

    public void submitEnquiry(String enquiry) {
    }

    public boolean deleteEnquiry(int enquiryID) {
        return true;
    }

    public void viewAllProjects() {
        if (this.eligibilityCriteria && this.getMaritalStatus() == MaritalStatus.Single) {
            System.out.println("Viewing all projects for Single applicants.");

            // project.Project projects = new project.Project();
            // String projectName = projects.getProjectName();
            // System.out.println("Project Name: " + projectName);

        } else if (this.eligibilityCriteria && this.getMaritalStatus() == MaritalStatus.Married) {
            System.out.println("Viewing all projects for Married applicants.");

            // project.Project projects = new project.Project();
            // String projectName = projects.getProjectName();
            // System.out.println("Project Name: " + projectName);

        } else {
            System.out.println("There are no eligible projects to view");
        }
    }

    @Override
    public UserRole getRole() {
        return UserRole.Applicant;
    }

    @Override
    public boolean checkEligibility(Project project) {
        // Updated rule: Single applicants must be at least 35, married applicants at
        // least 21
        if (this.getMaritalStatus().equals("Single") && this.getAge() >= 35) {
            this.eligibilityCriteria = true;
            return true;
        } else if (this.getMaritalStatus().equals("Married") && this.getAge() >= 21) {
            this.eligibilityCriteria = true;
            return true;
        }
        this.eligibilityCriteria = false;
        return false;
    }
}