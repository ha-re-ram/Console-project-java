package models;

import java.time.LocalDate;

public class Rental {
    private String rentalId;
    private Borrower borrower;
    private Vehicle vehicle;
    private LocalDate rentalDate;
    private LocalDate dueDate;
    private boolean returned;
    private int extensionCount; // max 2
    private double kmsDriven;
    private String status; // "ACTIVE", "RETURNED", "LOST"

    public Rental(String rentalId, Borrower borrower, Vehicle vehicle, LocalDate rentalDate) {
        this.rentalId = rentalId;
        this.borrower = borrower;
        this.vehicle = vehicle;
        this.rentalDate = rentalDate;
        this.dueDate = rentalDate; // same day return by default
        this.returned = false;
        this.extensionCount = 0;
        this.kmsDriven = 0;
        this.status = "ACTIVE";
    }

    // Getters
    public String getRentalId() { return rentalId; }
    public Borrower getBorrower() { return borrower; }
    public Vehicle getVehicle() { return vehicle; }
    public LocalDate getRentalDate() { return rentalDate; }
    public LocalDate getDueDate() { return dueDate; }
    public boolean isReturned() { return returned; }
    public int getExtensionCount() { return extensionCount; }
    public double getKmsDriven() { return kmsDriven; }
    public String getStatus() { return status; }

    // Setters
    public void setReturned(boolean r) { this.returned = r; }
    public void setKmsDriven(double kms) { this.kmsDriven = kms; }
    public void setStatus(String s) { this.status = s; }

    public boolean extendDueDate() {
        if (extensionCount < 2) {
            dueDate = dueDate.plusDays(1);
            extensionCount++;
            return true;
        }
        return false; // can't extend more than twice
    }
}
