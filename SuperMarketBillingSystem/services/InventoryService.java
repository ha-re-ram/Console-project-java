package services;

import models.*;
import data.DataStore;
import utils.InputHelper;
import java.util.ArrayList;
import java.util.Comparator;

public class InventoryService {

    // ADD a product
    public static void addProduct() {
        System.out.println("\n--- ADD PRODUCT ---");
        String id = InputHelper.getString("Product ID (e.g. P006): ");

        // Check duplicate ID
        for (Product p : DataStore.products) {
            if (p.getProductId().equalsIgnoreCase(id)) {
                System.out.println("Product ID already exists.");
                return;
            }
        }

        String name = InputHelper.getString("Product Name: ");
        double price = InputHelper.getDouble("Price: ₹");
        int qty = InputHelper.getInt("Quantity: ");

        DataStore.products.add(new Product(id, name, price, qty));
        System.out.println("Product added: " + name);
    }

    // MODIFY a product
    public static void modifyProduct() {
        System.out.println("\n--- MODIFY PRODUCT ---");
        String id = InputHelper.getString("Enter Product ID to modify: ");

        Product target = findById(id);
        if (target == null) {
            System.out.println("Product not found.");
            return;
        }

        System.out.println("Found: " + target.getName());
        System.out.println("What to modify?");
        System.out.println("1. Name");
        System.out.println("2. Price");
        System.out.println("3. Quantity");
        int choice = InputHelper.getInt("Choose: ");

        switch (choice) {
            case 1:
                String newName = InputHelper.getString("New Name: ");
                // We cant set name directly since there's no setter
                // So we replace the product with updated values
                int idx = DataStore.products.indexOf(target);
                DataStore.products.set(idx, new Product(
                    target.getProductId(),
                    newName,
                    target.getPrice(),
                    target.getQuantity()
                ));
                System.out.println("Name updated.");
                break;

            case 2:
                double newPrice = InputHelper.getDouble("New Price: ₹");
                target.setPrice(newPrice);
                System.out.println("Price updated.");
                break;

            case 3:
                int newQty = InputHelper.getInt("New Quantity: ");
                target.setQuantity(newQty);
                System.out.println("Quantity updated.");
                break;

            default:
                System.out.println("Invalid choice.");
        }
    }

    // DELETE a product
    public static void deleteProduct() {
        System.out.println("\n--- DELETE PRODUCT ---");
        String id = InputHelper.getString("Enter Product ID to delete: ");

        Product target = findById(id);
        if (target == null) {
            System.out.println("Product not found.");
            return;
        }

        DataStore.products.remove(target);
        System.out.println("Product deleted: " + target.getName());
    }

    // VIEW all products sorted
    public static void viewAllProducts() {
        System.out.println("\n--- ALL PRODUCTS ---");
        System.out.println("Sort by: 1. Name   2. Price");
        int choice = InputHelper.getInt("Choose: ");

        ArrayList<Product> list = new ArrayList<>(DataStore.products);

        if (choice == 1) {
            list.sort(Comparator.comparing(Product::getName));
        } else {
            list.sort(Comparator.comparingDouble(Product::getPrice));
        }

        if (list.isEmpty()) {
            System.out.println("No products found.");
            return;
        }

        for (Product p : list) {
            p.displayInfo();
        }
    }

    // SEARCH product by name
    public static void searchProduct() {
        System.out.println("\n--- SEARCH PRODUCT ---");
        String keyword = InputHelper.getString("Enter product name: ").toLowerCase();

        boolean found = false;
        for (Product p : DataStore.products) {
            if (p.getName().toLowerCase().contains(keyword)) {
                p.displayInfo();
                found = true;
            }
        }

        if (!found) System.out.println("No products matched.");
    }

    // INCREASE customer credit limit
    public static void increaseCustomerCredit() {
        System.out.println("\n--- INCREASE CUSTOMER CREDIT ---");
        String email = InputHelper.getString("Enter customer email: ");

        Customer target = findCustomer(email);
        if (target == null) {
            System.out.println("Customer not found.");
            return;
        }

        System.out.println("Current credit limit: ₹" + target.getCreditLimit());
        double increase = InputHelper.getDouble("Increase amount: ₹");
        target.setCreditLimit(target.getCreditLimit() + increase);
        target.addToWallet(increase);

        System.out.println("Credit updated.");
        System.out.println("New credit limit : ₹" + target.getCreditLimit());
        System.out.println("New wallet balance: ₹" + target.getWalletBalance());
    }

    // ─── HELPERS ──────────────────────────────────────────────

    // Find product by ID
    public static Product findById(String id) {
        for (Product p : DataStore.products) {
            if (p.getProductId().equalsIgnoreCase(id)) {
                return p;
            }
        }
        return null;
    }

    // Find product by name (exact, for cart use)
    public static Product findByName(String name) {
        for (Product p : DataStore.products) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    // Find customer by email
    public static Customer findCustomer(String email) {
        for (User u : DataStore.users) {
            if (u instanceof Customer &&
                u.getEmail().equalsIgnoreCase(email)) {
                return (Customer) u;
            }
        }
        return null;
    }
}