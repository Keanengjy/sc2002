package person;

import person.Applicant;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import person.MaritalStatus;
import project.ApplicationStatus;
import project.UserRole;

import project.Enquiry;
import project.HDBFlat;
import project.Project;

public class Application {
    private Applicant applicant;
    private Project project;
    private HDBFlat selectedFlat;
    private ApplicationStatus status;

    public Application(Applicant applicant, Project project) {
        this.applicant = applicant;
        this.project = project;
        this.selectedFlat = null;
        this.status = ApplicationStatus.Pending;
    }

    public void applyProject() {
        if (!applicant.isLoggedIn()) {
            System.out.println("Applicant must be logged in to apply for a project.");
            return;
        }

        if (applicant.getAppliedProject() != null) {
            System.out.println("Applicant has already applied to a project.");
            return;
        }

        if (!applicant.checkEligibility(project)) {
            System.out.println("Applicant is not eligible for this project.");
            return;
        }

        applicant.setAppliedProject(project.getProjectName());
        applicant.setApplicationStatus(ApplicationStatus.Pending);
        System.out.println("Project applied successfully.");
    }

    public void bookFlat(HDBFlat flat) {
        if (!applicant.isLoggedIn()) {
            System.out.println("Applicant must be logged in to book a flat.");
            return;
        }

        if (applicant.getApplicationStatus() != ApplicationStatus.Successful) {
            System.out.println("Application must be successful to book a flat.");
            return;
        }

        this.selectedFlat = flat;
        applicant.setSelectedFlat(flat);
        System.out.println("Flat booked successfully: " + flat.getFlatNumber());
    }

    public void withdrawProject() {
        if (!applicant.isLoggedIn()) {
            System.out.println("Applicant must be logged in to withdraw.");
            return;
        }

        if (applicant.getAppliedProject() == null) {
            System.out.println("No project application to withdraw.");
            return;
        }

        applicant.setAppliedProject(null);
        applicant.setApplicationStatus(null);
        applicant.setSelectedFlat(null);
        this.project = null;
        this.status = null;
        System.out.println("Application withdrawn successfully.");
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public Project getProject() {
        return project;
    }

    public HDBFlat getSelectedFlat() {
        return selectedFlat;
    }

    public Applicant getApplicant() {
        return applicant;
    }
}
