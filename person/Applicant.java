package person;

public class Applicant implements User {
    private String name;
    private String NRIC;
    private String password;
    private MaritalStatus maritalStatus;
    private enum MaritalStatus { MARRIED, SINGLE};
    private String appliedProject;
    private String applicationStatus;
    private String eligibilityCriteria;

    public Applicant(String name, String NRIC, String password, MaritalStatus maritalStatus) {
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

    public void viewProject() {
        System.out.println("Applied Project: " + appliedProject);
        System.out.println("Application Status: " + applicationStatus);
    }

    public void applyProject(String project) {
        this.appliedProject = project;
    }
    
    public void bookFlat(){
        
    }
 
