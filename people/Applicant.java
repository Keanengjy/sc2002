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

public class Applicant extends AbstractUser{
    private String appliedProject;
    private String applicationStatus;
    private HDBFlat selectedFlat;
    private MaritalStatus maritalStatus; // Add maritalStatus field

    public MaritalStatus getMaritalStatus() { // Add getter for maritalStatus
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) { // Add setter for maritalStatus
        this.maritalStatus = maritalStatus;
    }

    public void viewProject() {}
    public void applyProject() {}
    public void bookFlat() {}
    public boolean withdrawApplication() { return true; }
    public void submitEnquiry(String enquiry) {}
    public boolean deleteEnquiry(int enquiryID) { return true; }
    public void viewAllProjects() {}

    @Override
    public UserRole getRole() {
        return UserRole.Applicant;
    }

    @Override
    public boolean checkEligibility(Project project) {
        // Updated rule: Single applicants must be at least 35, married applicants at least 21
        if (this.getMaritalStatus() == MaritalStatus.Single && this.getAge() >= 35) {
            return true;
        } else if (this.getMaritalStatus() == MaritalStatus.Married && this.getAge() >= 21) {
            return true;
        }
        return false;
    }
}