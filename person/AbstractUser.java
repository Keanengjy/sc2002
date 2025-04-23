package person;

import project.Project;

public abstract class AbstractUser implements User{
    protected String name;
    protected String NRIC;
    protected int age;
    protected MaritalStatus maritalStatus;
    protected String password;
    protected boolean eligibilityCriteria;
    protected boolean loggedIn;

    /**
     * Constructs a new AbstractUser with the specified parameters.
     */
    public AbstractUser(String name, String NRIC, int age, MaritalStatus maritalStatus, String password, boolean eligibilityCriteria, boolean loggedIn) {
        this.name = name;
        this.NRIC = NRIC;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.password = password;
        this.eligibilityCriteria = eligibilityCriteria;
        this.loggedIn = false;
    }


    public void changePassword(String oldPassword, String newPassword) {
        try {
            if (!this.password.equals(oldPassword)) {
                throw new InvalidPasswordException("Current password is incorrect");
            }
            if (newPassword == null || newPassword.isEmpty()) {
                throw new InvalidPasswordException("New password cannot be empty");
            }
            this.password = newPassword;
            System.out.println("Password changed successfully. Please re-login.");
            logout();
        } catch (InvalidPasswordException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    
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

    public boolean checkEligibility(Project project) {
        // Base eligibility check - can be overridden by subclasses
        return this.eligibilityCriteria;
    }

    public abstract UserRole getRole();


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getNRIC() {
        return NRIC;
    }

    public void setNRIC(String userID) {
        this.NRIC = userID;
    }

    public String getPassword() {
        return password;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }
    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public Boolean isEligible() {
        return eligibilityCriteria;
    }
    public void setEligibilityCriteria(Boolean eligibilityCriteria) {
        this.eligibilityCriteria = eligibilityCriteria;
    }


    public boolean loggedIn() {
        return loggedIn;
    }
}

class InvalidPasswordException extends Exception {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
