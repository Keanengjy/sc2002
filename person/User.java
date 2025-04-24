package person;
import project.Project;

public interface User {
    void login(String username, String password);
    void logout();
    void changePassword(String newPassword);
    boolean checkEligibility(Project project)throws AuthenticationException; // Assume Project class is defined elsewhere
    UserRole getRole();
}

class AuthenticationException extends Exception {
    public AuthenticationException(String message) {
        super(message);
    }
}
class InvalidNRICFormatException extends Exception {
    public InvalidNRICFormatException(String message) {
        super(message);
    }
}