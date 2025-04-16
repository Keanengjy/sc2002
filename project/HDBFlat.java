package project;

enum FlatType {
    TWO_ROOM,
    THREE_ROOM
}

class FlatUnavailableException extends Exception {
    public FlatUnavailableException(String message) {
        super(message);
    }
}

public class HDBFlat {
    private FlatType flatType;
    private int unitNo;
    private String description;
    private double sellingPrice;
    private boolean isBooked;
    private String postalCode;

    /**
     * Constructs a new HDBFlat with the specified parameters.
     */
    public HDBFlat(FlatType flatType, int unitNo, String description, double sellingPrice, String postalCode) {
        this.flatType = flatType;
        this.unitNo = unitNo;
        this.description = description;
        this.sellingPrice = sellingPrice;
        this.isBooked = false;
        this.postalCode = postalCode;
    }

    // Getters and setters
    public FlatType getFlatType() {
        return flatType;
    }

    public void setFlatType(FlatType flatType) {
        this.flatType = flatType;
    }

    public int getUnitNo() {
        return unitNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Generates a receipt for booking this flat.
     */
    public String generateReceipt() {
        return "BOOKING RECEIPT\n" +
               "Flat Type: " + flatType + "\n" +
               "Unit Number: " + unitNo + "\n" +
               "Postal Code: " + postalCode + "\n" +
               "Selling Price: $" + String.format("%.2f", sellingPrice) + "\n" +
               "Date: " + java.time.LocalDate.now() + "\n" +
               "Time: " + java.time.LocalTime.now();
    }
}
