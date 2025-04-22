package fileops;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileOps {
    private static final String APPLICANT_LIST_FILE = "Data/ApplicantList.csv";
    private static final String OFFICER_LIST_FILE = "Data/OfficerList.csv";
    private static final String MANAGER_LIST_FILE = "Data/ManagerList.csv";
    private static final String PROJECT_LIST_FILE = "Data/ProjectList.csv";
    private static final String APPLICATION_LIST_FILE = "Data/ApplicationList.csv";
    private static final String REGISTRATION_LIST_FILE = "Data/RegistrationList.csv";
    private static final String ENQUIRY_LIST_FILE = "Data/EnquiryList.csv";
    private static final String REPLY_LIST_FILE = "Data/ReplyList.csv";

    private static String getFilePath(String filename) {
        String filepath;
        switch (filename) {
            case "ApplicantList":
                filepath = APPLICANT_LIST_FILE;
                break;
            case "OfficerList":
                filepath = OFFICER_LIST_FILE;
                break;
            case "ManagerList":
                filepath = MANAGER_LIST_FILE;
                break;
            case "ProjectList":
                filepath = PROJECT_LIST_FILE;
                break;
            case "ApplicationList":
                filepath = APPLICATION_LIST_FILE;
                break;
            case "RegistrationList":
                filepath = REGISTRATION_LIST_FILE;
                break;
            case "EnquiryList":
                filepath = ENQUIRY_LIST_FILE;
                break;
            case "ReplyList":
                filepath = REPLY_LIST_FILE;
                break;
            default:
                throw new IllegalArgumentException("Invalid filename: " + filename);
        }
        return filepath;
    }

    public static List<List<String>> readFile(String filename) throws Exception {
        String filePath = getFilePath(filename);
        List<List<String>> records = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                records.add(getRecordFromLine(scanner.nextLine()));
            }
        }
        return records;
    }

    private static List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }

    public static void writeFile(String filename, List<List<String>> data) throws Exception{
        String filePath = getFilePath(filename);
        try (PrintWriter writer = new PrintWriter(filePath)) {
            for (List<String> record : data) {
                for (String i : record) {
                    writer.print(i + ",");
                }
                writer.println(); // Move to the next line after writing a record
            }
        } catch (Exception e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    // Example usage of the FileOps class
    // public static void main(String[] args) {
    //     try {
    //         // Read from file example  
    //         List<List<String>> data = readFile("ApplicantList");
    //         for (List<String> record : data) {
    //             System.out.println(record);
    //         }
    //         // Write to file example
    //         data.add(List.of("John Doe", "12345678A"));
    //         writeFile("ApplicantList", data);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }
}
