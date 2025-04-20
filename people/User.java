package people;

enum UserRole {
    Applicant,
    HDBOfficer,
    HDBManager
}

interface User {

    boolean login(String username, String password);
    void logout();
    void changePassword(String newPassword);
    boolean checkEligibility();
    UserRole getRole();

}
