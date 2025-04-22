package projects;

import java.util.List;
import java.util.Map;

enum ApplicationStatus {
    Pending,
    Approved,
    Rejected,
    Booked
}

enum Visibility {
    on,
    off
}

enum FlatType {
    TwoRoom,
    ThreeRoom
}

public class Project {
    // public static List<String> allProject;
    private String projectName;
    private String neighborhood;
    private String manager;
    private String projectID;
    // private ApplicationStatus applicationStatus;
    private boolean visibility;
    private String applicationOpeningDate;
    private String applicationClosingDate;
    private Map<FlatType, Integer> availableFlats;
    // private List<HDBFlat> flats;

    public Project() {
        // Default constructor
        this.projectName = null;
        this.neighborhood = null;
        this.manager = null;
        this.projectID = null;
        //this.applicationStatus = null;
        this.visibility = false;
        this.applicationOpeningDate = null;
        this.applicationClosingDate = null;
        this.availableFlats = null;
        // this.flats = null;
    }

    public Project(String projectName, String neighborhood, String manager, String projectID, boolean visibility,
            String applicationOpeningDate, String applicationClosingDate, Map<FlatType, Integer> availableFlats) {
        this.projectName = projectName;
        this.neighborhood = neighborhood;
        this.manager = manager;
        this.projectID = projectID;
        // this.applicationStatus = applicationStatus;
        this.visibility = visibility;
        this.applicationOpeningDate = applicationOpeningDate;
        this.applicationClosingDate = applicationClosingDate;
        this.availableFlats = availableFlats;
    }

    public void setProjectName(String name) {
        this.projectName = name;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    // public void setApplicationStatus(ApplicationStatus status) {
    //     this.applicationStatus = status;
    // }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public void setApplicationOpeningDate(String date) {
        this.applicationOpeningDate = date;
    }

    public void setApplicationClosingDate(String date) {
        this.applicationClosingDate = date;
    }
    // public void setAvailableFlats(Map<HDBFlat, Integer> availableFlats) {
    // this.availableFlats = availableFlats; }
    // public void setFlats(List<HDBFlat> flats) { this.flats = flats; }

    public String getProjectName() {
        if (visibility) {
            return projectName;
        }
        return null;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getManager() {
        return manager;
    }

    public String getProjectID() {
        return projectID;
    }

    // public ApplicationStatus getApplicationStatus() {
    //     return applicationStatus;
    // }

    public boolean getVisibility() {
        return visibility;
    }

    public String getApplicationOpeningDate() {
        return applicationOpeningDate;
    }

    public String getApplicationClosingDate() {
        return applicationClosingDate;
    }
    // public Map<HDBFlat, Integer> getAvailableFlats() { return availableFlats; }
    // public List<HDBFlat> getFlats() { return flats; }

    // public void addProject(String project) { allProject.add(project); }
    // public void setProjectName(String name) { this.projectName = name; }
    // public void filterProjectDetails(String criteria) {}
    // public void filterProjectName(String name) {}
    public boolean isEligibleForApplicant() {
        return true;
    }

    public boolean isVisible() {
        return visibility;
    }

    
}
