package app;

import person.AbstractUser;
import project.HDBFlat;

import java.util.List;

public class Application {
    private AbstractUser currentUser;
    private List<String> enquiryQueue;
    private List<HDBFlat> flatBookingRequests;

    public Application(AbstractUser currentUser) {
        this.currentUser = currentUser;
    }

    public void routeEnquiry(String enquiry) {
        enquiryQueue.add(enquiry);
    }

    public void processBookingRequest(HDBFlat flat) {
        flatBookingRequests.add(flat);
    }

    public List<HDBFlat> getAvailableOptions() {
        return flatBookingRequests;
    }
} {
    
}
