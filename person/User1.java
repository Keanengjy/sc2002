package person;


enum MaritalStatus {
    SINGLE,
    MARRIED
}

interface User1 {
    String getName();
    String getNRIC();
    String getPassword();
    MaritalStatus getMaritalStatus();
    void login(String username, String password) throws AuthenticationException;
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