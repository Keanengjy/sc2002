package person;

import java.util.Scanner;

class HDBOfficer extends Applicant implements User {
    private String name;
    private String NRIC;
    private String password;
    private MaritalStatus maritalStatus;
    private enum MaritalStatus { MARRIED, SINGLE};
    private String projects;
    private String registeredProject;
    private String registeredProjectStatus;
    private String eligibilityCriteria;

    Scanner scanner = new Scanner(System.in);

    public HDBOfficer(String name, String NRIC, String password, MaritalStatus maritalStatus) {
        this.name = name;
        this.NRIC = NRIC;
        this.password = password;
        this.maritalStatus = maritalStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void registerProject(String project) {
        this.registeredProject = project;
    }

    public void getProjectDetails(){
        System.out.println("Project Name: " + registeredProject);
        System.out.println("Project Status: " + registeredProjectStatus);
    }

    public void replyEnquiry(String reply) {
        System.out.println("Replying to enquiry: " + reply);
        String response = scanner.nextLine();
    }
    
}
