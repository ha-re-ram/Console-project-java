package models;

public class Car extends Vehicle {
    private double securityDepositRequired;

    public Car(String vehicleId, String name, String numberPlate, double rentalPrice, int count) {
        super(vehicleId, name, numberPlate, "CAR", rentalPrice, count);
        this.securityDepositRequired = 10000;
    }

    @Override
    public int getServiceLimit() { return 3000; } // service every 3000 kms

    public double getSecurityDepositRequired() { return securityDepositRequired; }
    public void setSecurityDepositRequired(double amt) { this.securityDepositRequired = amt; }
}