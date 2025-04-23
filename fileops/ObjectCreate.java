package fileops;

import java.util.*;
import person.HDBManager;
import person.HDBOfficer;
import project.*;

public class ObjectCreate {

    // ⬇ static so Main can access
    public static final Map<String,HDBOfficer> officerMap = new HashMap<>();
    public static final Map<String,HDBManager> managerMap = new HashMap<>();
    public static final List<HDBOfficer> projectOfficers = new ArrayList<>();
    public static final List<Project>        projectList = new ArrayList<>();

    static {      // static‑init block runs once
        try {
            List<List<String>> managerData = FileOps.readFile("ManagerList");
            List<List<String>> officerData = FileOps.readFile("OfficerList");
            List<List<String>> projectData = FileOps.readFile("ProjectList");

            // ── build officers ────────────────────────────
            for (List<String> rec : officerData.subList(1, officerData.size())) {
                HDBOfficer off = new HDBOfficer(
                        rec.get(0).trim(), rec.get(1).trim(),
                        Integer.parseInt(rec.get(2).trim()),
                        rec.get(3).trim(), rec.get(4).trim(), "");
                officerMap.put(off.getName(), off);
            }

            // ── build managers ────────────────────────────
            for (List<String> rec : managerData.subList(1, managerData.size())) {
                HDBManager manager = new HDBManager(
                        rec.get(0).trim(), rec.get(1).trim(),
                        Integer.parseInt(rec.get(2).trim()),
                        rec.get(3).trim(), rec.get(4).trim());
                managerMap.put(manager.getName(), manager);
            }

            // ── build projects ────────────────────────────
            for (List<String> record : projectData.subList(1, projectData.size())) {
                // Map<FlatType,Integer> flats = new HashMap<>();
                // flats.put(FlatType.TwoRoom , Integer.parseInt(record.get(3).trim()));
                // flats.put(FlatType.ThreeRoom, Integer.parseInt(record.get(6).trim()));

                HDBManager manager = managerMap.get(record.get(10).trim());
                if (manager==null) continue;

                // Create a list of officers for this project
                
                projectOfficers.clear(); // Clear the list for each project
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
                        record.get(0).trim(),   // name
                        record.get(1).trim(),   // neighbourhood
                        manager,              // updated variable name
                        "",                  // id
                        ApplicationStatus.Pending,
                        true,
                        record.get(8).trim(),   // open
                        record.get(9).trim(),   // close
                        // flats,
                        projectOfficers,
                        Integer.parseInt(record.get(11).trim()),
                        Integer.parseInt(record.get(3).trim()),
                        Integer.parseInt(record.get(6).trim()));

                projectList.add(p);
            }



        } catch(Exception e){ e.printStackTrace(); }
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
