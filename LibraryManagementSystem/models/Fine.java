package models;

import java.time.LocalDate;

public class Fine {
    private String fineId;
    private Borrower borrower;
    private String reason;      // "LATE_RETURN", "LOST_BOOK", "LOST_CARD"
    private double amount;
    private boolean paid;
    private String paymentMode; // "CASH" or "CAUTION"
    private LocalDate fineDate;

    public Fine(String fineId, Borrower borrower, String reason, double amount) {
        this.fineId = fineId;
        this.borrower = borrower;
        this.reason = reason;
        this.amount = amount;
        this.paid = false;
        this.paymentMode = "";
        this.fineDate = LocalDate.now();
    }

    public String getFineId() { return fineId; }
    public Borrower getBorrower() { return borrower; }
    public String getReason() { return reason; }
    public double getAmount() { return amount; }
    public boolean isPaid() { return paid; }
    public String getPaymentMode() { return paymentMode; }
    public LocalDate getFineDate() { return fineDate; }

    public void markPaid(String mode) {
        this.paid = true;
        this.paymentMode = mode;
    }

    public void displayFine() {
        System.out.println("Fine ID : " + fineId +
            " | Reason: " + reason +
            " | Amount: ₹" + amount +
            " | Status: " + (paid ? "Paid (" + paymentMode + ")" : "Unpaid") +
            " | Date: " + fineDate);
    }
}