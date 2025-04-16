package person;

abstract public class AbstractUser implements User{
    protected String name;
    protected String nric;
    protected String password;
    protected MaritalStatus maritalStatus;
    protected int age;
    protected String eligibilityCriteria;
    protected boolean isLoggedIn;

    /**
     * Constructs a new AbstractUser with the specified parameters.
     */
    public AbstractUser(String name, String nric, String password, MaritalStatus maritalStatus, int age) {
        this.name = name;
        this.nric = nric;
        this.password = password;
        this.maritalStatus = maritalStatus;
        this.age = age;
        this.isLoggedIn = false;
        calculateEligibilityCriteria();
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
