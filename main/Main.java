package main;

import project.ApplicationStatus;
import project.Enquiry;
import project.FlatType;
import project.Visibility;
import person.MaritalStatus;

import fileops.FileOps;
import person.HDBManager;
import person.HDBOfficer;
import project.Project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {

            Scanner scanner = new Scanner(System.in);
            // Read from file example
            List<List<String>> applicantsData = FileOps.readFile("ApplicantList");
            List<List<String>> managerData = FileOps.readFile("ManagerList");
            List<List<String>> officerData = FileOps.readFile("OfficerList");
            List<List<String>> projectData = FileOps.readFile("ProjectList");

            Map<String, HDBOfficer> officerMap = new HashMap<>();
            for (List<String> record : officerData.subList(1, officerData.size())) {
                String name = record.get(0).trim();
                String NRIC = record.get(1).trim();
                int age = Integer.parseInt(record.get(2).trim());
                String maritalStatus = record.get(3).trim();
                String password = record.get(4).trim();

                HDBOfficer officer = new HDBOfficer(name, NRIC, age, maritalStatus, password, "");
                officerMap.put(name, officer);
            }

            Map<String, HDBManager> managerMap = new HashMap<>();
            for (List<String> record : managerData.subList(1, managerData.size())) {
                String name = record.get(0);
                String NRIC = record.get(1);
                int age = Integer.parseInt(record.get(2));
                String maritalStatus = record.get(3);
                String password = record.get(4);

                HDBManager manager = new HDBManager(name, NRIC, age, maritalStatus, password);
                managerMap.put(name, manager);
            }

            for (List<String> record : projectData.subList(1, projectData.size())) {
                Map<FlatType, Integer> availableFlats = new HashMap<>();
                availableFlats.put(FlatType.TwoRoom, Integer.parseInt(record.get(3)));
                availableFlats.put(FlatType.ThreeRoom, Integer.parseInt(record.get(6)));

                String managerName = record.get(10).trim();
                HDBManager manager = managerMap.get(managerName);

                // Create a list of officers for this project
                List<HDBOfficer> projectOfficers = new ArrayList<>();

                for (int i = 12; i < record.size(); i++) {
                    String officerName = stripQuotes(record.get(i).trim());
                    if (officerName.isEmpty()) continue;
                
                    HDBOfficer officer = officerMap.get(officerName);
                    if (officer != null) {
                        projectOfficers.add(officer);
                    } else {
                        System.out.println("Officer with name \"" + officerName + "\" not found.");
                    }
                }

                Project p = new Project(
                    record.get(0),  // projectName
                    record.get(1),  // neighborhood
                    manager,        // manager object resolved from name
                    "",             // projectID (unused)
                    ApplicationStatus.Pending, // applicationStatus (unused)
                    true,           // visibility (arbitrary default)
                    record.get(8),  // applicationOpeningDate
                    record.get(9),  // applicationClosingDate
                    availableFlats,
                    projectOfficers  // list of officers
                );

                    // Simulate officer registering for a project

                    // HDBOfficer officer = officerMap.get("Daniel");
                    // if (officer != null) {
                    //     officer.registerProject(p);
                    // }

                    // Show all pending approvals by this manager

                    // manager.processPendingApprovals();

                    // Let manager approve or reject

                    // System.out.println("Do you want to approve officer for project " + p.getProjectName() + "? (yes/no)");
                    // String input = scanner.nextLine();
                    // if (input.equalsIgnoreCase("yes")) {
                    //     manager.approveOfficer(p);
                    // } else {
                    //     System.out.println("Approval not granted.");};

                    // System.out.println("Project Details for " + officer.getName() + "'s Registered Project:");
                    // officer.getProjectDetails();

                    // Test Enquiry reply

                    // Map<String,String> msg = new HashMap<>();
                    // msg.put("Applicant123", "When is key collection?");
                    // Enquiry enquiry = new Enquiry(msg, 1, 123, null, 0);

                    // // 2. create officer and let them reply
                    // HDBOfficer officer = officerMap.get("Daniel");
                    // officer.replyEnquiry(enquiry);

                    // // 3. print stored reply to verify
                    // System.out.println("\n=== Stored in Enquiry object ===");
                    // System.out.println("Responder ID : " + enquiry.getResponderID());
                    // System.out.println("Reply        : " + enquiry.getResponse());
                    
            }

            

            // Write to file example
            // data.add(List.of("John Doe", "12345678A"));
            // FileOps.writeFile("ApplicantList", data);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private static String stripQuotes(String value) {
        if (value.startsWith("\"")) {
            return value.substring(1);
        } else if (value.endsWith("\"")) {
            return value.substring(0, value.length() - 1);
        }
        return value;
    }
}