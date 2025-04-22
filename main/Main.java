package main;

import fileops.FileOps;
import project.HDBFlat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            // Read from file example  
            List<List<String>> applicantsData = FileOps.readFile("ApplicantList");
            List<List<String>> managerData = FileOps.readFile("ManagerList");
            List<List<String>> officerData = FileOps.readFile("OfficerList");
            List<List<String>> projectData = FileOps.readFile("ProjectList");
            for (List<String> record : projectData.subList(1, projectData.size())) {
                
                Map<FlatType, Integer> availableFlats = new HashMap<>();
                availableFlats.put(main.FlatType.TwoRoom, Integer.parseInt(record.get(2)));
                availableFlats.put(main.FlatType.ThreeRoom, Integer.parseInt(record.get(3)));

                Project p = new Project(
                    record.get(0),  // projectName
                    record.get(1),  // neighborhood
                    record.get(10), // manager
                    "",             // projectID (unused)
                    true,           // visibility (arbitrary default)
                    record.get(8),  // applicationOpeningDate
                    record.get(9),  // applicationClosingDate
                    availableFlats
                );

                System.out.println(p.getProjectName());

            }

            
            // Write to file example
            // data.add(List.of("John Doe", "12345678A"));
            // FileOps.writeFile("ApplicantList", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}