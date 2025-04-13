package person;

import java.util.ArrayList;
import java.util.List;

public class HDBManager extends AbstractUser implements ReportGenerator{
     private String HDBOfficesUnder;
    private List<Project> managedProjects;
    private boolean pendingRegistration;

    /**
     * Constructs a new HDBManager with specified attributes.
     * 
     * @param name         Manager's name
     * @param NRIC         Manager's NRIC
     * @param password     Manager's password
     * @param maritalStatus Manager's marital status
     * @param age          Manager's age
     * @param eligibilityCriteria Manager's eligibility criteria
     * @param HDBOfficesUnder Offices under this manager's supervision
     */
    public HDBManager(String name, String NRIC, String password, MaritalStatus maritalStatus, 
                      int age, String eligibilityCriteria, String HDBOfficesUnder) {
        super(name, NRIC, password, maritalStatus, age, eligibilityCriteria);
        this.HDBOfficesUnder = HDBOfficesUnder;
        this.managedProjects = new ArrayList<>();
        this.pendingRegistration = true;
    }

    /**
     * Gets HDB offices under this manager's supervision.
     * 
     * @return String representing offices under supervision
     */
    public String getHDBOfficesUnder() {
        return HDBOfficesUnder;
    }

    /**
     * Sets HDB offices under this manager's supervision.
     * 
     * @param HDBOfficesUnder Offices to be put under supervision
     */
    public void setHDBOfficesUnder(String HDBOfficesUnder) {
        this.HDBOfficesUnder = HDBOfficesUnder;
    }

    /**
     * Gets list of projects managed by this manager.
     * 
     * @return List of managed projects
     */
    public List<Project> getManagedProjects() {
        return new ArrayList<>(managedProjects); // Return defensive copy
    }

    /**
     * Checks if manager is pending registration.
     * 
     * @return true if registration is pending, false otherwise
     */
    public boolean isPendingRegistration() {
        return pendingRegistration;
    }

    /**
     * Sets registration status for the manager.
     * 
     * @param pendingRegistration new registration status
     */
    public void setPendingRegistration(boolean pendingRegistration) {
        this.pendingRegistration = pendingRegistration;
    }

    /**
     * Adds a project to the list of managed projects if the manager doesn't already
     * have a project in the same application period.
     * 
     * @param project Project to be added
     * @return true if project was added successfully, false otherwise
     * @throws IllegalArgumentException if project is null
     */
    public boolean addProject(Project project) {
        if (project == null) {
            throw new IllegalArgumentException("Project cannot be null");
        }

        // Check if manager already has a project in the same application period (Open/Close principle)
        for (Project existingProject : managedProjects) {
            if (hasOverlappingApplicationPeriod(existingProject, project)) {
                return false;
            }
        }

        managedProjects.add(project);
        return true;
    }

    /**
     * Checks if two projects have overlapping application periods.
     * 
     * @param project1 First project
     * @param project2 Second project
     * @return true if periods overlap, false otherwise
     */
    private boolean hasOverlappingApplicationPeriod(Project project1, Project project2) {
        String startDate1 = project1.getApplicationOpeningDate();
        String endDate1 = project1.getApplicationClosingDate();
        String startDate2 = project2.getApplicationOpeningDate();
        String endDate2 = project2.getApplicationClosingDate();
        
        // Simple string comparison (assuming dates are in comparable format)
        return !(endDate1.compareTo(startDate2) < 0 || startDate1.compareTo(endDate2) > 0);
    }

    /**
     * Creates a new BTO project.
     * 
     * @return true if project was created successfully, false otherwise
     */
    public boolean createProject() {
        // Implementation would create a new project and call addProject
        // This is a simplified version
        return true;
    }

