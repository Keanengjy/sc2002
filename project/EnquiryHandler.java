package project;

public interface EnquiryHandler {
    void handleEnquiry(int enquiryID) throws EnquiryNotFoundException;
    void replyEnquiry(String reply) throws EnquiryNotFoundException;
}

class EnquiryNotFoundException extends Exception {
    public EnquiryNotFoundException(String message) {
        super(message);
    }
}
