package services;

import models.*;
import data.DataStore;
import utils.InputHelper;
import java.time.LocalDate;
import java.util.ArrayList;

public class BorrowService {

    // VIEW available books
    public static void viewAvailableBooks() {
        System.out.println("\n--- AVAILABLE BOOKS ---");
        boolean found = false;

        for (Book b : DataStore.books) {
            if (b.getAvailableQuantity() > 0) {
                b.displayInfo();
                found = true;
            }
        }

        if (!found) System.out.println("No books available right now.");
    }

    // BORROW a book
    public static void borrowBook(Borrower borrower) {
        System.out.println("\n--- BORROW BOOK ---");

        // Check max 3 books limit
        if (borrower.getActiveBorrowCount() >= 3) {
            System.out.println("You already have 3 books borrowed.");
            System.out.println("Return a book before borrowing another.");
            return;
        }

        // Check security deposit
        if (borrower.getSecurityDeposit() < 500) {
            System.out.println("Insufficient security deposit.");
            System.out.println("Minimum ₹500 required. Current: ₹"
                + borrower.getSecurityDeposit());
            System.out.println("Please contact admin to update your deposit.");
            return;
        }

        viewAvailableBooks();
        String input = InputHelper.getString("\nEnter Book ID, ISBN or Title: ");

        // Try finding by ID or ISBN first, then by title
        Book selected = BookService.findBook(input);
        if (selected == null) {
            selected = BookService.findBookByTitle(input);
        }

        if (selected == null) {
            System.out.println("Book not found.");
            return;
        }

        if (selected.getAvailableQuantity() <= 0) {
            System.out.println("No copies available for: " + selected.getTitle());
            return;
        }

        // Check if borrower already has this book
        if (hasActiveBorrow(borrower, selected)) {
            System.out.println("You already have this book borrowed.");
            System.out.println("You cannot borrow the same book twice.");
            return;
        }

        // All checks passed — create borrow record
        String recordId = "REC" + DataStore.recordCounter++;
        LocalDate today = LocalDate.now();
        BorrowRecord record = new BorrowRecord(recordId, borrower, selected, today);

        DataStore.borrowRecords.add(record);
        selected.reduceAvailable();
        borrower.incrementBorrowCount();

        System.out.println("\n✅ Book borrowed successfully!");
        System.out.println("Record ID : " + recordId);
        System.out.println("Book      : " + selected.getTitle());
        System.out.println("Borrowed  : " + today);
        System.out.println("Due Date  : " + record.getDueDate());
        System.out.println("Note: Return within 15 days to avoid fine.");
    }

    // RETURN a book
    public static void returnBook(Borrower borrower) {
        System.out.println("\n--- RETURN BOOK ---");

        ArrayList<BorrowRecord> active = getActiveRecords(borrower);
        if (active.isEmpty()) {
            System.out.println("You have no active borrows.");
            return;
        }

        // Show active borrows
        System.out.println("Your active borrows:");
        for (BorrowRecord r : active) {
            System.out.println("Record ID: " + r.getRecordId() +
                " | Book: " + r.getBook().getTitle() +
                " | Due: " + r.getDueDate());
        }

        String recordId = InputHelper.getString("\nEnter Record ID to return: ");
        BorrowRecord target = findRecord(recordId, borrower);

        if (target == null) {
            System.out.println("Record not found.");
            return;
        }

        // Get return date from user
        LocalDate returnDate = utils.DateHelper.getDateInput("Enter return date");
        target.setReturnDate(returnDate);
        target.setReturned(true);
        target.setStatus("RETURNED");

        target.getBook().increaseAvailable();
        borrower.decrementBorrowCount();

        System.out.println("Book returned: " + target.getBook().getTitle());

        // Check if fine applies
        long daysLate = utils.DateHelper.daysBetween(target.getDueDate(), returnDate);
        if (daysLate > 0) {
            System.out.println("Book is " + daysLate + " day(s) late.");
            FineService.applyLateFine(borrower, target, daysLate);
        } else {
            System.out.println("✅ Returned on time. No fine.");
        }
    }

    // EXTEND borrow tenure
    public static void extendBorrow(Borrower borrower) {
        System.out.println("\n--- EXTEND BORROW ---");

        ArrayList<BorrowRecord> active = getActiveRecords(borrower);
        if (active.isEmpty()) {
            System.out.println("No active borrows to extend.");
            return;
        }

        // Show active borrows
        for (BorrowRecord r : active) {
            System.out.println("Record ID: " + r.getRecordId() +
                " | Book: " + r.getBook().getTitle() +
                " | Due: " + r.getDueDate() +
                " | Extensions used: " + r.getExtensionCount() + "/2");
        }

        String recordId = InputHelper.getString("\nEnter Record ID to extend: ");
        BorrowRecord target = findRecord(recordId, borrower);

        if (target == null) {
            System.out.println("Record not found.");
            return;
        }

        boolean extended = target.extendDueDate();
        if (extended) {
            System.out.println("✅ Extended successfully!");
            System.out.println("New due date : " + target.getDueDate());
            System.out.println("Extensions used: " + target.getExtensionCount() + "/2");
        } else {
            System.out.println("Cannot extend. Maximum 2 extensions already used.");
        }
    }

