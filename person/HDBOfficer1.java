package person;

import java.util.HashMap;
import java.util.Map;
import project.*;


import project.Project;

class IneligibleApplicantException extends Exception {
    public IneligibleApplicantException(String message) {
        super(message);
    }
}

public class HDBOfficer1 extends AbstractUser implements EnquiryHandler{
    private String projects;
    private String registeredProjects;
    private String officerStatus;
    private String eligibilityCriteria;
    private Map<Integer, Enquiry> assignedEnquiries;
    
    /**
     * Constructs a new HDBOfficer with the specified parameters.
     */
    public HDBOfficer1(String name, String nric, String password, MaritalStatus maritalStatus, int age) {
        super(name, nric, password, maritalStatus, age);
        this.projects = "";
        this.registeredProjects = "";
        this.officerStatus = "Pending";
        this.assignedEnquiries = new HashMap<>();
    }

    @Override
    protected void calculateEligibilityCriteria() {
        this.eligibilityCriteria = "HDB Officer: Eligible to register for projects";
    }
    
    /**
     * Registers for a project.
     */
    public boolean registerForProject(Project project) throws IneligibleApplicantException {
        // Check if already registered for a project in the same period
        if (!registeredProjects.isEmpty()) {
            throw new IneligibleApplicantException("You are already registered for a project.");
        }
        
        // Check if there are available slots
        if (project.getAvailableOfficerSlots() <= 0) {
            throw new IneligibleApplicantException("No available officer slots for this project.");
        }
        
        project.decrementOfficerSlots();
        project.setHdbOfficer(this);
        this.registeredProjects = project.getProjectName();
        this.officerStatus = "Registered";
        System.out.println("Successfully registered as officer for project: " + project.getProjectName());
        return true;
    }
    
    /**
     * Gets the details of a project.
     */
    public String getProjectDetails(Project project) {
        return project.getProjectDetails();
    }
    
    /**
     * Responds to an enquiry.
     */
    public void replyEnquiry(int enquiryID, String response) throws EnquiryNotFoundException {
        if (!assignedEnquiries.containsKey(enquiryID)) {
            throw new EnquiryNotFoundException("Enquiry with ID " + enquiryID + " not found or not assigned to you");
        }
        
        Enquiry enquiry = assignedEnquiries.get(enquiryID);
        enquiry.setResponse(response, Integer.parseInt(this.nric.replaceAll("[^0-9]", "")));
        System.out.println("Response submitted successfully");
    }
    
    /**
     * Handles enquiries for a project.
     */
    public void replyEnquiry(String response) throws EnquiryNotFoundException {
        throw new UnsupportedOperationException("Not implemented");
    }
    
    /**
     * Handles an enquiry.
     */
    @Override
    public void handleEnquiry(int enquiryID) throws EnquiryNotFoundException {
        // This would typically fetch the enquiry from a database or service
        // For now, we'll simulate this behavior
        throw new EnquiryNotFoundException("Enquiry handling not implemented");
    }
    
    /**
     * Updates the flat count for a project.
     */
    public void updateFlatCount(Project project, int count) {
        // In a real implementation, this would update the project's flat count in the database
        System.out.println("Updated flat count for project " + project.getProjectName() + " to " + count);
    }
    
    // Getters and setters
    public String getProjects() {
        return projects;
    }
    
    public String getRegisteredProjects() {
        return registeredProjects;
    }
    
    public String getOfficerStatus() {
        return officerStatus;
    }
    
    public void setOfficerStatus(String status) {
        this.officerStatus = status;
    }
    
    @Override
    public String getEligibilityCriteria() {
        return eligibilityCriteria;
    }
    
    public void assignEnquiry(Enquiry enquiry) {
        assignedEnquiries.put(enquiry.getEnquiryID(), enquiry);
    }
    
    public Map<Integer, Enquiry> getAssignedEnquiries() {
        return new HashMap<>(assignedEnquiries);
    }
}
