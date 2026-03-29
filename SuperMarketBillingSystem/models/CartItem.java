package models;

public class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getSubtotal() {
        return product.getPrice() * quantity;
    }

    public void displayItem() {
        System.out.println("Product: " + product.getName() +
            " | Qty: " + quantity +
            " | Subtotal: ₹" + getSubtotal());
    }
}