package project;

import project.FlatType;
import project.Visibility;
import person.MaritalStatus;
import project.ApplicationStatus;

import person.HDBManager;
import person.HDBOfficer;
import person.Applicant;
import java.util.Map;

public class Enquiry {
    private Map<String, String> message;
    private int enquiryID;
    private int senderID;
    private String response;
    private int responderID;

    public Enquiry(Map<String, String> message, int enquiryID, int senderID, String response, int responderID) {
        this.message = message;
        this.enquiryID = enquiryID;
        this.senderID = senderID;
        this.response = response;
        this.responderID = responderID;
    }

    public void getMessage() {
        // Assuming this method is supposed to return the enquiry message
        for (Map.Entry<String, String> entry : message.entrySet()) {
            System.out.println("Enquiry from: " + entry.getKey() + " - Message: " + entry.getValue());
        }
    }

    public void setMessage(String enquiry) {
        // Assuming this method is supposed to set the enquiry message
        String[] parts = enquiry.split(":");
        if (parts.length == 2) {
            message.put(parts[0].trim(), parts[1].trim());
        } else {
            System.out.println("Invalid enquiry format. Expected format: 'Sender: Message'");
        }
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setEnquiryID(int enquiryID) {
        this.enquiryID = enquiryID;
    }

    public int getEnquiryID() {
        return enquiryID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setResponderID(int responderID) {
        this.responderID = responderID;
    }

    public int getResponderID() {
        return responderID;
    }
}