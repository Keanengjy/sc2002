package person;

import project.HDBFlat;

public class Applicant extends AbstractUser {
    private String appliedProject;
    private String applicationStatus;
    private HDBFlat selectedFlat;

    public Applicant(String name, String nric, String password, MaritalStatus maritalStatus, int age) {
        super(name, nric, password, maritalStatus, age);
        this.applicationStatus = "Not Applied";
        this.appliedProject = null;
        this.selectedFlat = null;
    }

    protected void calculateEligibilityCriteria() {
        StringBuilder criteria = new StringBuilder();
        criteria.append("Age: ").append(age).append("\n");
        criteria.append("Marital Status: ").append(maritalStatus).append("\n");

        if (maritalStatus == MaritalStatus.SINGLE) {
            if (age >= 35) {
                criteria.append("Eligible for Single Scheme\n");
            } else {
                criteria.append("Not eligible for Single Scheme (Age < 35)\n");
            }
        } else { // MARRIED
            criteria.append("Eligible for Family Scheme\n");
        }

        this.eligibilityCriteria = criteria.toString();
    }

    public void viewProject() {
        System.out.println("Applied Project: " + appliedProject);
        System.out.println("Application Status: " + applicationStatus);
    }

    public void applyProject(String project) {
        this.appliedProject = project;
    }

    public void setApplicationStatus(String status) {
        this.applicationStatus = status;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void bookFlat(HDBFlat flat) {
        this.selectedFlat = flat;
        System.out.println("Flat booked: " + flat.getFlatType());
    }

    public void withdrawApplication() {
        this.appliedProject = null;
        this.applicationStatus = "Not Applied";
        System.out.println("Application withdrawn.");
    }
}