package models;

public class Admin extends User {
    private int totalBooksManaged; // for reports — which admin handled most

    public Admin(String name, String email, String password) {
        super(name, email, password, "ADMIN");
        this.totalBooksManaged = 0;
    }

    public int getTotalBooksManaged() { return totalBooksManaged; }
    public void incrementBooksManaged() { totalBooksManaged++; }
}