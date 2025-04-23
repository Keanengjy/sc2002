package person;

import project.Project;
import project.UserRole;

interface User {

    boolean login(String username, String password);
    void logout();
    void changePassword(String newPassword);
    boolean checkEligibility(Project project);
    UserRole getRole();

}
