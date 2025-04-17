package main;

import fileops.FileOps;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Read from file example  
            List<List<String>> data = FileOps.readFile("ApplicantList");
            for (List<String> record : data) {
                System.out.println(record);
            }
            // Write to file example
            // data.add(List.of("John Doe", "12345678A"));
            // FileOps.writeFile("ApplicantList", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}