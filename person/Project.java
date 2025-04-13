package person;

import java.util.ArrayList;
import java.util.List;

enum ApplicationStatus {
    PENDING,
    SUCCESSFUL,
    UNSUCCESSFUL,
    BOOKED
}

enum Visibility {
    ON,
    OFF
}

class DuplicateProjectException extends Exception {
    public DuplicateProjectException(String message) {
        super(message);
    }
}

class ProjectNotFoundException extends Exception {
    public ProjectNotFoundException(String message) {
        super(message);
    }
}

public class Project {
    private String projectName;
    private String neighborhood;
    private String manager;
    private int projectID;
    private ApplicationStatus applicationStatus;
    private boolean visibility;
    private HDBOfficer hdbOfficer;
    private String applicationOpeningDate;
    private String applicationClosingDate;
    private List<HDBFlat> availableFlats;
    private int availableOfficerSlots;
    
    /**
     * Constructs a new Project with the specified parameters.
     * 
     * @param projectName The name of the project
     * @param neighborhood The neighborhood of the project
     * @param manager The manager of the project
     * @param projectID The ID of the project
     * @param applicationOpeningDate The opening date for applications
     * @param applicationClosingDate The closing date for applications
     * @param availableOfficerSlots The number of slots available for officers
     */
    public Project(String projectName, String neighborhood, String manager, int projectID, 
                  String applicationOpeningDate, String applicationClosingDate, int availableOfficerSlots) {
        this.projectName = projectName;
        this.neighborhood = neighborhood;
        this.manager = manager;
        this.projectID = projectID;
        this.applicationStatus = ApplicationStatus.PENDING;
        this.visibility = true; // Default to visible
        this.applicationOpeningDate = applicationOpeningDate;
        this.applicationClosingDate = applicationClosingDate;
        this.availableFlats = new ArrayList<>();
        this.availableOfficerSlots = availableOfficerSlots;
    }

    // Getters and setters
    public String getProjectName() {
        return projectName;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getManager() {
        return manager;
    }

    public int getProjectID() {
        return projectID;
    }

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public boolean isVisible() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public HDBOfficer getHdbOfficer() {
        return hdbOfficer;
    }

    public void setHdbOfficer(HDBOfficer hdbOfficer) {
        this.hdbOfficer = hdbOfficer;
    }
    
    public String getApplicationOpeningDate() {
        return applicationOpeningDate;
    }
    
    public String getApplicationClosingDate() {
        return applicationClosingDate;
    }
    
    public List<HDBFlat> getAvailableFlats() {
        return new ArrayList<>(availableFlats); // Return a copy to preserve encapsulation
    }
    
    public void addFlat(HDBFlat flat) {
        availableFlats.add(flat);
    }
    
    public int getAvailableOfficerSlots() {
        return availableOfficerSlots;
    }
    
    public void decrementOfficerSlots() {
        if (availableOfficerSlots > 0) {
            availableOfficerSlots--;
        }
    }
    
    /**
     * Gets the detailed information about the project.
     * 
     * @return The project details
     */
    public String getProjectDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Project Name: ").append(projectName).append("\n");
        details.append("Neighborhood: ").append(neighborhood).append("\n");
        details.append("Project ID: ").append(projectID).append("\n");
        details.append("Manager: ").append(manager).append("\n");
        details.append("Application Status: ").append(applicationStatus).append("\n");
        details.append("Visibility: ").append(visibility ? "Visible" : "Hidden").append("\n");
        details.append("Application Period: ").append(applicationOpeningDate).append(" to ")
               .append(applicationClosingDate).append("\n");
        details.append("Available Officer Slots: ").append(availableOfficerSlots).append("\n");
        details.append("Available Flats: ").append(availableFlats.size()).append("\n");
        
        return details.toString();
    }
    
    /**
     * Checks if an applicant is eligible for this project.
     * 
     * @param applicant The applicant to check
     * @return True if eligible, false otherwise
     */
    public boolean isEligibleForApplicant(Applicant applicant) {
        if (!visibility) {
            return false; // Project not visible
        }
        
        // Basic eligibility criteria based on age and marital status
        if (applicant.getAge() < 21) {
            return false; // Minimum age requirement
        }
        
        // Different criteria for different marital status
        if (applicant.getMaritalStatus() == MaritalStatus.SINGLE && applicant.getAge() < 35) {
            return false; // Singles under 35 are not eligible
        }
        
        return true;
    }
}
