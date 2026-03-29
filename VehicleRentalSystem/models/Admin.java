package models;

public class Admin extends User {

    public Admin(String name, String email, String password) {
        super(name, email, password, "ADMIN");
        // super() calls the parent (User) constructor
    }
}