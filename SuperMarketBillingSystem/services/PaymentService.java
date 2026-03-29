package services;

import models.*;
import data.DataStore;
import utils.InputHelper;
import java.util.ArrayList;

public class PaymentService {

    public static void checkout(Customer customer, Admin admin) {
        System.out.println("\n--- CHECKOUT ---");

        // Step 1 — check cart is not empty
        if (DataStore.currentCart.isEmpty()) {
            System.out.println("Your cart is empty. Add products first.");
            return;
        }

        // Step 2 — calculate raw total
        double total = CartService.getCartTotal();
        double discount = 0;

        System.out.println("Cart Total: ₹" + total);
        System.out.println("Wallet Balance: ₹" + customer.getWalletBalance());

        // Step 3 — check loyalty points redemption first
        // If customer has 50+ points, apply ₹100 discount
        if (customer.getLoyaltyPoints() >= 50) {
            System.out.println("\n🎉 You have " + customer.getLoyaltyPoints() +
                " loyalty points!");
            System.out.println("You qualify for a ₹100 discount.");
            System.out.println("Apply discount? (YES/NO): ");
            String apply = InputHelper.getString("").toUpperCase();

            if (apply.equals("YES")) {
                discount += 100;
                customer.resetLoyaltyPoints();
                System.out.println("₹100 loyalty discount applied. Points reset to 0.");
            }
        }

        // Step 4 — calculate final amount after discount
        double finalAmount = total - discount;

        // Step 5 — check if customer has enough wallet balance
        if (customer.getWalletBalance() < finalAmount) {
            System.out.println("\n❌ Insufficient wallet balance.");
            System.out.println("Required : ₹" + finalAmount);
            System.out.println("Available: ₹" + customer.getWalletBalance());
            System.out.println("Please ask admin to increase your credit limit.");
            return;
        }

        // Step 6 — confirm payment
        System.out.println("\n--- PAYMENT SUMMARY ---");
        System.out.println("Total    : ₹" + total);
        if (discount > 0)
            System.out.println("Discount : -₹" + discount);
        System.out.println("Final    : ₹" + finalAmount);
        System.out.println("Wallet   : ₹" + customer.getWalletBalance());

        String confirm = InputHelper.getString("\nConfirm payment? (YES/NO): ").toUpperCase();
        if (!confirm.equals("YES")) {
            System.out.println("Payment cancelled.");
            return;
        }

        // Step 7 — deduct wallet
        customer.deductWallet(finalAmount);

        // Step 8 — apply rewards
        applyRewards(customer, total);

        // Step 9 — reduce product stock
        for (CartItem item : DataStore.currentCart) {
            item.getProduct().reduceQuantity(item.getQuantity());
            item.getProduct().setEverBought(true); // mark as purchased
        }

        // Step 10 — generate bill
        String billNo = "BILL" + DataStore.billCounter++;
        ArrayList<CartItem> snapshot = new ArrayList<>(DataStore.currentCart);

        Bill bill = new Bill(billNo, customer, admin,
                            snapshot, total, discount, finalAmount);
        DataStore.bills.add(bill);

        // Step 11 — increment admin sales count
        admin.incrementSales();

        // Step 12 — print bill and clear cart
        bill.printBill();
        CartService.clearCart();

        System.out.println("\nWallet balance after payment: ₹" + customer.getWalletBalance());
        System.out.println("Loyalty points: " + customer.getLoyaltyPoints());
    }

    // ─── REWARD LOGIC ─────────────────────────────────────────

    private static void applyRewards(Customer customer, double total) {

        // Rule 1 — Big bill: ₹5000+ gets ₹100 cashback, NO loyalty points
        if (total >= 5000) {
            customer.addToWallet(100);
            System.out.println("\n🎉 Big bill reward! ₹100 added to your wallet.");
            System.out.println("No loyalty points added for bills above ₹5000.");
            return; // stop here — don't add loyalty points
        }

        // Rule 2 — Loyalty points: 1 point per ₹100 spent
        int pointsEarned = (int) (total / 100);
        customer.addLoyaltyPoints(pointsEarned);
        System.out.println("\n+" + pointsEarned + " loyalty points earned.");
        System.out.println("Total loyalty points: " + customer.getLoyaltyPoints());
    }
}