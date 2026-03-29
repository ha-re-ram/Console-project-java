package auth;


import models.*;
import data.DataStore;
import utils.InputHelper;

public class AuthService {

    // LOGIN — returns the logged in user or null
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

    // ADD ADMIN — only called by existing admin
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

    // ADD CUSTOMER — only called by admin
    public static void addCustomer() {
        System.out.println("\n--- ADD NEW CUSTOMER ---");
        String name = InputHelper.getString("Name: ");
        String email = InputHelper.getString("Email: ");
        String password = InputHelper.getString("Password: ");

        if (emailExists(email)) {
            System.out.println("Email already registered.");
            return;
        }

        Customer newCustomer = new Customer(name, email, password);
        DataStore.users.add(newCustomer);
        System.out.println("Customer added: " + name);
        System.out.println("Preloaded wallet: ₹1000");
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