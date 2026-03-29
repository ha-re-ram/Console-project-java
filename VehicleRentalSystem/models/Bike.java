package models;

public class Bike extends Vehicle {
    private double securityDepositRequired;

    public Bike(String vehicleId, String name, String numberPlate, double rentalPrice, int count) {
        super(vehicleId, name, numberPlate, "BIKE", rentalPrice, count);
        this.securityDepositRequired = 3000;
    }

    @Override
    public int getServiceLimit() { return 1500; } // service every 1500 kms

    public double getSecurityDepositRequired() { return securityDepositRequired; }
    public void setSecurityDepositRequired(double amt) { this.securityDepositRequired = amt; }
}