package fileops;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileOps {
    private static final String APPLICANT_LIST_FILE = "sc2002/Data/ApplicantList.csv";

    private static String getFilePath(String filename) {
        String filepath;
        switch (filename) {
            case "ApplicantList":
                filepath = APPLICANT_LIST_FILE;
                break;
            default:
                throw new IllegalArgumentException("Invalid filename: " + filename);
        }
        System.out.println("Resolved file path: " + filepath); // Debugging
        return filepath;
    }

    public static List<List<String>> readFile(String filename) throws Exception {
        String filePath = getFilePath(filename);
        List<List<String>> records = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            throw new Exception("File not found: " + filePath);
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                records.add(getRecordFromLine(scanner.nextLine()));
            }
        }
        return records;
    }

    private static List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }
}