    // EXCHANGE a book
    public static void exchangeBook(Borrower borrower) {
        System.out.println("\n--- EXCHANGE BOOK ---");

        ArrayList<BorrowRecord> active = getActiveRecords(borrower);
        if (active.isEmpty()) {
            System.out.println("No active borrows to exchange.");
            return;
        }

        // Show active borrows
        for (BorrowRecord r : active) {
            System.out.println("Record ID: " + r.getRecordId() +
                " | Book: " + r.getBook().getTitle());
        }

        String recordId = InputHelper.getString("\nEnter Record ID to exchange: ");
        BorrowRecord target = findRecord(recordId, borrower);

        if (target == null) {
            System.out.println("Record not found.");
            return;
        }

        // Show available books excluding current one
        System.out.println("\nAvailable books for exchange:");
        boolean found = false;
        for (Book b : DataStore.books) {
            if (!b.getBookId().equals(target.getBook().getBookId()) &&
                b.getAvailableQuantity() > 0) {
                b.displayInfo();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No other books available for exchange.");
            return;
        }

        String input = InputHelper.getString("\nEnter Book ID or ISBN to exchange with: ");
        Book newBook = BookService.findBook(input);

        if (newBook == null) {
            System.out.println("Book not found.");
            return;
        }

        if (newBook.getAvailableQuantity() <= 0) {
            System.out.println("No copies available for: " + newBook.getTitle());
            return;
        }

        // Check borrower doesn't already have this new book
        if (hasActiveBorrow(borrower, newBook)) {
            System.out.println("You already have this book borrowed.");
            return;
        }

        // Do the exchange — return old, borrow new
        target.getBook().increaseAvailable();

        String newRecordId = "REC" + DataStore.recordCounter++;
        BorrowRecord newRecord = new BorrowRecord(
            newRecordId, borrower, newBook, LocalDate.now());

        target.setStatus("RETURNED");
        target.setReturned(true);

        DataStore.borrowRecords.add(newRecord);
        newBook.reduceAvailable();

        System.out.println("✅ Exchange successful!");
        System.out.println("Returned : " + target.getBook().getTitle());
        System.out.println("New Book : " + newBook.getTitle());
        System.out.println("New Record ID: " + newRecordId);
        System.out.println("New Due Date : " + newRecord.getDueDate());
    }

    // MARK book as lost
    public static void markBookLost(Borrower borrower) {
        System.out.println("\n--- MARK BOOK AS LOST ---");

        ArrayList<BorrowRecord> active = getActiveRecords(borrower);
        if (active.isEmpty()) {
            System.out.println("No active borrows found.");
            return;
        }

        for (BorrowRecord r : active) {
            System.out.println("Record ID: " + r.getRecordId() +
                " | Book: " + r.getBook().getTitle());
        }

        String recordId = InputHelper.getString("\nEnter Record ID of lost book: ");
        BorrowRecord target = findRecord(recordId, borrower);

        if (target == null) {
            System.out.println("Record not found.");
            return;
        }

        target.setStatus("LOST");
        target.setReturned(true);
        borrower.decrementBorrowCount();

        // Apply lost book fine
        FineService.applyLostBookFine(borrower, target);
    }

    // REPORT membership card lost
    public static void reportCardLost(Borrower borrower) {
        System.out.println("\n--- REPORT MEMBERSHIP CARD LOST ---");

        if (borrower.isMembershipCardLost()) {
            System.out.println("Card already reported as lost.");
            return;
        }

        borrower.setMembershipCardLost(true);
        FineService.applyCardLostFine(borrower);
    }

    // VIEW borrow history
    public static void viewBorrowHistory(Borrower borrower) {
        System.out.println("\n--- YOUR BORROW HISTORY ---");
        boolean found = false;

        for (BorrowRecord r : DataStore.borrowRecords) {
            if (r.getBorrower().getEmail().equals(borrower.getEmail())) {
                System.out.println("Record ID : " + r.getRecordId());
                System.out.println("Book      : " + r.getBook().getTitle());
                System.out.println("ISBN      : " + r.getBook().getIsbn());
                System.out.println("Borrowed  : " + r.getBorrowDate());
                System.out.println("Due Date  : " + r.getDueDate());
                System.out.println("Returned  : " + utils.DateHelper.format(r.getReturnDate()));
                System.out.println("Status    : " + r.getStatus());
                System.out.println("-----------------------------");
                found = true;
            }
        }

        if (!found) System.out.println("No borrow history found.");
    }

    // ─── HELPERS ──────────────────────────────────────────────

    // Check if borrower already has this book active
    public static boolean hasActiveBorrow(Borrower borrower, Book book) {
        for (BorrowRecord r : DataStore.borrowRecords) {
            if (r.getBorrower().getEmail().equals(borrower.getEmail()) &&
                r.getBook().getBookId().equals(book.getBookId()) &&
                !r.isReturned()) {
                return true;
            }
        }
        return false;
    }

    // Get all active borrow records for a borrower
    public static ArrayList<BorrowRecord> getActiveRecords(Borrower borrower) {
        ArrayList<BorrowRecord> active = new ArrayList<>();
        for (BorrowRecord r : DataStore.borrowRecords) {
            if (r.getBorrower().getEmail().equals(borrower.getEmail()) &&
                !r.isReturned()) {
                active.add(r);
            }
        }
        return active;
    }

    // Find a specific record by ID belonging to a borrower
    private static BorrowRecord findRecord(String recordId, Borrower borrower) {
        for (BorrowRecord r : DataStore.borrowRecords) {
            if (r.getRecordId().equalsIgnoreCase(recordId) &&
                r.getBorrower().getEmail().equals(borrower.getEmail()) &&
                !r.isReturned()) {
                return r;
            }
        }
        return null;
    }
}