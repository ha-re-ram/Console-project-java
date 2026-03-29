package models;

public class Vehicle {
    protected String vehicleId;
    protected String name;
    protected String numberPlate;
    protected String type; // "CAR" or "BIKE"
    protected double rentalPrice;
    protected int availableCount;
    protected double totalKms;
    protected boolean isAvailable;

    public Vehicle(String vehicleId, String name, String numberPlate,
                   String type, double rentalPrice, int availableCount) {
        this.vehicleId = vehicleId;
        this.name = name;
        this.numberPlate = numberPlate;
        this.type = type;
        this.rentalPrice = rentalPrice;
        this.availableCount = availableCount;
        this.totalKms = 0;
        this.isAvailable = true;
    }

    // Getters
    public String getVehicleId() { return vehicleId; }
    public String getName() { return name; }
    public String getNumberPlate() { return numberPlate; }
    public String getType() { return type; }
    public double getRentalPrice() { return rentalPrice; }
    public int getAvailableCount() { return availableCount; }
    public double getTotalKms() { return totalKms; }
    public boolean isAvailable() { return isAvailable; }

    // Setters
    public void setAvailableCount(int count) { this.availableCount = count; }
    public void setRentalPrice(double price) { this.rentalPrice = price; }
    public void addKms(double kms) { this.totalKms += kms; }
    public void setAvailable(boolean status) { this.isAvailable = status; }

    // Each vehicle type has a different service limit — child class will define this
    public int getServiceLimit() { return 0; }

    public void displayInfo() {
        System.out.println("ID: " + vehicleId + " | Name: " + name +
            " | Plate: " + numberPlate + " | Price: ₹" + rentalPrice +
            "/day | Available: " + availableCount + " | KMs: " + totalKms);
    }
}