package project;

<<<<<<< HEAD
import java.util.HashMap;
=======
import project.FlatType;
import project.Visibility;
import person.MaritalStatus;
import project.ApplicationStatus;

import person.HDBManager;
import person.HDBOfficer;
import person.Applicant;
>>>>>>> d4a37763a682a225f6ab7e66cbb39fc6cf5ff63e
import java.util.Map;

public class Enquiry {
    private Map<String, String> message;
    private int enquiryID;
    private int senderID;
    private String response;
    private int responderID;

<<<<<<< HEAD
    public Enquiry(int enquiryID, int senderID) {
        this.message = new HashMap<>();
        this.enquiryID = enquiryID;
        this.senderID = senderID;
        this.response = "";
        this.responderID = 0;
    }

    public String getEnquiry() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : message.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    public void setEnquiry(String key, String value) {
        this.message.put(key, value);
    }

    public int getEnquiryID() {
        return this.enquiryID;
    }
=======
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

>>>>>>> d4a37763a682a225f6ab7e66cbb39fc6cf5ff63e
    public void setEnquiryID(int enquiryID) {
        this.enquiryID = enquiryID;
    }

<<<<<<< HEAD
    public int getSenderID() {
        return this.senderID;
    }
=======
    public int getEnquiryID() {
        return enquiryID;
    }

>>>>>>> d4a37763a682a225f6ab7e66cbb39fc6cf5ff63e
    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

<<<<<<< HEAD
    public String getResponse() {
        return this.response;
    }
    public void setResponse(String response) {
        this.response = response;
    }
    
    public int getRespondID() {
        return this.responderID;
    }
    public void setRespondID(int respondID) {
        this.responderID = respondID;
    }

    public boolean setEnquiry(Enquiry inquiry, String string) {
        if (inquiry != null && string != null) {
            this.message.put("Content", string);
            return true;
        }
        return false;
=======
    public int getSenderID() {
        return senderID;
    }

    public void setResponderID(int responderID) {
        this.responderID = responderID;
    }

    public int getResponderID() {
        return responderID;
>>>>>>> d4a37763a682a225f6ab7e66cbb39fc6cf5ff63e
    }
}