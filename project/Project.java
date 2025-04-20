package project;

import java.util.List;

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

public class Project {
    private String projectName;
    private String neighborhood;
    private String manager;
    private String projectID;
    private ApplicationStatus applicationStatus;
    private boolean visibility;
    private String applicationOpeningDate;
    private String applicationClosingDate;
    private int availableFlats;
    private List<HDBFlat> flats;

    public void setProjectName(String name) { this.projectName = name; }
    public void filterProjectDetails(String criteria) {}
    public void filterProjectName(String name) {}
    public boolean isEligibleForApplicant() { return true; }
}
