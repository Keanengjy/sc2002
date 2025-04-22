package people;

import project.HDBFlat;
import project.Project;

enum UserRole {
    Applicant,
    HDBOfficer,
    HDBManager
}

enum MaritalStatus {
    Single,
    Married
}

enum FlatType {
    TwoRoom,
    ThreeRoom
}

public class Applicant extends AbstractUser{
    private String appliedProject;
    private String applicationStatus;
    private HDBFlat selectedFlat;
    private MaritalStatus maritalStatus; // Add maritalStatus field

    public Applicant(String appliedProject, String applicationStatus, HDBFlat selectedFlat, MaritalStatus maritalStatus) {
        this.appliedProject = appliedProject;
        this.applicationStatus = applicationStatus;
        this.selectedFlat = selectedFlat;
        this.maritalStatus = maritalStatus; // Initialize maritalStatus
    }

    public MaritalStatus getMaritalStatus() { // Add getter for maritalStatus
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) { // Add setter for maritalStatus
        this.maritalStatus = maritalStatus;
    }

    public void viewProject() {
        System.out.println("Applied Project: " + appliedProject);

    }
    public void applyProject() {
        System.out.println("Applying for project: " + appliedProject);
    }
    public void bookFlat() {}
    public boolean withdrawApplication() { return true; }
    public void submitEnquiry(String enquiry) {}
    public boolean deleteEnquiry(int enquiryID) { return true; }

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
        // Updated rule: Single applicants must be at least 35, married applicants at least 21
        if (this.getMaritalStatus() == MaritalStatus.Single && this.getAge() >= 35) {
            this.eligibilityCriteria = true;
            return true;
        } else if (this.getMaritalStatus() == MaritalStatus.Married && this.getAge() >= 21) {
            this.eligibilityCriteria = true;
            return true;
        }
        this.eligibilityCriteria = false;
        return false;
    }
}