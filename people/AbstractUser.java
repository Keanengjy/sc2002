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

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; } // Add setter for age   
    public String getNRIC() { return NRIC; } // Add getter for NRIC
    public void setNRIC(String NRIC) { this.NRIC = NRIC; } // Add setter for NRIC
    public MaritalStatus getMaritalStatus() { return maritalStatus; } // Add getter for maritalStatus
    public void setMaritalStatus(MaritalStatus maritalStatus) { this.maritalStatus = maritalStatus; } // Add setter for maritalStatus
    public String getPassword() { return password; } // Add getter for password
    public void setPassword(String password) { this.password = password; } // Add setter for password
    public boolean isLoggedIn() { return loggedIn; } // Add getter for loggedIn 

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
    public abstract boolean checkEligibility(Project project);

    public UserRole getRole() {
        return UserRole.Applicant; // Default role, can be overridden in subclasses
    }
}