package models;

public class Customer extends User {
    private double creditLimit;    // how much they can spend
    private double walletBalance;  // preloaded credit (starts at 1000)
    private int loyaltyPoints;     // earned by spending

    public Customer(String name, String email, String password) {
        super(name, email, password, "CUSTOMER");
        this.walletBalance = 1000.0; // preloaded as per spec
        this.creditLimit = 1000.0;
        this.loyaltyPoints = 0;
    }

    public double getWalletBalance() { return walletBalance; }
    public double getCreditLimit() { return creditLimit; }
    public int getLoyaltyPoints() { return loyaltyPoints; }

    public void setWalletBalance(double amount) { this.walletBalance = amount; }
    public void setCreditLimit(double limit) { this.creditLimit = limit; }

    public void deductWallet(double amount) { this.walletBalance -= amount; }
    public void addToWallet(double amount) { this.walletBalance += amount; }

    public void addLoyaltyPoints(int points) { this.loyaltyPoints += points; }
    public void resetLoyaltyPoints() { this.loyaltyPoints = 0; }
}