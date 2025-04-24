package person;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import fileops.ObjectCreate;
import person.MaritalStatus;
import project.ApplicationStatus;
import project.UserRole;

import project.Enquiry;
import project.FlatType;
import project.HDBFlat;
import project.Project;
import person.HDBOfficer;
import person.Application;

public class Applicant extends AbstractUser {
    private Enquiry enquiry;
    private String appliedProject;
    private ApplicationStatus applicationStatus;
    private String selectedFlat;

    public Applicant(String name, String NRIC, int age, String maritalStatus, 
                    String password) {
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

        // 1. check if the applicant ever applied
        if (appliedProject == null) {
            System.out.println("You have not applied for any BTO project yet.");
            return;
        }
    
        // 2. show core application info
        System.out.println("\n--- Your Application Details ---");
        System.out.println("Project Name   : " + appliedProject);
        System.out.println("Status         : " + applicationStatus);
    
        // 3. if an Application object is linked, show more
        if (applicationStatus != null) {
            Application application = Application.findApplicationForApplicant(this);
            Project p = application.getProject();
            System.out.println("Neighbourhood  : " + p.getNeighborhood());
            if (applicationStatus == ApplicationStatus.Booked) {
                HDBFlat flat = application.getSelectedFlat();
                System.out.println("Flat Type      : " + flat.getFlatType());
            }
        }
    }
    
/*  In class Applicant  */
    public void editEnquiry(Scanner sc) {

        System.out.print("Enter the enquiry ID to edit: ");
        int enquiryID;
        try {
            enquiryID = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException ex) {
            System.out.println("Invalid ID.");
            return;
        }

        /* find enquiry in global list */
        Enquiry target = ObjectCreate.enquiryList.stream()
                        .filter(e -> e.getEnquiryID() == enquiryID)
                        .findFirst()
                        .orElse(null);

        if (target == null) {
            System.out.println("Enquiry with ID " + enquiryID + " not found.");
            return;
        }

        /* ownership check */
        if (!target.getSenderID().equals(getNRIC())) {
            System.out.println("You can only edit your own enquiries.");
            return;
        }

        /* already responded? */
        if (target.getResponse() != null && !target.getResponse().isBlank()) {
            System.out.println("Cannot edit an enquiry that has been responded to.");
            return;
        }

        /* ask for new message content */
        System.out.print("Enter new message: ");
        String newMsg = sc.nextLine().trim();
        if (newMsg.isBlank()) {
            System.out.println("Message cannot be empty.");
            return;
        }

        /* update the map stored in Enquiry */
        Map<String,String> msgMap = target.getMessage();        // Assume getter
        // For single‑entry maps we just replace the value
        String key = msgMap.keySet().iterator().next();
        msgMap.put(key, newMsg);

        System.out.println("Enquiry " + enquiryID + " updated successfully.");
    }

    Scanner sc = new Scanner(System.in);

    public Enquiry submitEnquiry() {     
        System.out.print("Enter your enquiry: ");
        String msg = sc.nextLine().trim();
        if (msg.isEmpty()) {
            System.out.println("Enquiry cannot be empty.");
            return null;
        }

        int nextEnquiryId = 0;

        // build the message map expected by the constructor
        Map<String,String> map = new HashMap<>();
        map.put(getNRIC(), msg);                   // or use getName()

        int id = nextEnquiryId++;                  // unique enquiry id
        Enquiry e = new Enquiry(
                map,                               // message map
                id,                                // enquiryID
                getNRIC(),                         // senderID (example)
                null,                              // response (none yet)
                "0");                              // responderID (0 = not replied)

        // optionally store in a global list
        ObjectCreate.enquiryList.add(e);

        System.out.println("Enquiry submitted with ID " + id);
        return e;
    }

    public boolean deleteEnquiry(int enquiryID) {

        /* locate the enquiry in the global list */
        Enquiry target = ObjectCreate.enquiryList.stream()
                        .filter(e -> e.getEnquiryID() == enquiryID)
                        .findFirst()
                        .orElse(null);

        if (target == null) {
            System.out.println("Enquiry with ID " + enquiryID + " not found.");
            return false;
        }

        /* ensure it belongs to this applicant                               */
        if (!target.getSenderID().equals(getNRIC())) {
            System.out.println("You can only delete your own enquiries.");
            return false;
        }

        /* don’t allow deletion after a reply                                 */
        if (target.getResponse() != null && !target.getResponse().isBlank()) {
            System.out.println("Cannot delete an enquiry that has been responded to.");
            return false;
        }

        /* remove from the list                                               */
        boolean removed = ObjectCreate.enquiryList.remove(target);

        if (removed) {
            System.out.println("Enquiry " + enquiryID + " deleted successfully.");
        } else {
            System.out.println("Failed to delete enquiry.");
        }
        return removed;
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
    
    /**
     * Find an enquiry by ID
     */
    // private Enquiry findEnquiry(int enquiryID) {
    //     // This would search for the enquiry in persistent storage
    //     // For now, we return a mock enquiry if the ID matches our test case
    //     if (enquiryID == 12345) {
            
    //         Map<String, String> message = new HashMap<>();
    //         int enquiryIDs = 12345;
    //         int senderID = 1001;
    //         String response = "We will get back to you shortly.";
    //         int responderID = 2001;
    //         Enquiry mockEnquiry = new Enquiry(message,enquiryIDs,senderID,response,responderID);

    //         message.put("subject", "Query about project");
    //         message.put("body", "When does the application period open?");
    //         mockEnquiry.setMessage(message);
    //         mockEnquiry.setEnquiryID(enquiryID);
    //         mockEnquiry.setSenderID(Integer.parseInt(getNRIC()));
    //         return mockEnquiry;
    //     }
    //     return null;
    // }


    public boolean meetsEligibility(FlatType type) {
        if (maritalStatus.equalsIgnoreCase("Single")) {
            return age >= 35 && type == FlatType.TwoRoom;
        } else if (maritalStatus.equalsIgnoreCase("Married")) {
            return age >= 21;                    // any type allowed
        }
        return false;                            // other cases not allowed
    }

}