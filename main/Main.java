package main;

import fileops.FileOps;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Debug: Print current working directory
            System.out.println("Current working directory: " + System.getProperty("user.dir"));

            // Read from file example
            List<List<String>> data = FileOps.readFile("ApplicantList");

            // Debug: Print data size
            System.out.println("Number of records: " + data.size());

            for (List<String> record : data) {
                System.out.println(record);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print exception details
        } finally {
            System.out.println("File operations completed.");
        }
    }
}