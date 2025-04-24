package project;

import project.FlatType;
import project.Visibility;
import person.MaritalStatus;
import project.ApplicationStatus;

import person.HDBManager;
import person.HDBOfficer;
import person.Applicant;
import java.util.HashMap;
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
    public void setMessage(Map<String, String> message) {
        this.message = message;
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
        return enquiryID;}

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
    
    public String getEnquiry() {
        StringBuilder enquiryDetails = new StringBuilder();
        enquiryDetails.append("Enquiry ID: ").append(enquiryID).append("\n");
        enquiryDetails.append("Sender ID: ").append(senderID).append("\n");
        
        enquiryDetails.append("Message: \n");
        for (Map.Entry<String, String> entry : message.entrySet()) {
            enquiryDetails.append("  ").append(entry.getKey()).append(": ")
                         .append(entry.getValue()).append("\n");
        }
        
        if (responderID != -1) {
            enquiryDetails.append("Response: ").append(response).append("\n");
            enquiryDetails.append("Responder ID: ").append(responderID).append("\n");
        } else {
            enquiryDetails.append("Status: Pending response\n");
        }
        
        return enquiryDetails.toString();
    }
    
    public void setEnquiry(String enquiry) {
        // In a real implementation, this would parse the enquiry string
        // and populate the message map accordingly
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("content", enquiry);
        this.message = messageMap;
    }
}
