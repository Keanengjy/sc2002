package person;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fileops.ObjectCreate;
import person.MaritalStatus;
import project.ApplicationStatus;
import project.UserRole;

import project.Enquiry;
import project.FlatType;
import project.HDBFlat;
import project.Project;
import person.HDBOfficer;

public class Applicant extends AbstractUser {
    private Enquiry enquiry;
    private String appliedProject;
    private ApplicationStatus applicationStatus;
    private HDBFlat selectedFlat;

    public Applicant(String name, String NRIC, int age, String maritalStatus, 
                    String password, boolean eligibilityCriteria, boolean loggedIn) {
        super(name, NRIC, age, maritalStatus, password);
        this.appliedProject = null;
        this.applicationStatus = null;
        this.selectedFlat = null;
    }
    @Override
    public void changePassword(String newPassword) {
        // Assuming `password` is a protected or private field in AbstractUser or User.
        this.password = newPassword;
    }



    public UserRole getRole() {
        return UserRole.Applicant;
    }

    public boolean checkEligibility(Project project) {
        return eligibilityCriteria;
    }

    public String getAppliedProject() {return appliedProject;}
    public void setAppliedProject(String appliedProject) {this.appliedProject = appliedProject;}
    public ApplicationStatus getApplicationStatus() {return applicationStatus;}
    public void setApplicationStatus(ApplicationStatus applicationStatus) {this.applicationStatus = applicationStatus;}
    public String getSelectedFlat() {return selectedFlat;}
    public void setSelectedFlat(String selectedFlat) {this.selectedFlat = selectedFlat;}  // updated to accept String

    public void viewProject() {
        if (!loggedIn) {
            System.out.println("Please log in to view projects.");
            return;
        }
        
        System.out.println("Viewing available projects...");
        // display available projects 
    }

    
    public String editEnquiry() {
        if (!loggedIn) {
            return "You must be logged in to edit an enquiry";
        }
        
        // In a real implementation, we would need a way to identify which enquiry to edit
        // This could be through user input, a selection in the UI, or a currently active enquiry
        
        // Simulate getting the enquiry to edit
        Enquiry enquiryToEdit = getCurrentEnquiry(); 
        
        if (enquiryToEdit == null) {
            return "No enquiry selected for editing";
        }
        
        // Check if the enquiry belongs to this applicant
        if (enquiryToEdit.getSenderID() != Integer.parseInt(getNRIC())) {
            return "You can only edit your own enquiries";
        }
        
        // In a real implementation, you would update the enquiry message here
        // For example: enquiryToEdit.setMessage("New message content");
        
        // Simulate saving the updated enquiry
        boolean updateSuccess = saveEnquiryChanges(enquiryToEdit);
        
        if (updateSuccess) {
            return "Enquiry successfully edited";
        } else {
            return "Failed to edit enquiry";
        }
    }
/*
    public String submitEnquiry() {
        // Implementation to submit an enquiry
        if (!isLoggedIn()) {
            return "You must be logged in to submit an enquiry";
        }
        
        // Validate input
        if (enquiry.getMessage() == null || enquiry.getMessage().trim().isEmpty()) {
            return "Enquiry message cannot be empty";
        }
        
        // Check if the project exists
        boolean projectExists = checkProjectExists(enquiry.getProjectID());
        if (!projectExists) {
            return "Project with ID " + enquiry.getProjectID() + " not found";
        }
        
        // Generate a new enquiry ID (in a real system, this would be done by the database)
        int newEnquiryID = generateNewEnquiryID();
        
        // Assign the new enquiry ID
        enquiry.setEnquiryID(newEnquiryID);
        enquiry.setSenderID(getNRIC()); // Assuming getNRIC() retrieves applicant's ID
        
        // Save the enquiry (shouldn't be a database per your requirements, maybe a file instead?)
        boolean saveSuccess = saveEnquiry(enquiry);
        
        if (saveSuccess) {
            System.out.println("Enquiry submitted successfully with ID: " + newEnquiryID);
            return "Your enquiry has been submitted. Your enquiry ID is: " + newEnquiryID;
        } else {
            return "Failed to submit enquiry. Please try again.";
        }
    }
*/

    public String submitEnquiry() {
        if (!loggedIn) {
            return "You must be logged in to submit an enquiry";
        }
        
        // In a real implementation, you would collect enquiry details from user input
        // Here we simulate creating a new enquiry
        
        // Create a new Map for the message (as per the class diagram)
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("subject", "Enquiry about housing project");
        messageMap.put("body", "I would like more information about the application process.");
        
        // Create a new enquiry
        Enquiry newEnquiry = new Enquiry("Requesting for flat availability", 1001, 1002);
        newEnquiry.setMessage(messageMap);
        newEnquiry.setEnquiryID(generateNewEnquiryID()); // Generate a unique ID
        newEnquiry.setSenderID(Integer.parseInt(getNRIC())); // Set sender ID to applicant's ID
        // Leave response and responderID as null/default since it's a new enquiry
        
        // Simulate saving the enquiry
        boolean saveSuccess = saveNewEnquiry(newEnquiry);
        
        if (saveSuccess) {
            return "Enquiry submitted successfully with ID: " + newEnquiry.getEnquiryID();
        } else {
            return "Failed to submit enquiry. Please try again.";
        }
    }
    
public boolean deleteEnquiry(int enquiryID) {
    // Implementation to delete an enquiry with the given ID
    
    // Check if user is logged in
    if (!loggedIn) {
        System.out.println("You must be logged in to delete an enquiry");
        return false;
    }
    
    // Find the enquiry with the given ID
    Enquiry enquiryToDelete = findEnquiry(enquiryID);
    
    if (enquiryToDelete == null) {
        System.out.println("Enquiry with ID " + enquiryID + " not found");
        return false;
    }
    
    // Check if the enquiry belongs to this applicant
    if (enquiryToDelete.getSenderID() != Integer.parseInt(getNRIC())) {
        System.out.println("You can only delete your own enquiries");
        return false;
    }
    
    // Check if the enquiry has already been responded to
    if (enquiryToDelete.getResponse() != null && !enquiryToDelete.getResponse().isEmpty()) {
        System.out.println("Cannot delete an enquiry that has been responded to");
        return false;
    }
    
    // Simulate deleting the enquiry
    boolean deleteSuccess = removeEnquiry(enquiryID);
    
    if (deleteSuccess) {
        System.out.println("Enquiry with ID " + enquiryID + " deleted successfully");
        return true;
    } else {
        System.out.println("Failed to delete enquiry");
        return false;
    }
}
    
