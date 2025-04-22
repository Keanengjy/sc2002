package project;

enum FlatType {
    TwoRoom,
    ThreeRoom
}


public class HDBFlat {
    private FlatType flatType;
    private int unitNo;
    private String description;
    private double sellingPrice;
    private String location;
    private boolean isBooked;


    public HDBFlat(FlatType flatType, int unitNo, String description, double sellingPrice, String location) {
        this.flatType = flatType;
        this.unitNo = unitNo;
        this.description = description;
        this.sellingPrice = sellingPrice;
        this.location = location;
        this.isBooked = false; // Default to not booked
    }

    public FlatType getFlatType() { return flatType; }
    public int getUnitNo() { return unitNo; }
    public String getDescription() { return description; }
    public double getSellingPrice() { return sellingPrice; }
    public String getLocation() { return location; }
    public boolean isBooked() { return isBooked; }


    public void setFlatType(String type) {
        if (type.equals("TwoRoom")) this.flatType = FlatType.TwoRoom;
        else if (type.equals("ThreeRoom")) this.flatType = FlatType.ThreeRoom;
    }
    public void setUnitNo(int unitNo) { this.unitNo = unitNo; }
    public void setDescription(String description) { this.description = description; }
    public void setSellingPrice(double price) { this.sellingPrice = price; }
    public void setLocation(String location) { this.location = location; }
    public void setBooked(boolean booked) { this.isBooked = booked; }


    public void decrementUnits() {}
}