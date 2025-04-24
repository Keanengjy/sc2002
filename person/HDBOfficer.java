package person;

import project.ApplicationStatus;
import project.Visibility;
import person.MaritalStatus;
import project.UserRole;
import project.FlatType;

import project.HDBFlat;
import project.Enquiry;
import project.Project;

import person.Applicant;
import person.HDBManager;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class HDBOfficer extends AbstractUser {
    private String projects;
    private Project registeredProject;
    private ApplicationStatus registeredProjectStatus;

    public HDBOfficer(String name, String NRIC, int age, String maritalStatus, String password, String projects) {
        super(name, NRIC, age, maritalStatus, password);
        this.projects = projects;
        this.registeredProjectStatus = ApplicationStatus.Pending;
    }

    public void setRegisteredProjectStatus(ApplicationStatus status) {
        this.registeredProjectStatus = status;
    }

    public ApplicationStatus getRegisteredProjectStatus() {
        return this.registeredProjectStatus;
    }

    public void setRegisteredProject(Project project) {
        this.registeredProject = project;
    }

    public Project getRegisteredProject() {
        return registeredProject;
    }

    public void registerProject(Project p) {

        this.registeredProjectStatus = ApplicationStatus.Pending;
        p.getManager().applicationDecision(p.getProjectName(), this);
        System.out.println(name + " registering for project: " + p.getProjectName() + ". Awaiting approval.");
    }

    public void getProjectDetails() {
        if (registeredProject != null) {
            // Accessing Project details
            System.out.println("=== Project Details ===");
            System.out.println("Project Name: " + registeredProject.getProjectName());
            System.out.println("Neighborhood: " + registeredProject.getNeighborhood());
            System.out.println("Application Open: " + registeredProject.getApplicationOpeningDate());
            System.out.println("Application Close: " + registeredProject.getApplicationClosingDate());
            System.out.println("Flats Available: " + registeredProject.getFlats());
            System.out.println("Visibility: " + (registeredProject.isVisible() ? "Visible" : "Not Visible"));
            System.out.println("Status: " + registeredProjectStatus);  // Assuming this field is set when the officer is approved
        } else {
            System.out.println("No project registered.");
        }
    }

    public void replyEnquiry(Enquiry enquiry) {
        // Assuming this method is supposed to reply to an enquiry
        enquiry.getMessage();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your reply: ");
        String response = scanner.nextLine();

        enquiry.setResponse(response);
        System.out.println("Reply sent: " + response);

        scanner.close();
    }

    public boolean updateFlatCount(Project project, FlatType flatType) {
        return HDBFlat.decrementFlat(project, flatType); // Update the flat count in the Project class
    }

    public String[] getApplication(String nric, Map<String, Applicant> applicants) {
        // Check if the NRIC exists in the map
        Applicant a = applicants.get(nric);
        
        // If the applicant with the given NRIC is found
        if (a != null) {
            String project = a.getAppliedProject();   // "" if never applied
            HDBFlat flat = a.getSelectedFlat();       // may be null
    
            // Return project and flat details
            return new String[]{
                    project,
                    (flat != null) ? flat.toString() : "null"  // flat.toString() or "null" if no flat
            };
        }
        return null;
    }

    public boolean updateInfo(String nric, Map<String, Applicant> applicants, Project project, FlatType flat) {
        // Check if the NRIC exists in the map
        Applicant a = applicants.get(nric);
        if (a == null) {
            return false;
        }
        if (a.getApplicationStatus() != ApplicationStatus.Successful) {
            return false;  // only book if they were marked successful
        }

        a.setApplicationStatus(ApplicationStatus.Booked);
        HDBFlat chosen = new HDBFlat(flat, 0.0, project.getNeighborhood());
        a.setSelectedFlat(chosen);

        return true; 
    }

public String generateReceipt(Applicant a, Project project) {

    // --- safety checks -------------------------------------------------
    if (a == null)      throw new IllegalArgumentException("Applicant is null");
    if (project == null) throw new IllegalArgumentException("Project is null");
    if (a.getSelectedFlat() == null)
        throw new IllegalStateException("Applicant has not selected a flat yet");
    // -------------------------------------------------------------------

    HDBFlat   flat      = a.getSelectedFlat();
    FlatType  type      = flat.getFlatType();

    StringBuilder sb = new StringBuilder();
    sb.append("--------------------------------------------------\n");
    sb.append("            H D B   B O O K I N G   R E C E I P T\n");
    sb.append("--------------------------------------------------\n");
    sb.append(String.format("Applicant Name  : %s%n", a.getName()));
    sb.append(String.format("NRIC            : %s%n", a.getNRIC()));
    sb.append(String.format("Age             : %d%n", a.getAge()));
    sb.append(String.format("Marital Status  : %s%n", a.getMaritalStatus()));
    sb.append("--------------------------------------------------\n");
    sb.append(String.format("Project Name    : %s%n", project.getProjectName()));
    sb.append(String.format("Neighbourhood   : %s%n", project.getNeighborhood()));
    sb.append(String.format("Project ID      : %s%n", project.getProjectID()));
    sb.append("--------------------------------------------------\n");
    sb.append(String.format("Flat Type       : %s%n", type));
    sb.append(String.format("Selling Price   : $%.2f%n", flat.getSellingPrice()));
    sb.append(String.format("Location        : %s%n", flat.getLocation()));
    sb.append(String.format("Booking Status  : %s%n",
                 a.getApplicationStatus()));           // e.g.  Booked
    sb.append("--------------------------------------------------\n");
    sb.append("Thank you for choosing HDB. Please keep this receipt.\n");

    return sb.toString();
}

    public void viewStatus() {
        System.out.println("Status: " + registeredProjectStatus);
    }

    @Override
    public UserRole getRole() {
        return UserRole.HDBOfficer;
    }

    @Override
    public boolean checkEligibility(Project project) {
        // Example rule: Officers are eligible if they are logged in and have any project assigned

        return this.loggedIn && registeredProject != null && registeredProject.isEmpty();
    }

    public boolean isEmpty(){
        return registeredProject == null;
    }
}
