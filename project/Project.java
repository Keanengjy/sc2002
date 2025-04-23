package project;

import project.FlatType;
import project.Visibility;
import person.MaritalStatus;
import project.ApplicationStatus;

import java.util.List;
import java.util.Map;

import person.HDBManager;
import person.HDBOfficer;

public class Project {
    // public static List<String> allProject;
    private String projectName;
    private String neighborhood;
    private HDBManager manager;
    private String projectID;
    private ApplicationStatus applicationStatus;
    private boolean visibility;
    private String applicationOpeningDate;
    private String applicationClosingDate;
    private Map<FlatType, Integer> availableFlats;
    private List<HDBOfficer> officers;
    // private List<HDBFlat> flats;

    public Project(String projectName, String neighborhood, HDBManager manager, String projectID,
            ApplicationStatus applicationStatus, boolean visibility,
            String applicationOpeningDate, String applicationClosingDate,
            Map<FlatType, Integer> availableFlats, List<HDBOfficer> officers) {
        this.projectName = projectName;
        this.neighborhood = neighborhood;
        this.manager = manager;
        this.projectID = projectID;
        this.applicationStatus = applicationStatus;
        this.visibility = visibility;
        this.applicationOpeningDate = applicationOpeningDate;
        this.applicationClosingDate = applicationClosingDate;
        this.availableFlats = availableFlats;
        this.officers = officers; // Assign list of officers
    }

    public void setProjectName(String name) {
        this.projectName = name;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public void setManager(HDBManager manager) {
        this.manager = manager;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    // public void setApplicationStatus(ApplicationStatus status) {
    // this.applicationStatus = status;
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

    public HDBManager getManager() {
        return manager;
    }

    public String getProjectID() {
        return projectID;
    }

    // public ApplicationStatus getApplicationStatus() {
    // return applicationStatus;
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

    public Map<FlatType, Integer> getAvailableFlats() {
        return availableFlats;
    }

    public List<HDBOfficer> getOfficers() {
        return officers;
    }

    public void addOfficer(HDBOfficer officer) {
        this.officers.add(officer);
    }

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
