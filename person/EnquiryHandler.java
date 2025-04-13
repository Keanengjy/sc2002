package person;

interface EnquiryHandler {
    /**
     * Handles an enquiry based on its ID.
     * 
     * @param enquiryID The ID of the enquiry to handle
     */
    void handleEnquiry(int enquiryID) throws EnquiryNotFoundException;
    
    /**
     * Replies to an enquiry.
     * 
     * @param reply The reply message
     */
    void replyEnquiry(String reply) throws EnquiryNotFoundException;
}

class EnquiryNotFoundException extends Exception {
    public EnquiryNotFoundException(String message) {
        super(message);
    }
}
