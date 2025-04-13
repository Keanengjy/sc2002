package person;

public class Enquiry {
    private String message;
    private int enquiryID;
    private String senderID;
    private String response;
    private int responderID;
    
    /**
     * Constructs a new Enquiry with the specified parameters.
     * 
     * @param message The enquiry message
     * @param enquiryID The ID of the enquiry
     * @param senderID The ID of the sender
     */
    public Enquiry(String message, int enquiryID, String senderID) {
        this.message = message;
        this.enquiryID = enquiryID;
        this.senderID = senderID;
        this.response = "";
        this.responderID = -1; // No responder yet
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public int getEnquiryID() {
        return enquiryID;
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
    
    /**
     * Gets the enquiry details.
     * 
     * @return The enquiry details
     */
    public String getEnquiry() {
        StringBuilder enquiryDetails = new StringBuilder();
        enquiryDetails.append("Enquiry ID: ").append(enquiryID).append("\n");
        enquiryDetails.append("Sender ID: ").append(senderID).append("\n");
        enquiryDetails.append("Message: ").append(message).append("\n");
        
        if (responderID != -1) {
            enquiryDetails.append("Response: ").append(response).append("\n");
            enquiryDetails.append("Responder ID: ").append(responderID).append("\n");
        } else {
            enquiryDetails.append("Status: Pending response\n");
        }
        
        return enquiryDetails.toString();
    }
}
