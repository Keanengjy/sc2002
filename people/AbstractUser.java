package people;

import project.Project;

enum MaritalStatus {
    Single,
    Married
};

enum UserRole {
    Applicant,
    HDBOfficer,
    HDBManager
}

abstract class AbstractUser implements User {
    protected String name;
    protected String NRIC;
    protected int age;
    protected MaritalStatus maritalStatus;
    protected String password;
    protected boolean eligibilityCriteria; // Indicates if the user has access permissions
    protected boolean loggedIn;

    public String getNRIC() { return NRIC; }
    public void setNRIC(String NRIC) { this.NRIC = NRIC; }
    public void changePassword(String newPassword) { this.password = newPassword; }
    public boolean login(String username, String password) {
        if (this.NRIC.equals(username) && this.password.equals(password)) {
            this.loggedIn = true;
            return true;
        }
        return false;
    }
    public void logout() { this.loggedIn = false; }

    // This method acts as an access control check based on eligibility criteria
    public boolean checkEligibility(Project project) {
        return this.eligibilityCriteria;
    }

    public abstract UserRole getRole();
}