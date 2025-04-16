package person;

import project.EnquiryHandler;
import project.Project;
import java.util.Scanner;

public class HDBOfficer extends AbstractUser implements EnquiryHandler {

    private String projects;
    private String registeredProjects;
    private String registeredProjectStatus;

    public HDBOfficer(String name, String nric, String password, MaritalStatus maritalStatus, int age) {
        super(name, nric, password, maritalStatus, age);
        this.projects = null;
        this.registeredProjects = null;
        this.registeredProjectStatus = null;
    }

    public void registerProject(String project) {
        this.registeredProjects = project;
        this.registeredProjectStatus = "Registered";
        System.out.println("Project registered: " + project);
    }

    public void getProjectDetails() {
        System.out.println("Registered Project: " + registeredProjects);
        System.out.println("Project Status: " + registeredProjectStatus);
    }

    @Override
    public void calculateEligibilityCriteria() {
        // Logic to calculate eligibility criteria
        System.out.println("Calculating eligibility criteria for HDBOfficer.");
    }

    @Override
    public void handleEnquiry(int enquiryID) {
        System.out.println("Handling enquiry with ID: " + enquiryID);
        // Logic to handle the enquiry
    }

    @Override
    public void replyEnquiry(String reply) {
        System.out.println("Replying to enquiry: " + reply);
        Scanner scanner = new Scanner(System.in);
        String response = scanner.nextLine();
        System.out.println("Response sent: " + response);
    }

    public void updateFlatCount(Project project) {
        System.out.println("Updating flat count for project: " + project.getProjectName());
        // Logic to update flat count
    }
}
