package models;

public class Book {
    private String bookId;
    private String title;
    private String author;
    private String isbn;
    private double cost;          // needed for lost book fine calculation
    private int totalQuantity;
    private int availableQuantity;
    private int borrowCount;      // for "heavily borrowed" report
    private boolean everBorrowed; // for "never borrowed" report

    public Book(String bookId, String title, String author,
                String isbn, double cost, int quantity) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.cost = cost;
        this.totalQuantity = quantity;
        this.availableQuantity = quantity;
        this.borrowCount = 0;
        this.everBorrowed = false;
    }

    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public double getCost() { return cost; }
    public int getTotalQuantity() { return totalQuantity; }
    public int getAvailableQuantity() { return availableQuantity; }
    public int getBorrowCount() { return borrowCount; }
    public boolean isEverBorrowed() { return everBorrowed; }

    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setCost(double cost) { this.cost = cost; }
    public void setAvailableQuantity(int qty) { this.availableQuantity = qty; }
    public void setTotalQuantity(int qty) { this.totalQuantity = qty; }

    public void reduceAvailable() {
        this.availableQuantity--;
        this.borrowCount++;
        this.everBorrowed = true;
    }

    public void increaseAvailable() { this.availableQuantity++; }

    public void displayInfo() {
        System.out.println("ID: " + bookId +
            " | Title: " + title +
            " | Author: " + author +
            " | ISBN: " + isbn +
            " | Cost: ₹" + cost +
            " | Available: " + availableQuantity + "/" + totalQuantity);
    }
}