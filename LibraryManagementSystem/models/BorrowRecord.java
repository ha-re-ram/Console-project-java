package models;

import java.time.LocalDate;

public class BorrowRecord {
    private String recordId;
    private Borrower borrower;
    private Book book;
    private LocalDate borrowDate;
    private LocalDate dueDate;       // 15 days from borrow date
    private LocalDate returnDate;    // actual return date (set when returned)
    private boolean returned;
    private int extensionCount;      // max 2
    private String status;           // "ACTIVE", "RETURNED", "LOST"

    public BorrowRecord(String recordId, Borrower borrower,
                        Book book, LocalDate borrowDate) {
        this.recordId = recordId;
        this.borrower = borrower;
        this.book = book;
        this.borrowDate = borrowDate;
        this.dueDate = borrowDate.plusDays(15); // 15 day borrow period
        this.returnDate = null;
        this.returned = false;
        this.extensionCount = 0;
        this.status = "ACTIVE";
    }

    public String getRecordId() { return recordId; }
    public Borrower getBorrower() { return borrower; }
    public Book getBook() { return book; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public boolean isReturned() { return returned; }
    public int getExtensionCount() { return extensionCount; }
    public String getStatus() { return status; }

    public void setReturned(boolean r) { this.returned = r; }
    public void setReturnDate(LocalDate date) { this.returnDate = date; }
    public void setStatus(String status) { this.status = status; }

    public boolean extendDueDate() {
        if (extensionCount < 2) {
            dueDate = dueDate.plusDays(15); // extend by another 15 days
            extensionCount++;
            return true;
        }
        return false;
    }
}