import models.*;
import data.DataStore;
import utils.InputHelper;

public class AuthService {

    // Returns the logged-in User object, or null if failed
    public static User login() {
        System.out.println("\n--- LOGIN ---");
        String email = InputHelper.getString("Email: ");
        String password = InputHelper.getString("Password: ");

        for (User user : DataStore.users) {
            if (user.getEmail().equalsIgnoreCase(email) &&
                user.getPassword().equals(password)) {
                System.out.println("\nWelcome, " + user.getName() + "! Role: " + user.getRole());
                return user;
            }
        }

        System.out.println("Invalid credentials. Try again.");
        return null;
    }

    public static void signup() {
        System.out.println("\n--- SIGNUP ---");
        String name = InputHelper.getString("Full Name: ");
        String email = InputHelper.getString("Email: ");
        String password = InputHelper.getString("Password: ");

        // Check if email already exists
        for (User user : DataStore.users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                System.out.println("Email already registered. Please login.");
                return;
            }
        }

        Borrower newUser = new Borrower(name, email, password);
        DataStore.users.add(newUser);
        DataStore.saveToDisk(); // Save immediately after signup
        System.out.println("Account created! You can now login.");
        System.out.println("Note: Default caution deposit of ₹30,000 has been applied.");
    }
}