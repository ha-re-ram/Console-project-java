package services;

import models.*;
import data.DataStore;
import utils.InputHelper;
import java.util.ArrayList;
import java.util.Comparator;

public class BookService {

    // ADD a book
    public static void addBook() {
        System.out.println("\n--- ADD BOOK ---");
        String id = InputHelper.getString("Book ID (e.g. B006): ");

        // Check duplicate ID
        for (Book b : DataStore.books) {
            if (b.getBookId().equalsIgnoreCase(id)) {
                System.out.println("Book ID already exists.");
                return;
            }
        }

        String title = InputHelper.getString("Title: ");
        String author = InputHelper.getString("Author: ");
        String isbn = InputHelper.getString("ISBN: ");
        double cost = InputHelper.getDouble("Cost: ₹");
        int qty = InputHelper.getInt("Quantity: ");

        DataStore.books.add(new Book(id, title, author, isbn, cost, qty));
        System.out.println("Book added: " + title);
    }

    // MODIFY a book
    public static void modifyBook() {
        System.out.println("\n--- MODIFY BOOK ---");
        String input = InputHelper.getString("Enter Book ID or ISBN: ");

        Book target = findBook(input);
        if (target == null) {
            System.out.println("Book not found.");
            return;
        }

        System.out.println("Found: " + target.getTitle());
        System.out.println("What to modify?");
        System.out.println("1. Title");
        System.out.println("2. Author");
        System.out.println("3. Available Quantity");
        System.out.println("4. Cost");
        int choice = InputHelper.getInt("Choose: ");

        switch (choice) {
            case 1:
                String newTitle = InputHelper.getString("New Title: ");
                target.setTitle(newTitle);
                System.out.println("Title updated.");
                break;

            case 2:
                String newAuthor = InputHelper.getString("New Author: ");
                target.setAuthor(newAuthor);
                System.out.println("Author updated.");
                break;

            case 3:
                int newQty = InputHelper.getInt("New Available Quantity: ");
                target.setAvailableQuantity(newQty);
                System.out.println("Quantity updated.");
                break;

            case 4:
                double newCost = InputHelper.getDouble("New Cost: ₹");
                target.setCost(newCost);
                System.out.println("Cost updated.");
                break;

            default:
                System.out.println("Invalid choice.");
        }
    }

    // DELETE a book
    public static void deleteBook() {
        System.out.println("\n--- DELETE BOOK ---");
        String input = InputHelper.getString("Enter Book ID or ISBN: ");

        Book target = findBook(input);
        if (target == null) {
            System.out.println("Book not found.");
            return;
        }

        // Don't delete if copies are currently borrowed
        if (target.getAvailableQuantity() < target.getTotalQuantity()) {
            System.out.println("Cannot delete. Some copies are currently borrowed.");
            return;
        }

        DataStore.books.remove(target);
        System.out.println("Book deleted: " + target.getTitle());
    }

    // VIEW all books sorted
    public static void viewAllBooks() {
        System.out.println("\n--- ALL BOOKS ---");
        System.out.println("Sort by: 1. Title   2. Available Quantity");
        int choice = InputHelper.getInt("Choose: ");

        ArrayList<Book> list = new ArrayList<>(DataStore.books);

        if (choice == 1) {
            list.sort(Comparator.comparing(Book::getTitle));
        } else {
            list.sort(Comparator.comparingInt(Book::getAvailableQuantity).reversed());
        }

        if (list.isEmpty()) {
            System.out.println("No books found.");
            return;
        }

        for (Book b : list) {
            b.displayInfo();
        }
    }

    // SEARCH by title or ISBN
    public static void searchBook() {
        System.out.println("\n--- SEARCH BOOK ---");
        String keyword = InputHelper.getString("Enter Title or ISBN: ").toLowerCase();

        boolean found = false;
        for (Book b : DataStore.books) {
            if (b.getTitle().toLowerCase().contains(keyword) ||
                b.getIsbn().toLowerCase().contains(keyword)) {
                b.displayInfo();
                found = true;
            }
        }

        if (!found) System.out.println("No books matched your search.");
    }

    // MANAGE borrower fine limit (security deposit)
    public static void manageFineLimit() {
        System.out.println("\n--- MANAGE BORROWER FINE LIMIT ---");
        String email = InputHelper.getString("Enter borrower email: ");

        Borrower target = findBorrower(email);
        if (target == null) {
            System.out.println("Borrower not found.");
            return;
        }

        System.out.println("Borrower       : " + target.getName());
        System.out.println("Security Deposit: ₹" + target.getSecurityDeposit());
        System.out.println("Caution Deposit : ₹" + target.getCautionDeposit());

        System.out.println("\n1. Set Security Deposit");
        System.out.println("2. Add to Caution Deposit");
        int choice = InputHelper.getInt("Choose: ");

        if (choice == 1) {
            double amount = InputHelper.getDouble("New Security Deposit: ₹");
            target.setSecurityDeposit(amount);
            System.out.println("Security deposit updated to ₹" + amount);
        } else if (choice == 2) {
            double amount = InputHelper.getDouble("Amount to add: ₹");
            target.addCaution(amount);
            System.out.println("Caution deposit updated to ₹" + target.getCautionDeposit());
        } else {
            System.out.println("Invalid choice.");
        }
    }

    // ─── HELPERS ──────────────────────────────────────────────

    // Find book by ID or ISBN
    public static Book findBook(String input) {
        for (Book b : DataStore.books) {
            if (b.getBookId().equalsIgnoreCase(input) ||
                b.getIsbn().equalsIgnoreCase(input)) {
                return b;
            }
        }
        return null;
    }

    // Find book by title (partial match, for borrower search)
    public static Book findBookByTitle(String title) {
        for (Book b : DataStore.books) {
            if (b.getTitle().toLowerCase().contains(title.toLowerCase())) {
                return b;
            }
        }
        return null;
    }

    // Find borrower by email
    public static Borrower findBorrower(String email) {
        for (User u : DataStore.users) {
            if (u instanceof Borrower &&
                u.getEmail().equalsIgnoreCase(email)) {
                return (Borrower) u;
            }
        }
        return null;
    }
}