    /**
     * Edits an existing project's name and other details.
     * 
     * @param projectName current name of the project
     * @param newName new name for the project
     * @return true if edit was successful, false otherwise
     * @throws IllegalArgumentException if projectName is null or empty
     */
    public boolean editProject(String projectName, String newName) {
        if (projectName == null || projectName.isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be null or empty");
        }
        
        for (Project project : managedProjects) {
            if (project.getProjectName().equals(projectName)) {
                project.setProjectName(newName);
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes a project from managed projects.
     * 
     * @param projectName name of project to delete
     * @return true if deletion was successful, false otherwise
     * @throws IllegalArgumentException if projectName is null or empty
     */
    public boolean deleteProject(String projectName) {
        if (projectName == null || projectName.isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be null or empty");
        }
        
        return managedProjects.removeIf(project -> project.getProjectName().equals(projectName));
    }

    /**
     * Toggles visibility of a project.
     * 
     * @param projectName name of project
     * @param visibility new visibility status
     * @return true if visibility was toggled successfully, false otherwise
     * @throws IllegalArgumentException if projectName is null or empty
     */
    public boolean toggleProjectVisibility(String projectName, boolean visibility) {
        if (projectName == null || projectName.isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be null or empty");
        }
        
        for (Project project : managedProjects) {
            if (project.getProjectName().equals(projectName)) {
                project.setVisibility(visibility);
                return true;
            }
        }
        return false;
    }

    /**
     * Makes decision on application (approve or reject).
     * 
     * @param decision approval decision
     * @return true if decision was processed successfully, false otherwise
     */
    public boolean applicationDecision(String decision) {
        // Implementation would process application decisions
        // This is a simplified version
        return true;
    }

    /**
     * Approves an officer registration.
     * 
     * @param officer HDB officer to approve
     * @return true if approval was successful, false otherwise
     * @throws IllegalArgumentException if officer is null
     */
    public boolean approveOfficer(HDBOfficer officer) {
        if (officer == null) {
            throw new IllegalArgumentException("Officer cannot be null");
        }
        
        // Check if officer is already an applicant or registered for another project
        if (officer.isApplicantForAnyProject() || officer.hasRegisteredForOtherProject()) {
            return false;
        }
        
        officer.setPendingRegistration(false);
        return true;
    }

    /**
     * Approves a BTO application.
     * 
     * @param application application to approve
     * @return true if approval was successful, false otherwise
     * @throws IllegalArgumentException if application is null
     */
    public boolean approveBTOApplication(Application application) {
        if (application == null) {
            throw new IllegalArgumentException("Application cannot be null");
        }
        
        application.setStatus(ApplicationStatus.Successful);
        return true;
    }

    /**
     * Rejects a BTO application.
     * 
     * @param application application to reject
     * @return true if rejection was successful, false otherwise
     * @throws IllegalArgumentException if application is null
     */
    public boolean rejectBTOApplication(Application application) {
        if (application == null) {
            throw new IllegalArgumentException("Application cannot be null");
        }
        
        application.setStatus(ApplicationStatus.Unsuccessful);
        return true;
    }

    /**
     * Generates a report based on specified criteria.
     * Implementation of method from ReportGenerator interface.
     * 
     * @param criteria criteria for report generation
     * @return Report object containing generated report
     */
    @Override
    public Report generateReport(String criteria) {
        // Create report based on criteria
        Report report = new Report();
        report.setCriteria(criteria);
        report.generateContent(managedProjects);
        return report;
    }

    /**
     * Filters reports based on specified criteria.
     * 
     * @param criteria criteria for filtering
     * @return filtered Report object
     */
    public Report filterReport(String criteria) {
        // Implementation would filter an existing report based on criteria
        // This is a simplified version
        return generateReport(criteria);
    }

    /**
     * Gets all projects in the system.
     * 
     * @return list of all projects
     */
    public List<Project> getAllProjects() {
        // Would return all projects from database, not just managed ones
        // Simplified version returns only managed projects
        return getManagedProjects();
    }

    /**
     * Gets projects filtered by specified criteria.
     * 
     * @param filterCriteria criteria for filtering
     * @return filtered list of projects
     */
    public List<Project> getFilteredProjects(String filterCriteria) {
        List<Project> filteredProjects = new ArrayList<>();
        
        for (Project project : managedProjects) {
            if (matchesFilterCriteria(project, filterCriteria)) {
                filteredProjects.add(project);
            }
        }
        
        return filteredProjects;
    }
    
    /**
     * Checks if project matches the specified filter criteria.
     * 
     * @param project project to check
     * @param filterCriteria criteria to match against
     * @return true if project matches criteria, false otherwise
     */
    private boolean matchesFilterCriteria(Project project, String filterCriteria) {
        // Implement filtering logic based on different criteria
        return project.getProjectName().contains(filterCriteria) || 
               project.getNeighborhood().contains(filterCriteria);
    }
    
    /**
     * Creates a new project with the specified details.
     * 
     * @param name project name
     * @param neighborhood project neighborhood
     * @param numUnits number of units
     * @param openDate application opening date
     * @param closeDate application closing date
     * @return the created project or null if creation failed
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public Project createNewProject(String name, String neighborhood, int numUnits, 
                                   String openDate, String closeDate) {
        if (name == null || name.isEmpty() || neighborhood == null || neighborhood.isEmpty() ||
            numUnits <= 0 || openDate == null || openDate.isEmpty() || 
            closeDate == null || closeDate.isEmpty()) {
            throw new IllegalArgumentException("Invalid project parameters");
        }
        
        Project newProj = new Project(name, neighborhood, numUnits, openDate, closeDate);
        if (addProject(newProj)) {
            return newProj;
        }
        return null;
    }
}
