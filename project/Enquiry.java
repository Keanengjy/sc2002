package project;

import java.util.HashMap;
import java.util.Map;

public class Enquiry {
    private Map<String, String> message;
    private int enquiryID;
    private String senderID;
    private String response;
    private int responderID;
    
    public Enquiry(String message, int enquiryID, String senderID) {
        this.message = new HashMap<>();
        this.enquiryID = enquiryID;
        this.senderID = senderID;
        this.response = "";
        this.responderID = -1; // No responder yet
    }

    // Getters and setters
    public Map<String, String> getMessage() {
        return message;
    }
    public void setMessage(Map<String, String> message) {
        this.message = message;
    }

    public int getEnquiryID() {
        return enquiryID;
    }
    public void setEnquiryID(int enquiryID) {
        this.enquiryID = enquiryID;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response, int responderID) {
        this.response = response;
        this.responderID = responderID;
    }

    public int getResponderID() {
        return responderID;
    }
    public void setResponderID(int responderID) {
        this.responderID = responderID;
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
