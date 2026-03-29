package models;

import java.time.LocalDate;
import java.util.ArrayList;

public class Bill {
    private String billNumber;
    private Customer customer;
    private Admin processedBy;
    private ArrayList<CartItem> items;
    private double totalAmount;
    private double discount;
    private double finalAmount;
    private LocalDate billDate;

    public Bill(String billNumber, Customer customer, Admin processedBy,
                ArrayList<CartItem> items, double totalAmount,
                double discount, double finalAmount) {
        this.billNumber = billNumber;
        this.customer = customer;
        this.processedBy = processedBy;
        this.items = items;
        this.totalAmount = totalAmount;
        this.discount = discount;
        this.finalAmount = finalAmount;
        this.billDate = LocalDate.now();
    }

    public String getBillNumber() { return billNumber; }
    public Customer getCustomer() { return customer; }
    public Admin getProcessedBy() { return processedBy; }
    public ArrayList<CartItem> getItems() { return items; }
    public double getTotalAmount() { return totalAmount; }
    public double getDiscount() { return discount; }
    public double getFinalAmount() { return finalAmount; }
    public LocalDate getBillDate() { return billDate; }

    public void printBill() {
        System.out.println("\n=============================");
        System.out.println("       SUPERMARKET BILL      ");
        System.out.println("=============================");
        System.out.println("Bill No  : " + billNumber);
        System.out.println("Date     : " + billDate);
        System.out.println("Customer : " + customer.getName());
        System.out.println("-----------------------------");
        for (CartItem item : items) {
            item.displayItem();
        }
        System.out.println("-----------------------------");
        System.out.println("Total    : ₹" + totalAmount);
        if (discount > 0)
            System.out.println("Discount : -₹" + discount);
        System.out.println("Final    : ₹" + finalAmount);
        System.out.println("=============================");
    }
}