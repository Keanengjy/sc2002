package project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Enquiry implements EnquiryHandler {
    private Map<String, String> message;
    private int enquiryID;
    private String senderID;
    private String response;
    private String responderID;

    public Enquiry(Map<String, String> message, int enquiryID, String senderID, String response, String responderID) {
        this.message = message;
        this.enquiryID = enquiryID;
        this.senderID = senderID;
        this.response = response;
        this.responderID = responderID;
    }

    public Map<String,String> getMessage() {
        // Assuming this method is supposed to return the enquiry message
        return message;
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

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setResponderID(String responderID) {
        this.responderID = responderID;
    }

    public String getResponderID() {
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
        
        return enquiryDetails.toString();
    }
        
    public static String viewAllEnquiries(List<Enquiry> list) {
        if (list == null || list.isEmpty()) return "No enquiries in the system.";

        StringBuilder sb = new StringBuilder("\n=== All Enquiries ===\n");
        for (Enquiry e : list) {
            sb.append(e.getEnquiry());                    // call the existing method
            if (e.getResponse() != null && !e.getResponse().isBlank()) {
                sb.append("Response   : ").append(e.getResponse()).append("\n");
                sb.append("ResponderID: ").append(e.getResponderID()).append("\n");
            } else {
                sb.append("Response   : (none)\n");
            }
            sb.append("----------------------------------------\n");
        }
        return sb.toString();
    }
    
    public void setEnquiry(String enquiry) {
        // In a real implementation, this would parse the enquiry string
        // and populate the message map accordingly
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("content", enquiry);
        this.message = messageMap;
    }

    //from interface enquiryhandler

    public void handleEnquiry(int enquiryID) {
        System.out.println("Processing enquiry with ID: " + enquiryID);
        
        if (this.enquiryID == enquiryID) {
            System.out.println("Handling enquiry from sender ID: " + senderID);
            for (Map.Entry<String, String> entry : message.entrySet()) {
                System.out.println("Message content: " + entry.getValue());
            }
        } else {
            System.out.println("Enquiry not found with ID: " + enquiryID);
        }
    }

    public void replyEnquiry(String response) {
        // This method sets the response to an enquiry
        this.response = response;
        System.out.println("Reply: " + response);
    }
}
