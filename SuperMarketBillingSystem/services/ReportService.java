package services;

import models.*;
import data.DataStore;
import java.util.ArrayList;
import java.util.Comparator;

public class ReportService {

    // ─── MODULE E — CUSTOMER REPORTS ──────────────────────────

    // Purchase history for a specific customer
    public static void purchaseHistory(Customer customer) {
        System.out.println("\n--- YOUR PURCHASE HISTORY ---");
        boolean found = false;

        for (Bill bill : DataStore.bills) {
            if (bill.getCustomer().getEmail().equals(customer.getEmail())) {
                System.out.println("\nBill No  : " + bill.getBillNumber());
                System.out.println("Date     : " + bill.getBillDate());
                System.out.println("Items    :");
                for (CartItem item : bill.getItems()) {
                    System.out.println("  - " + item.getProduct().getName() +
                        " x" + item.getQuantity() +
                        " = ₹" + item.getSubtotal());
                }
                System.out.println("Total    : ₹" + bill.getTotalAmount());
                if (bill.getDiscount() > 0)
                    System.out.println("Discount : -₹" + bill.getDiscount());
                System.out.println("Paid     : ₹" + bill.getFinalAmount());
                System.out.println("-----------------------------");
                found = true;
            }
        }

        if (!found) System.out.println("No purchase history found.");
    }

    // ─── MODULE F — ADMIN REPORTS ─────────────────────────────

    // REPORT 1 — Low stock products (quantity < 10)
    public static void lowStockProducts() {
        System.out.println("\n--- LOW STOCK PRODUCTS ---");
        boolean found = false;

        for (Product p : DataStore.products) {
            if (p.getQuantity() < 10) {
                System.out.println("⚠️  " + p.getName() +
                    " | Stock: " + p.getQuantity() +
                    " | Price: ₹" + p.getPrice());
                found = true;
            }
        }

        if (!found) System.out.println("All products are well stocked.");
    }

    // REPORT 2 — Products never bought by anyone
    public static void productsNeverBought() {
        System.out.println("\n--- PRODUCTS NEVER BOUGHT ---");
        boolean found = false;

        for (Product p : DataStore.products) {
            if (!p.isEverBought()) {
                p.displayInfo();
                found = true;
            }
        }

        if (!found) System.out.println("All products have been purchased at least once.");
    }

    // REPORT 3 — Customers sorted by total spending
    public static void topSpendingCustomers() {
        System.out.println("\n--- TOP SPENDING CUSTOMERS ---");

        // Build a list of customers with their total spend
        ArrayList<Customer> customers = new ArrayList<>();
        for (User u : DataStore.users) {
            if (u instanceof Customer) {
                customers.add((Customer) u);
            }
        }

        if (customers.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }

        // Calculate total spend per customer
        // We sort by total amount spent across all their bills
        customers.sort((a, b) -> {
            double spendA = getTotalSpend(a);
            double spendB = getTotalSpend(b);
            return Double.compare(spendB, spendA); // descending
        });

        for (Customer c : customers) {
            double spend = getTotalSpend(c);
            System.out.println("Customer : " + c.getName() +
                " | Email: " + c.getEmail() +
                " | Total Spent: ₹" + spend);
        }
    }

    // REPORT 4 — Admins sorted by total sales processed
    public static void adminSalesReport() {
        System.out.println("\n--- ADMIN SALES REPORT ---");

        ArrayList<Admin> admins = new ArrayList<>();
        for (User u : DataStore.users) {
            if (u instanceof Admin) {
                admins.add((Admin) u);
            }
        }

        if (admins.isEmpty()) {
            System.out.println("No admins found.");
            return;
        }

        // Sort by total sales descending
        admins.sort((a, b) -> Integer.compare(b.getTotalSales(), a.getTotalSales()));

        for (Admin a : admins) {
            System.out.println("Admin  : " + a.getName() +
                " | Email: " + a.getEmail() +
                " | Sales Processed: " + a.getTotalSales());
        }
    }

    // ─── HELPER ───────────────────────────────────────────────

    // Calculate total amount spent by a customer across all bills
    private static double getTotalSpend(Customer customer) {
        double total = 0;
        for (Bill bill : DataStore.bills) {
            if (bill.getCustomer().getEmail().equals(customer.getEmail())) {
                total += bill.getFinalAmount();
            }
        }
        return total;
    }
}