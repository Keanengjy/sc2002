package person;

import project.Project;
import project.Report;
import project.ReportGenerator;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import main.Application;
import main.ApplicationStatus;

public class HDBManager extends AbstractUser implements ReportGenerator {

    private String HDBOfficesUnder;
    private List<Project> managedProjects;
    private List<Application> pendingApprovals; // Added based on UML
    private boolean pendingRegistration;

    public HDBManager(String name, String nric, String password, MaritalStatus maritalStatus, int age, String HDBOfficesUnder) {
        super(name, nric, password, maritalStatus, age);
        this.HDBOfficesUnder = HDBOfficesUnder;
        this.managedProjects = new ArrayList<>();
        this.pendingApprovals = new ArrayList<>(); // Initialize pending approvals
        this.pendingRegistration = true;
    }

    @Override
    public void calculateEligibilityCriteria() {
        System.out.println("Eligibility criteria calculated for HDBManager.");
    }

    public String getHDBOfficesUnder() {
        return HDBOfficesUnder;
    }

    public void setHDBOfficesUnder(String HDBOfficesUnder) {
        this.HDBOfficesUnder = HDBOfficesUnder;
    }

    public List<Project> getManagedProjects() {
        return new ArrayList<>(managedProjects); // Defensive copy
    }

    public boolean isPendingRegistration() {
        return pendingRegistration;
    }

    public void setPendingRegistration(boolean pendingRegistration) {
        this.pendingRegistration = pendingRegistration;
    }

    public boolean addProject(Project project) {
        if (project == null) {
            throw new IllegalArgumentException("Project cannot be null");
        }

        for (Project existingProject : managedProjects) {
            if (hasOverlappingApplicationPeriod(existingProject, project)) {
                return false;
            }
        }

        managedProjects.add(project);
        return true;
    }

    private boolean hasOverlappingApplicationPeriod(Project project1, Project project2) {
        String startDate1 = project1.getApplicationOpeningDate();
        String endDate1 = project1.getApplicationClosingDate();
        String startDate2 = project2.getApplicationOpeningDate();
        String endDate2 = project2.getApplicationClosingDate();

        return !(endDate1.compareTo(startDate2) < 0 || startDate1.compareTo(endDate2) > 0);
    }

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

    public boolean deleteProject(String projectName) {
        if (projectName == null || projectName.isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be null or empty");
        }

        return managedProjects.removeIf(project -> project.getProjectName().equals(projectName));
    }

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

    public boolean applicationDecision(Application application, boolean approve) {
        if (application == null) {
            throw new IllegalArgumentException("Application cannot be null");
        }

        application.setStatus(approve ? ApplicationStatus.Successful : ApplicationStatus.Unsuccessful);
        pendingApprovals.remove(application); // Remove from pending approvals
        return true;
    }

    public void addPendingApproval(Application application) {
        if (application == null) {
            throw new IllegalArgumentException("Application cannot be null");
        }
        pendingApprovals.add(application);
    }

    public List<Application> getPendingApprovals() {
        return new ArrayList<>(pendingApprovals); // Defensive copy
    }

    public boolean createProject(Project project) {
        if (project == null) {
            throw new IllegalArgumentException("Project cannot be null");
        }
        return addProject(project);
    }

    public Project getProjectDetails(String projectName) {
        if (projectName == null || projectName.isEmpty()) {
            throw new IllegalArgumentException("Project name cannot be null or empty");
        }

        for (Project project : managedProjects) {
            if (project.getProjectName().equals(projectName)) {
                return project;
            }
        }
        return null; // Project not found
    }

    @Override
    public Report generateReport(String criteria) {
        Report report = new Report();
        report.setCriteria(criteria);
        report.generateContent(managedProjects);
        return report;
    }

    @Override
    public Report filterReport(String criteria) {
        return generateReport(criteria); // Simplified filtering logic
    }
}
