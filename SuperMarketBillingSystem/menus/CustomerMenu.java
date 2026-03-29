package menus;

import models.*;
import services.*;
import utils.InputHelper;

public class CustomerMenu {

    public static void show(Customer customer) {
        while (true) {
            System.out.println("\n=================================");
            System.out.println(" CUSTOMER MENU — " + customer.getName());
            System.out.println("=================================");
            System.out.println("Wallet: ₹" + customer.getWalletBalance() +
                " | Points: " + customer.getLoyaltyPoints());
            System.out.println("---------------------------------");
            System.out.println("--- Shopping ---");
            System.out.println("1. View Products");
            System.out.println("2. Add to Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Edit Cart Item");
            System.out.println("5. Remove from Cart");
            System.out.println("6. Checkout");
            System.out.println("--- History ---");
            System.out.println("7. Purchase History");
            System.out.println("---------------------------------");
            System.out.println("8. Logout");

            int choice = InputHelper.getInt("Choose: ");

            switch (choice) {
                case 1: CartService.viewProducts(); break;
                case 2: CartService.addToCart(); break;
                case 3: CartService.viewCart(); break;
                case 4: CartService.editCartItem(); break;
                case 5: CartService.removeFromCart(); break;
                case 6:
                    // Checkout needs to know which admin is serving
                    // Since this is console app, we just pass the first admin
                    // In real apps this would be the logged-in admin at counter
                    Admin servingAdmin = getFirstAdmin();
                    if (servingAdmin != null) {
                        PaymentService.checkout(customer, servingAdmin);
                    } else {
                        System.out.println("No admin available for checkout.");
                    }
                    break;
                case 7: ReportService.purchaseHistory(customer); break;
                case 8:
                    CartService.clearCart(); // clear cart on logout
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // Get first available admin for billing
    private static Admin getFirstAdmin() {
        for (models.User u : data.DataStore.users) {
            if (u instanceof Admin) return (Admin) u;
        }
        return null;
    }
}