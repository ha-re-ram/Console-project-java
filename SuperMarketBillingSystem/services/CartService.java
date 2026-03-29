package services;

import models.*;
import data.DataStore;
import utils.InputHelper;

public class CartService {

    // VIEW all available products (only show in stock items)
    public static void viewProducts() {
        System.out.println("\n--- AVAILABLE PRODUCTS ---");
        boolean found = false;

        for (Product p : DataStore.products) {
            if (p.getQuantity() > 0) {
                p.displayInfo();
                found = true;
            }
        }

        if (!found) System.out.println("No products available right now.");
    }

    // ADD product to cart
    public static void addToCart() {
        System.out.println("\n--- ADD TO CART ---");
        viewProducts();

        String id = InputHelper.getString("\nEnter Product ID: ");
        Product product = InventoryService.findById(id);

        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        if (product.getQuantity() <= 0) {
            System.out.println("Product out of stock.");
            return;
        }

        int qty = InputHelper.getInt("Enter quantity: ");

        if (qty <= 0) {
            System.out.println("Quantity must be at least 1.");
            return;
        }

        if (qty > product.getQuantity()) {
            System.out.println("Only " + product.getQuantity() + " units available.");
            return;
        }

        // Check if product already in cart — if yes just increase quantity
        for (CartItem item : DataStore.currentCart) {
            if (item.getProduct().getProductId().equals(product.getProductId())) {
                int newQty = item.getQuantity() + qty;
                if (newQty > product.getQuantity()) {
                    System.out.println("Cannot add more. Only " +
                        product.getQuantity() + " units in stock.");
                    return;
                }
                item.setQuantity(newQty);
                System.out.println("Updated quantity in cart: " + newQty);
                return;
            }
        }

        // Product not in cart yet — add fresh
        DataStore.currentCart.add(new CartItem(product, qty));
        System.out.println("Added to cart: " + product.getName() + " x" + qty);
    }

    // VIEW current cart
    public static void viewCart() {
        System.out.println("\n--- YOUR CART ---");

        if (DataStore.currentCart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        double total = 0;
        for (CartItem item : DataStore.currentCart) {
            item.displayItem();
            total += item.getSubtotal();
        }

        System.out.println("-----------------------------");
        System.out.println("Cart Total: ₹" + total);
    }

    // EDIT quantity of an item in cart
    public static void editCartItem() {
        System.out.println("\n--- EDIT CART ITEM ---");

        if (DataStore.currentCart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        viewCart();
        String id = InputHelper.getString("\nEnter Product ID to edit: ");

        CartItem target = findInCart(id);
        if (target == null) {
            System.out.println("Product not found in cart.");
            return;
        }

        System.out.println("Current quantity: " + target.getQuantity());
        int newQty = InputHelper.getInt("New quantity (0 to remove): ");

        if (newQty == 0) {
            DataStore.currentCart.remove(target);
            System.out.println("Item removed from cart.");
        } else if (newQty > target.getProduct().getQuantity()) {
            System.out.println("Only " + target.getProduct().getQuantity() +
                " units available in stock.");
        } else {
            target.setQuantity(newQty);
            System.out.println("Quantity updated to: " + newQty);
        }
    }

    // REMOVE an item from cart completely
    public static void removeFromCart() {
        System.out.println("\n--- REMOVE FROM CART ---");

        if (DataStore.currentCart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        viewCart();
        String id = InputHelper.getString("\nEnter Product ID to remove: ");

        CartItem target = findInCart(id);
        if (target == null) {
            System.out.println("Product not in cart.");
            return;
        }

        DataStore.currentCart.remove(target);
        System.out.println("Removed: " + target.getProduct().getName());
    }

    // CLEAR entire cart (used after payment or cancellation)
    public static void clearCart() {
        DataStore.currentCart.clear();
    }

    // CALCULATE cart total
    public static double getCartTotal() {
        double total = 0;
        for (CartItem item : DataStore.currentCart) {
            total += item.getSubtotal();
        }
        return total;
    }

    // HELPER — find item in cart by product ID
    public static CartItem findInCart(String productId) {
        for (CartItem item : DataStore.currentCart) {
            if (item.getProduct().getProductId().equalsIgnoreCase(productId)) {
                return item;
            }
        }
        return null;
    }
}