package models;

public class Admin extends User {
    private int totalSales; // tracks how many bills this admin processed

    public Admin(String name, String email, String password) {
        super(name, email, password, "ADMIN");
        this.totalSales = 0;
    }

    public int getTotalSales() { return totalSales; }
    public void incrementSales() { totalSales++; }
}