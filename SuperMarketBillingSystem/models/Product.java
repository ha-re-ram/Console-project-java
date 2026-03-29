package models;

public class Product {
    private String productId;
    private String name;
    private double price;
    private int quantity;
    private boolean everBought; // for report — products never bought

    public Product(String productId, String name, double price, int quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.everBought = false;
    }

    public String getProductId() { return productId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public boolean isEverBought() { return everBought; }

    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setEverBought(boolean status) { this.everBought = status; }

    public void reduceQuantity(int qty) { this.quantity -= qty; }
    public void increaseQuantity(int qty) { this.quantity += qty; }

    public void displayInfo() {
        System.out.println("ID: " + productId +
            " | Name: " + name +
            " | Price: ₹" + price +
            " | Stock: " + quantity);
    }
}