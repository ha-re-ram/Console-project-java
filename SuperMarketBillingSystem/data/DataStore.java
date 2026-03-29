package data;

import models.*;
import java.util.ArrayList;

public class DataStore {
    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<Product> products = new ArrayList<>();
    public static ArrayList<Bill> bills = new ArrayList<>();
    public static ArrayList<CartItem> currentCart = new ArrayList<>();
    public static int billCounter = 1;

    public static void init() {
        // Default admin
        users.add(new Admin("Admin", "admin@market.com", "admin123"));

        // Default customers
        Customer c1 = new Customer("Ha-re-Ram", "hareram@gmail.com", "pass123");
        users.add(c1);

        // Default products
        products.add(new Product("P001", "Rice (1kg)", 60.0, 100));
        products.add(new Product("P002", "Sugar (1kg)", 45.0, 80));
        products.add(new Product("P003", "Milk (1L)", 25.0, 50));
        products.add(new Product("P004", "Bread", 35.0, 40));
        products.add(new Product("P005", "Eggs (12pcs)", 80.0, 60));
    }
}