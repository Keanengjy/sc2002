package project;

import java.util.HashMap;
import java.util.Map;

public class Enquiry {
    private Map<String, String> message;
    private int enquiryID;
    private int senderID;
    private String response;
    private int responderID;

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
    public void setEnquiryID(int enquiryID) {
        this.enquiryID = enquiryID;
    }

    public int getSenderID() {
        return this.senderID;
    }
    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

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
    }
}