// push folder
//my applicant class

package person;

import java.util.*;

import project.FlatUnavailableException;
import project.HDBFlat;
import project.Project;
import project.Enquiry;


class ApplicationNotFoundException extends Exception {
    public ApplicationNotFoundException(String message) {
        super(message);
    }
}

class Applicant1 extends AbstractUser implements User{
    private String appliedProject;
    private String applicationStatus;
    private List<String> bookingStatus;
    private List<Project> viewableProjects;
    private List<Enquiry> enquiries;
    private HDBFlat bookedFlat;
    
    /**
     * Constructs a new Applicant with the specified parameters.
     */
    public Applicant(String name, String nric, String password, MaritalStatus maritalStatus, int age) {
        super(name, nric, password, maritalStatus, age);
        this.applicationStatus = "Not Applied";
        this.bookingStatus = new ArrayList<>();
        this.viewableProjects = new ArrayList<>();
        this.enquiries = new ArrayList<>();
    }

    @Override
    protected void calculateEligibilityCriteria() {
        StringBuilder criteria = new StringBuilder();
        criteria.append("Age: ").append(age).append("\n");
        criteria.append("Marital Status: ").append(maritalStatus).append("\n");
        
        if (maritalStatus == MaritalStatus.SINGLE) {
            if (age >= 35) {
                criteria.append("Eligible for Single Scheme\n");
            } else {
                criteria.append("Not eligible for Single Scheme (Age < 35)\n");
            }
        } else { // MARRIED
            criteria.append("Eligible for Family Scheme\n");
        }
        
        this.eligibilityCriteria = criteria.toString();
    }
    
    /**
     * Views available projects based on eligibility.
     * 
     * @return List of viewable projects
     */
    public List<Project> viewProjects() {
        // In a real implementation, this would fetch from a database or service
        return viewableProjects;
    }
    
    /**
     * Applies for a project.
     * 
     * @param project The project to apply for
     * @return True if application is successful, false otherwise
     */
    public boolean applyProject(Project project) throws IneligibleApplicantException {
        if (!project.isEligibleForApplicant(this)) {
            throw new IneligibleApplicantException("You are not eligible for this project.");
        }
        
        if (!project.isVisible()) {
            throw new IneligibleApplicantException("This project is not currently accepting applications.");
        }
        
        if (appliedProject != null && !appliedProject.isEmpty()) {
            throw new IneligibleApplicantException("You already have an active application.");
        }
        
        appliedProject = project.getProjectName();
        applicationStatus = "Pending";
        System.out.println("Application submitted successfully for " + project.getProjectName());
        return true;
    }
    
    /**
     * Books a flat.
     * 
     * @param flat The flat to book
     */
    public void bookFlat(HDBFlat flat) throws FlatUnavailableException {
        if (bookedFlat != null) {
            throw new FlatUnavailableException("You have already booked a flat.");
        }
        
        if (flat.isBooked()) {
            throw new FlatUnavailableException("This flat is already booked.");
        }
        
        if (!"Successful".equals(applicationStatus)) {
            throw new FlatUnavailableException("Your application must be successful before booking a flat.");
        }
        
        flat.setBooked(true);
        bookedFlat = flat;
        bookingStatus.add("Booked flat " + flat.getUnitNo() + " of type " + flat.getFlatType());
        System.out.println("Flat booked successfully!");
    }
    
    /**
     * Checks the status of an application.
     * 
     * @return True if application exists, false otherwise
     */
    public boolean checkApplicationStatus() {
        if (appliedProject == null || appliedProject.isEmpty()) {
            System.out.println("You have no active applications.");
            return false;
        }
        
        System.out.println("Application for project: " + appliedProject);
        System.out.println("Status: " + applicationStatus);
        return true;
    }
    
    /**
     * Adds an enquiry.
     * 
     * @param message The enquiry message
     * @return The ID of the new enquiry
     */
    public int addEnquiry(String message) {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Enquiry message cannot be empty");
        }
        
        int enquiryID = enquiries.size() + 1;
        Enquiry enquiry = new Enquiry(message, enquiryID, this.nric);
        enquiries.add(enquiry);
        System.out.println("Enquiry submitted successfully with ID: " + enquiryID);
        return enquiryID;
    }
    
    /**
     * Edits an existing enquiry.
     * 
     * @param enquiryID The ID of the enquiry to edit
     * @param newMessage The new message
     * @return True if edit is successful, false otherwise
     */
    public boolean editEnquiry(int enquiryID, String newMessage) throws EnquiryNotFoundException {
        for (Enquiry enquiry : enquiries) {
            if (enquiry.getEnquiryID() == enquiryID) {
                if (enquiry.getResponderID() != -1) {
                    throw new IllegalStateException("Cannot edit an enquiry that has been responded to");
                }
                
                // Create a new enquiry with the updated message but same ID
                Enquiry updatedEnquiry = new Enquiry(newMessage, enquiryID, this.nric);
                enquiries.set(enquiries.indexOf(enquiry), updatedEnquiry);
                System.out.println("Enquiry updated successfully");
                return true;
            }
        }
        
        throw new EnquiryNotFoundException("Enquiry with ID " + enquiryID + " not found");
    }
    
    /**
     * Deletes an enquiry.
     * 
     * @param enquiryID The ID of the enquiry to delete
     * @return True if deletion is successful, false otherwise
     */
    public boolean deleteEnquiry(int enquiryID) throws EnquiryNotFoundException {
        for (Enquiry enquiry : enquiries) {
            if (enquiry.getEnquiryID() == enquiryID) {
                enquiries.remove(enquiry);
                System.out.println("Enquiry deleted successfully");
                return true;
            }
        }
        
        throw new EnquiryNotFoundException("Enquiry with ID " + enquiryID + " not found");
    }
    
    // Getters and setters
    public String getAppliedProject() {
        return appliedProject;
    }
    
    public void setApplicationStatus(String status) {
        this.applicationStatus = status;
    }
    
    public String getApplicationStatus() {
        return applicationStatus;
    }
    
    public List<String> getBookingStatus() {
        return new ArrayList<>(bookingStatus);
    }
    
    public void addViewableProject(Project project) {
        if (project.isVisible() && project.isEligibleForApplicant(this)) {
            viewableProjects.add(project);
        }
    }
    
    public List<Enquiry> getEnquiries() {
        return new ArrayList<>(enquiries);
    }
    
    public HDBFlat getBookedFlat() {
        return bookedFlat;
    }

}