package person;

abstract public class AbstractUser implements User{
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

    /**
     * Calculates the eligibility criteria for the user.
     */
    protected abstract void calculateEligibilityCriteria();

    /**
     * Changes the user's password.
     */
    public void changePassword(String oldPassword, String newPassword) throws InvalidPasswordException {
        if (!this.password.equals(oldPassword)) {
            throw new InvalidPasswordException("Current password is incorrect");
        }
        if (newPassword == null || newPassword.isEmpty()) {
            throw new InvalidPasswordException("New password cannot be empty");
        }
        this.password = newPassword;
        System.out.println("Password changed successfully. Please re-login.");
        logout();
    }

    /**
     * Logs the user into the system.
     */
    public void login(String username, String password) throws AuthenticationException {
        if (!this.nric.equals(username)) {
            throw new AuthenticationException("Invalid NRIC");
        }
        if (!this.password.equals(password)) {
            throw new AuthenticationException("Incorrect password");
        }
        this.isLoggedIn = true;
        System.out.println("Login successful. Welcome, " + this.name + "!");
    }

    /**
     * Logs the user out of the system.
     */
    public void logout() {
        this.isLoggedIn = false;
        System.out.println("You have been logged out successfully.");
    }

    // Getters as per User interface

    public String getName() {
        return name;
    }

    public String getNRIC() {
        return nric;
    }

    public String getPassword() {
        return password;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public int getAge() {
        return age;
    }

    public String getEligibilityCriteria() {
        return eligibilityCriteria;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
}

class InvalidPasswordException extends Exception {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
