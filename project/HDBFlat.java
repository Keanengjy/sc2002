package project;

import java.util.Map;
import java.util.Objects;

import person.HDBManager;
import person.HDBOfficer;
import person.Applicant;
import person.Application;
import person.MaritalStatus;
import project.UserRole;
import project.FlatType;
import project.ApplicationStatus;

public class HDBFlat {
    private FlatType flatType;
    private double sellingPrice;
    private String location;
    private boolean isBooked;

    public HDBFlat(FlatType flatType, double sellingPrice, String location) {
        this.flatType = flatType;
        this.sellingPrice = sellingPrice;
        this.location = location;
        this.isBooked = false; // Default to not booked
    }

    public FlatType getFlatType() {
        return flatType;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public String getLocation() {
        return location;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setFlatType(String type) {
        if (type.equals("TwoRoom"))
            this.flatType = FlatType.TwoRoom;
        else if (type.equals("ThreeRoom"))
            this.flatType = FlatType.ThreeRoom;
    }

    public void setSellingPrice(double price) {
        this.sellingPrice = price;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setBooked(boolean booked) {
        this.isBooked = booked;
    }
    
    public static boolean decrementFlat(Project project, FlatType flatType, Map<HDBFlat, Integer>availableFlats, Application app) {
        /* 1 ─ locate a matching HDBFlat key ---------------------------- */
        HDBFlat match = null;
        for (HDBFlat f : availableFlats.keySet()) {
            if (f.getFlatType() == flatType) {
                match = f;
                break;
            }
        }
        if (match == null) {
            return false;
        }

        /* 2 ─ check remaining quantity -------------------------------- */
        int remaining = availableFlats.get(match);
        if (remaining <= 0) {
            return false;                              // none left
        }

        /* 3 ─ decrement and update map -------------------------------- */
        if (remaining == 1) {
            availableFlats.remove(match);              // last unit → remove entry
        } else {
            availableFlats.put(match, remaining - 1);  // otherwise minus‑1
        }

        /* 4 ─ attach flat to the application -------------------------- */
        app.setSelectedFlat(match);

        return true;                                   // booking succeeded
            }

            public void resetBooking() {
                isBooked = false;
            }
}