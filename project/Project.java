package project;

import person.Applicant;
import person.MaritalStatus;

/*
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
*/

public class Project {
    private String projectName;
    private String neighborhood;
    private String manager;
    private int projectID;
    private ApplicationStatus applicationStatus;
    private boolean visibility;
    private String HDBOfficer;
    private String ApplicationOpeningDate;
    private String ApplicationClosingDate;
    private int AvailableHDBOfficerSlots;

    public Project(String projectName, String neighborhood, String manager, int projectID) {
        this.projectName = projectName;
        this.neighborhood = neighborhood;
        this.manager = manager;
        this.projectID = projectID;
        this.applicationStatus = ApplicationStatus.Pending;
        this.visibility = false;  // Default to "off"
        this.AvailableHDBOfficerSlots = 0;
    }
    

    // Getters and setters
    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    //STOPPED AT FILTER PROJ

    public String getNeighborhood() {
        return neighborhood;
    }
    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getManager() {
        return manager;
    }
    public void setManager(String manager) {
        this.manager = manager;
    }

    public int getProjectID() {
        return projectID;
    }
    public void setProjectID(int projectID) {
        this.projectID = projectID;
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

    public String getHdbOfficer() {
        return HDBOfficer;
    }

    public void setHdbOfficer(String HDBOfficer) {
        this.HDBOfficer = HDBOfficer;
    }
    
    public String getApplicationOpeningDate() {
        return ApplicationOpeningDate;
    }
    public void setApplicationOpeningDate(String applicationOpeningDate) {
        this.ApplicationOpeningDate = applicationOpeningDate;
    }

    public String getApplicationClosingDate() {
        return ApplicationClosingDate;
    }
    public void setApplicationClosingDate(String applicationClosingDate) {
        this.ApplicationClosingDate = applicationClosingDate;
    }
    
    
    public int getAvailableHDBOfficerSlots() {
        return AvailableHDBOfficerSlots;
    }
    
    public void setAvailableHDBOfficerSlots(int availableHDBOfficerSlots) {
        this.AvailableHDBOfficerSlots = availableHDBOfficerSlots;
    }
    
    
    public String filterProject(String details) {
        StringBuilder filteredInfo = new StringBuilder();
        
        if (details.contains("name")) {
            filteredInfo.append("Project Name: ").append(this.projectName).append("\n");
        }
        if (details.contains("neighborhood")) {
            filteredInfo.append("Neighborhood: ").append(this.neighborhood).append("\n");
        }
        if (details.contains("manager")) {
            filteredInfo.append("Manager: ").append(this.manager).append("\n");
        }
        if (details.contains("status")) {
            filteredInfo.append("Application Status: ").append(this.applicationStatus).append("\n");
        }
        if (details.contains("dates")) {
            filteredInfo.append("Opening Date: ").append(this.ApplicationOpeningDate).append("\n");
            filteredInfo.append("Closing Date: ").append(this.ApplicationClosingDate).append("\n");
        }
        
        return filteredInfo.toString();
    }
    

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
