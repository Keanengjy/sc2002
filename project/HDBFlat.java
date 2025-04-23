package project;

import java.util.Map;

import person.HDBManager;
import person.HDBOfficer;
import person.Applicant;

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

    public static boolean decrementUnits(Project project, FlatType type) {
        Map<FlatType,Integer> stock = project.getFlats(); // the EnumMap you seeded
        int remaining = stock.getOrDefault(type, 0);
        if (remaining > 0) {
            stock.put(type, remaining - 1);
            return true;        // booking succeeded
        }
        return false;           // no units left
    }
    

    public void resetBooking() {
        isBooked = false;
    }
}