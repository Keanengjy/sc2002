package person;

import person.Applicant;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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

public class Application {
    private Applicant applicant;
    private Project project;
    private HDBFlat selectedFlat;
    private ApplicationStatus status;

    public static final List<Application> applicationRegistry = new ArrayList<>();

    public Application(Applicant applicant, Project project) {
        this.applicant = applicant;
        this.project = project;
        this.selectedFlat = null;
        this.status = ApplicationStatus.Pending;
    }

    public ApplicationStatus getStatus() {return status;}
    public void setStatus(ApplicationStatus status) {this.status = status;}

    public Project getProject() {return project;}


    public HDBFlat getSelectedFlat() {return selectedFlat;}
    public void setSelectedFlat(HDBFlat selectedFlat) {this.selectedFlat = selectedFlat;}

    public Applicant getApplicant() {return applicant;}


        
    public static Application findApplicationForApplicant(Applicant appl) {
        for (Application a : applicationRegistry) {
            if (a.getApplicant() == appl) return a;
        }
        return null;

    }

    public static void applyProject(Project project, Applicant applicant, FlatType desiredType) {

        /* login & duplicate guards (unchanged) */

        if (applicant.getAppliedProject() != null) {
            System.out.println("Applicant has already applied to a project.");
            return;
        }
    
        /* new eligibility check */
        if (!applicant.meetsEligibility(desiredType)) {
            System.out.println("Applicant does not meet age/marital rules for this flat type.");
            return;
        }
    
        /* also ensure the project actually offers that type */
        if (!project.getAvailableFlatsByType().containsKey(desiredType)) {
            System.out.println("Project does not offer the requested flat type.");
            return;
        }
    
        /* create & register application */
        Application newApp = new Application(applicant, project);

        applicationRegistry.add(newApp);
    
        applicant.setAppliedProject(project.getProjectName());
        applicant.setApplicationStatus(ApplicationStatus.Pending);
    
        System.out.printf("Project \"%s\" applied successfully for %s (%s).%n",
                          project.getProjectName(), applicant.getName(), desiredType);
    }

    public static boolean bookFlat(HDBFlat flat, Applicant applicant, HDBOfficer officer) {

        if (applicant.getApplicationStatus() != ApplicationStatus.Successful) {
            System.out.println("Application not successful yet."); return false;
        }
    
        /* locate project */
        Project targetProject = null;
        for (Project p : ObjectCreate.projectList) {
            if (applicant.getAppliedProject().equalsIgnoreCase(p.getProjectName())) {
                targetProject = p; break;
            }
        }
        if (targetProject == null) { System.out.println("Project not found."); return false; }
    
        /* fetch the Application instance */
        Application appObj = findApplicationForApplicant(applicant);
        if (appObj == null) { System.out.println("No Application object found."); return false; }
    
        /* convert flat object to its enum type */
        FlatType type = flat.getFlatType();
    
        /* ask officer to update stock, passing Application as well */
        boolean ok = officer.updateFlatCount(targetProject, type, appObj);
        if (!ok) {
            System.out.println("No more units of that flat type available.");
            return false;
        }
        
        return true;
    }
    
    public void withdrawProject() {


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


}
