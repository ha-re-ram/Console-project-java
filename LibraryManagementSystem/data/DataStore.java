package data;

import models.*;
import java.util.ArrayList;

public class DataStore {
    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<Book> books = new ArrayList<>();
    public static ArrayList<BorrowRecord> borrowRecords = new ArrayList<>();
    public static ArrayList<Fine> fines = new ArrayList<>();

    public static int recordCounter = 1;
    public static int fineCounter = 1;

    public static void init() {
        // Default admin
        users.add(new Admin("Admin", "admin@library.com", "admin123"));

        // Default borrower
        Borrower b = new Borrower("Ha-re-Ram", "hareram@gmail.com", "pass123");
        b.setSecurityDeposit(500); // meets minimum requirement
        users.add(b);

        // Default books
        books.add(new Book("B001", "Clean Code", "Robert C. Martin",
                           "978-0132350884", 450.0, 5));
        books.add(new Book("B002", "The Pragmatic Programmer", "Andrew Hunt",
                           "978-0201616224", 500.0, 3));
        books.add(new Book("B003", "Introduction to Algorithms", "CLRS",
                           "978-0262033848", 800.0, 4));
        books.add(new Book("B004", "Head First Java", "Kathy Sierra",
                           "978-0596009205", 350.0, 6));
        books.add(new Book("B005", "Design Patterns", "Gang of Four",
                           "978-0201633610", 600.0, 2));
    }
}