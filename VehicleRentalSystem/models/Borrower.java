package models;

public class Borrower extends User {
    private double securityDeposit;
    private double cautionDeposit; // starts at 30000 as per spec

    public Borrower(String name, String email, String password) {
        super(name, email, password, "BORROWER");
        this.cautionDeposit = 30000.0;
        this.securityDeposit = 0.0;
    }

    public double getCautionDeposit() { return cautionDeposit; }
    public double getSecurityDeposit() { return securityDeposit; }

    public void setSecurityDeposit(double amount) {
        this.securityDeposit = amount;
    }

    public void deductCaution(double amount) {
        this.cautionDeposit -= amount;
    }
}