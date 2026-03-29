package auth;

import models.*;
import data.DataStore;
import utils.InputHelper;

public class AuthService {

    // LOGIN
    public static User login() {
        System.out.println("\n--- LOGIN ---");
        String email = InputHelper.getString("Email: ");
        String password = InputHelper.getString("Password: ");

        for (User user : DataStore.users) {
            if (user.getEmail().equalsIgnoreCase(email) &&
                user.getPassword().equals(password)) {
                System.out.println("\nWelcome, " + user.getName() +
                    "! Role: " + user.getRole());
                return user;
            }
        }

        System.out.println("Invalid credentials. Try again.");
        return null;
    }

    // ADD ADMIN — only existing admin can do this
    public static void addAdmin() {
        System.out.println("\n--- ADD NEW ADMIN ---");
        String name = InputHelper.getString("Name: ");
        String email = InputHelper.getString("Email: ");
        String password = InputHelper.getString("Password: ");

        if (emailExists(email)) {
            System.out.println("Email already registered.");
            return;
        }

        DataStore.users.add(new Admin(name, email, password));
        System.out.println("Admin added successfully: " + name);
    }

    // ADD BORROWER — only admin can register borrowers
    public static void addBorrower() {
        System.out.println("\n--- ADD NEW BORROWER ---");
        String name = InputHelper.getString("Name: ");
        String email = InputHelper.getString("Email: ");
        String password = InputHelper.getString("Password: ");

        if (emailExists(email)) {
            System.out.println("Email already registered.");
            return;
        }

        Borrower newBorrower = new Borrower(name, email, password);
        DataStore.users.add(newBorrower);
        System.out.println("Borrower added: " + name);
        System.out.println("Caution deposit preloaded: ₹1500");
        System.out.println("Note: Admin must set security deposit before borrowing.");
    }

    // HELPER — check duplicate email
    private static boolean emailExists(String email) {
        for (User u : DataStore.users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }
}