    public void viewAllProjects() {
        // Implementation to view all available projects
    
        // Logic to retrieve and display all projects would go here
        String filePath = "ProjectList.csv"; // Ensure the file path is correct
        System.out.println("Viewing all available projects:\n");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) { // Skipping header row
                    isHeader = false;
                    continue;
                }
                String[] projectData = line.split("\t"); // Assuming tab-separated values
                System.out.println("Project Name: " + projectData[0]);
                System.out.println("Neighborhood: " + projectData[1]);
                System.out.println("Type 1: " + projectData[2] + " (" + projectData[3] + " units, $" + projectData[4] + ")");
                System.out.println("Type 2: " + projectData[5] + " (" + projectData[6] + " units, $" + projectData[7] + ")");
                System.out.println("Application Period: " + projectData[8] + " to " + projectData[9]);
                System.out.println("Manager: " + projectData[10]);
                System.out.println("Officer Slot: " + projectData[11]);
                System.out.println("Officer(s): " + projectData[12]);
                System.out.println("-----------------------------");
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
    
    // This method is not in the diagram but might be useful for implementation
    @Override
    public String toString() {
        return "Applicant{" +
                "name='" + getName() + '\'' +
                ", NRIC='" + getNRIC() + '\'' +
                ", appliedProject='" + appliedProject + '\'' +
                ", applicationStatus='" + applicationStatus + '\'' +
                ", selectedFlat=" + selectedFlat +
                '}';
    }

    private Enquiry getCurrentEnquiry() {
        // This would retrieve the currently selected enquiry from the application state
        // For now, we return a mock enquiry
        int enquiryIDs = 12345;
        int senderID = 1001;
        String response = "We will get back to you shortly.";
        int responderID = 2001;
 
        Map<String, String> message = new HashMap<>();
        message.put("subject", "Query about project");
        message.put("body", "When does the application period open?");
        Enquiry mockEnquiry = new Enquiry(message,enquiryIDs,senderID,response,responderID);

        mockEnquiry.setMessage(message);
        mockEnquiry.setEnquiryID(12345);
        mockEnquiry.setSenderID(Integer.parseInt(getNRIC()));
        return mockEnquiry;
    }
    
    /**
     * Save changes to an existing enquiry
     */
    private boolean saveEnquiryChanges(Enquiry enquiry) {
        // This would save the updated enquiry to persistent storage
        // For now, we just simulate success
        System.out.println("Saving changes to enquiry ID: " + enquiry.getEnquiryID());
        return true;
    }
    
    /**
     * Generate a new unique enquiry ID
     */
    private int generateNewEnquiryID() {
        // This would generate a unique ID, possibly from a database sequence
        // For now, we simulate with a random number
        return 10000 + (int)(Math.random() * 90000);
    }
    
    /**
     * Save a new enquiry
     */
    private boolean saveNewEnquiry(Enquiry enquiry) {
        // This would save the new enquiry to persistent storage
        // For now, we just simulate success
        System.out.println("Saving new enquiry with ID: " + enquiry.getEnquiryID());
        return true;
    }
    
    /**
     * Find an enquiry by ID
     */
    private Enquiry findEnquiry(int enquiryID) {
        // This would search for the enquiry in persistent storage
        // For now, we return a mock enquiry if the ID matches our test case
        if (enquiryID == 12345) {
            
            Map<String, String> message = new HashMap<>();
            int enquiryIDs = 12345;
            int senderID = 1001;
            String response = "We will get back to you shortly.";
            int responderID = 2001;
            Enquiry mockEnquiry = new Enquiry(message,enquiryIDs,senderID,response,responderID);

            message.put("subject", "Query about project");
            message.put("body", "When does the application period open?");
            mockEnquiry.setMessage(message);
            mockEnquiry.setEnquiryID(enquiryID);
            mockEnquiry.setSenderID(Integer.parseInt(getNRIC()));
            return mockEnquiry;
        }
        return null;
    }
    
    /**
     * Remove an enquiry from the system
     */
    private boolean removeEnquiry(int enquiryID) {
        // This would remove the enquiry from persistent storage
        // For now, we just simulate success
        System.out.println("Removing enquiry with ID: " + enquiryID);
        return true;
    }
}