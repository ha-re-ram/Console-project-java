package models;

public class Borrower extends User {
    private double securityDeposit;   // minimum 500 required to borrow
    private double cautionDeposit;    // starts at 1500, fines deducted here
    private boolean membershipCardLost; // tracks if card is lost
    private int activeBorrowCount;    // max 3 at a time

    public Borrower(String name, String email, String password) {
        super(name, email, password, "BORROWER");
        this.securityDeposit = 0.0;
        this.cautionDeposit = 1500.0; // preloaded as per spec
        this.membershipCardLost = false;
        this.activeBorrowCount = 0;
    }

    public double getSecurityDeposit() { return securityDeposit; }
    public double getCautionDeposit() { return cautionDeposit; }
    public boolean isMembershipCardLost() { return membershipCardLost; }
    public int getActiveBorrowCount() { return activeBorrowCount; }

    public void setSecurityDeposit(double amount) { this.securityDeposit = amount; }
    public void setMembershipCardLost(boolean status) { this.membershipCardLost = status; }

    public void deductCaution(double amount) { this.cautionDeposit -= amount; }
    public void addCaution(double amount) { this.cautionDeposit += amount; }

    public void incrementBorrowCount() { this.activeBorrowCount++; }
    public void decrementBorrowCount() {
        if (this.activeBorrowCount > 0) this.activeBorrowCount--;
    }
}