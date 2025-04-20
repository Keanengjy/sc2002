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

    public void setFlatType(FlatType type) { this.flatType = type; }
    public void setFlatType(String type) {
        if (type.equals("TwoRoom")) this.flatType = FlatType.TwoRoom;
        else if (type.equals("ThreeRoom")) this.flatType = FlatType.ThreeRoom;
    }
    public void decrementUnits() {}
}