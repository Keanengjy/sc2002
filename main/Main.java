package main;

import fileops.FileOps;
import projects.HDBFlat;
import projects.Project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


enum FlatType {
    TwoRoom,
    ThreeRoom
}

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

                Project project = new Project(
                    record.get(0),  // projectName
                    record.get(1),  // neighborhood
                    record.get(10), // manager
                    "",             // projectID (unused)
                    true,           // visibility (arbitrary default)
                    record.get(8),  // applicationOpeningDate
                    record.get(9),  // applicationClosingDate
                    availableFlats
                );

                // Project project = new Project(
                //     "Project A",
                //     "Woodlands",
                //     "Manager X",
                //     "P001",
                //     true,
                //     "2025-05-01",
                //     "2025-06-01",
                //     availableFlats
                // );

            }

            
            
            // Write to file example
            // data.add(List.of("John Doe", "12345678A"));
            // FileOps.writeFile("ApplicantList", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}