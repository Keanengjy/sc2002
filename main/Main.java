package main;

import project.ApplicationStatus;
import project.Enquiry;
import project.FlatType;
import project.Visibility;
import person.MaritalStatus;

import fileops.FileOps;
import fileops.ObjectCreate;
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

            // // grab objects built in ObjectCreate
            // HDBOfficer officer = ObjectCreate.officerMap.get("Daniel");
            Project     p      = ObjectCreate.projectList.get(0);   // first project for demo
            
            // /* Officer submits application */
            // officer.registerProject(p);
        
            // /* Manager reviews the pending list */
            // p.getManager().processPendingApprovals();
        
            // /* Prompt for approval decision */
            // Scanner sc = new Scanner(System.in);
            // System.out.print(
            //     "Manager " + p.getManager().getName() +
            //     ": approve officer " + officer.getName() +
            //     " for project \"" + p.getProjectName() + "\"? (yes/no) ");
            // String reply = sc.nextLine().trim();
        
            // if (reply.equalsIgnoreCase("yes")) {
            //     p.getManager().approveOfficer(p);   // approves & updates officer
            //     System.out.println("Approved");
            // } else {
            //     System.out.println("Not approved");
            // }
        
            // /* Show officer’s view after decision */
            // officer.getProjectDetails();
            
        
            // Test Enquiry reply

            // Map<String,String> msg = new HashMap<>();
            // msg.put("Applicant123", "When is key collection?");
            // Enquiry enquiry = new Enquiry(msg, 1, 123, null, 0);

            // // 2. create officer and let them reply
            HDBOfficer officer = ObjectCreate.officerMap.get("Daniel");
            // officer.replyEnquiry(enquiry);

            // // 3. print stored reply to verify
            // System.out.println("\n=== Stored in Enquiry object ===");
            // System.out.println("Responder ID : " + enquiry.getResponderID());
            // System.out.println("Reply        : " + enquiry.getResponse());
            
            //Book flats

            // System.out.println("Initial stock: " + p.getFlats());
            // for (int i = 1; i <= 3; i++) {
            //     boolean ok = officer.updateFlatCount(p, FlatType.TwoRoom);
            //     System.out.printf("  Attempt %d to book 2 room: %s%n",
            //                       i, ok ? "SUCCESS" : "FAILED");
            // }
            //         // Book three 3‑room units
            // for (int i = 1; i <= 3; i++) {
            //     boolean ok = officer.updateFlatCount(p, FlatType.ThreeRoom);
            //     System.out.printf("  Attempt %d to book 3 room: %s%n",
            //                     i, ok ? "SUCCESS" : "FAILED");
            // }

            // System.out.println("\nFinal stock: " + p.getFlats());

            // Write to file example
            // data.add(List.of("John Doe", "12345678A"));
            // FileOps.writeFile("ApplicantList", data);

            }catch(Exception e)
        {
            e.printStackTrace();
        }



    }
}