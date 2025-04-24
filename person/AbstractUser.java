package person;
import project.Project;
import person.MaritalStatus;
import project.Project;
import project.UserRole;

import person.MaritalStatus;
import project.Project;
import project.UserRole;

abstract class AbstractUser implements User {
    protected String name;
    protected String NRIC;
    protected int age;
    protected String maritalStatus;
    protected String password;
    protected boolean eligibilityCriteria; // Indicates if the user has access permissions
    protected boolean loggedIn;

    public AbstractUser(String name, String NRIC, int age, String maritalStatus, String password) {
        this.name = name;
        this.NRIC = NRIC;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.password = password;
        this.eligibilityCriteria = false; // Default to no access permissions
        this.loggedIn = false; // Default to not logged in
    }

    public String getName() { return name; } // Add getter for name
    public void setName(String name) { this.name = name; } // Add setter for name
    public int getAge() { return age; } // Add getter for age
    public void setAge(int age) { this.age = age; } // Add setter for age   
    public String getNRIC() { return NRIC; } // Add getter for NRIC
    public void setNRIC(String NRIC) { this.NRIC = NRIC; } // Add setter for NRIC
    public String getMaritalStatus() { return maritalStatus; } // Update return type for getter
    public void setMaritalStatus(String maritalStatus) { this.maritalStatus = maritalStatus; } // Update setter for maritalStatus
    public String getPassword() { return password; } // Add getter for password
    public void setPassword(String password) { this.password = password; } // Add setter for password
    public boolean isLoggedIn() { return loggedIn; } // Add getter for loggedIn 

    public void changePassword(String newPassword) { this.password = newPassword; }

    public void login(String username, String password) {
        if (this.NRIC.equals(username) && this.password.equals(password)) {
            this.loggedIn = true;
            System.out.println("Login successful for user: " + this.name);
        } else {
            System.out.println("Login failed. Invalid credentials.");
        }

    }
    public void logout() {
        if (this.loggedIn) {
            this.loggedIn = false;
            System.out.println("User logged out successfully.");
        } else {
            System.out.println("No user is currently logged in.");
        }
    }


    // This method acts as an access control check based on eligibility criteria
    public abstract boolean checkEligibility(Project project);

    public UserRole getRole() {
        return UserRole.Applicant; // Default role, can be overridden in subclasses
    }

    class InvalidPasswordException extends Exception {
        public InvalidPasswordException(String message) {
            super(message);
        }
    }
}