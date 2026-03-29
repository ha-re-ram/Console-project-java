package services;

import models.*;
import data.DataStore;
import utils.InputHelper;
import utils.DateHelper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

public class ReportService {

    // ─── ADMIN REPORTS ────────────────────────────────────────

    // REPORT 1 — Low stock books (quantity less than 3)
    public static void lowStockBooks() {
        System.out.println("\n--- LOW STOCK BOOKS ---");
        boolean found = false;

        for (Book b : DataStore.books) {
            if (b.getAvailableQuantity() < 3) {
                System.out.println("⚠️  " + b.getTitle() +
                    " | ISBN: " + b.getIsbn() +
                    " | Available: " + b.getAvailableQuantity() +
                    "/" + b.getTotalQuantity());
                found = true;
            }
        }

        if (!found) System.out.println("All books are well stocked.");
    }

    // REPORT 2 — Books never borrowed
    public static void booksNeverBorrowed() {
        System.out.println("\n--- BOOKS NEVER BORROWED ---");
        boolean found = false;

        for (Book b : DataStore.books) {
            if (!b.isEverBorrowed()) {
                b.displayInfo();
                found = true;
            }
        }

        if (!found) System.out.println("All books have been borrowed at least once.");
    }

    // REPORT 3 — Heavily borrowed books (sorted by borrow count)
    public static void heavilyBorrowedBooks() {
        System.out.println("\n--- HEAVILY BORROWED BOOKS ---");

        ArrayList<Book> list = new ArrayList<>(DataStore.books);

        // Sort by borrow count descending
        list.sort((a, b) -> Integer.compare(b.getBorrowCount(), a.getBorrowCount()));

        boolean found = false;
        for (Book b : list) {
            if (b.getBorrowCount() > 0) {
                System.out.println("Title      : " + b.getTitle() +
                    " | Borrowed " + b.getBorrowCount() + " time(s)" +
                    " | Available: " + b.getAvailableQuantity() +
                    "/" + b.getTotalQuantity());
                found = true;
            }
        }

        if (!found) System.out.println("No books have been borrowed yet.");
    }

    // REPORT 4 — Outstanding unreturned books as on a given date
    public static void outstandingUnreturnedBooks() {
        System.out.println("\n--- OUTSTANDING UNRETURNED BOOKS ---");
        LocalDate asOnDate = DateHelper.getDateInput("Enter date to check");

        boolean found = false;
        for (BorrowRecord r : DataStore.borrowRecords) {
            if (!r.isReturned() && r.getDueDate().isBefore(asOnDate)) {
                long daysLate = DateHelper.daysBetween(r.getDueDate(), asOnDate);
                System.out.println("Borrower  : " + r.getBorrower().getName() +
                    " | Email: " + r.getBorrower().getEmail());
                System.out.println("Book      : " + r.getBook().getTitle());
                System.out.println("Due Date  : " + r.getDueDate());
                System.out.println("Days Late : " + daysLate);
                System.out.println("-----------------------------");
                found = true;
            }
        }

        if (!found) System.out.println("No outstanding unreturned books as on " + asOnDate);
    }

    // REPORT 5 — Book status by ISBN
    public static void bookStatusByISBN() {
        System.out.println("\n--- BOOK STATUS BY ISBN ---");
        String isbn = InputHelper.getString("Enter ISBN: ");

        Book book = null;
        for (Book b : DataStore.books) {
            if (b.getIsbn().equalsIgnoreCase(isbn)) {
                book = b;
                break;
            }
        }

        if (book == null) {
            System.out.println("No book found with ISBN: " + isbn);
            return;
        }

        // Show book details
        book.displayInfo();

        // Show who currently has it borrowed and expected return
        System.out.println("\nCurrently borrowed by:");
        boolean anyActive = false;

        for (BorrowRecord r : DataStore.borrowRecords) {
            if (r.getBook().getIsbn().equalsIgnoreCase(isbn) && !r.isReturned()) {
                System.out.println("  Borrower  : " + r.getBorrower().getName() +
                    " (" + r.getBorrower().getEmail() + ")");
                System.out.println("  Borrowed  : " + r.getBorrowDate());
                System.out.println("  Due Date  : " + r.getDueDate());
                System.out.println("  Expected  : " +
                    (r.getExtensionCount() > 0 ? "Extended — due " + r.getDueDate()
                                               : "Due " + r.getDueDate()));
                System.out.println("  ---");
                anyActive = true;
            }
        }

        if (!anyActive) {
            System.out.println("  This book is currently available on the rack.");
        }
    }

    // ─── BORROWER REPORTS ─────────────────────────────────────

    // Fine history for borrower
    public static void borrowerFineHistory(Borrower borrower) {
        System.out.println("\n--- YOUR FINE HISTORY ---");
        boolean found = false;

        for (Fine f : DataStore.fines) {
            if (f.getBorrower().getEmail().equals(borrower.getEmail())) {
                f.displayFine();
                found = true;
            }
        }

        if (!found) System.out.println("No fines on record. Great job!");
    }

    // Borrow history for borrower
    public static void borrowerBorrowHistory(Borrower borrower) {
        System.out.println("\n--- YOUR BORROW HISTORY ---");
        boolean found = false;

        for (BorrowRecord r : DataStore.borrowRecords) {
            if (r.getBorrower().getEmail().equals(borrower.getEmail())) {
                System.out.println("Record ID : " + r.getRecordId());
                System.out.println("Book      : " + r.getBook().getTitle());
                System.out.println("Author    : " + r.getBook().getAuthor());
                System.out.println("ISBN      : " + r.getBook().getIsbn());
                System.out.println("Borrowed  : " + r.getBorrowDate());
                System.out.println("Due Date  : " + r.getDueDate());
                System.out.println("Returned  : " + DateHelper.format(r.getReturnDate()));
                System.out.println("Status    : " + r.getStatus());
                System.out.println("-----------------------------");
                found = true;
            }
        }

        if (!found) System.out.println("No borrow history found.");
    }
}