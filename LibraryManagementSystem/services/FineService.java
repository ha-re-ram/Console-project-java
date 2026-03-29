package services;

import models.*;
import data.DataStore;
import utils.InputHelper;

public class FineService {

    // LATE RETURN FINE
    public static void applyLateFine(Borrower borrower,
                                     BorrowRecord record, long daysLate) {
        double fine = calculateLateFine(daysLate, record.getBook().getCost());

        System.out.println("\n⚠️  Late Fine Applicable");
        System.out.println("Days late : " + daysLate);
        System.out.println("Fine      : ₹" + fine);

        collectFine(borrower, fine, "LATE_RETURN");
    }

    // LOST BOOK FINE
    public static void applyLostBookFine(Borrower borrower, BorrowRecord record) {
        double bookCost = record.getBook().getCost();
        double fine = bookCost * 0.50; // 50% of book cost

        System.out.println("\n⚠️  Lost Book Fine");
        System.out.println("Book cost : ₹" + bookCost);
        System.out.println("Fine (50%): ₹" + fine);

        collectFine(borrower, fine, "LOST_BOOK");
    }

    // LOST CARD FINE
    public static void applyCardLostFine(Borrower borrower) {
        double fine = 10.0; // flat ₹10

        System.out.println("\n⚠️  Membership Card Lost Fine: ₹" + fine);
        collectFine(borrower, fine, "LOST_CARD");
    }

    // VIEW fine history for a borrower
    public static void viewFineHistory(Borrower borrower) {
        System.out.println("\n--- YOUR FINE HISTORY ---");
        boolean found = false;

        for (Fine f : DataStore.fines) {
            if (f.getBorrower().getEmail().equals(borrower.getEmail())) {
                f.displayFine();
                found = true;
            }
        }

        if (!found) System.out.println("No fines on record. Keep it up!");
    }

    // ─── CORE FINE CALCULATION ─────────────────────────────────

    private static double calculateLateFine(long daysLate, double bookCost) {
        double totalFine = 0;
        double ratePerDay = 2.0; // base rate ₹2/day
        long remainingDays = daysLate;
        int period = 1; // tracks which 10-day block we're in

        while (remainingDays > 0) {
            // How many days fall in this 10-day block
            long daysInThisPeriod = Math.min(remainingDays, 10);

            // Fine for this block = days × rate for this period
            totalFine += daysInThisPeriod * ratePerDay;

            remainingDays -= daysInThisPeriod;
            period++;

            // Double the rate for next 10-day block (exponential)
            ratePerDay = 2.0 * Math.pow(2, period - 1);
        }

        // Cap at 80% of book cost
        double maxFine = bookCost * 0.80;
        return Math.min(totalFine, maxFine);
    }

    // ─── FINE COLLECTION ──────────────────────────────────────

    private static void collectFine(Borrower borrower,
                                    double amount, String reason) {
        System.out.println("\nPay fine by:");
        System.out.println("1. Cash");
        System.out.println("2. Deduct from caution deposit");
        int choice = InputHelper.getInt("Choose: ");

        String fineId = "F" + DataStore.fineCounter++;
        Fine fine = new Fine(fineId, borrower, reason, amount);

        if (choice == 1) {
            fine.markPaid("CASH");
            DataStore.fines.add(fine);
            System.out.println("Please pay ₹" + amount + " in cash at the counter.");
        } else if (choice == 2) {
            if (borrower.getCautionDeposit() >= amount) {
                borrower.deductCaution(amount);
                fine.markPaid("CAUTION");
                DataStore.fines.add(fine);
                System.out.println("₹" + amount + " deducted from caution deposit.");
                System.out.println("Remaining caution: ₹" + borrower.getCautionDeposit());
            } else {
                System.out.println("Insufficient caution deposit.");
                System.out.println("Available : ₹" + borrower.getCautionDeposit());
                System.out.println("Required  : ₹" + amount);
                System.out.println("Please pay remaining amount in cash.");

                // Deduct whatever is available, rest by cash
                double available = borrower.getCautionDeposit();
                double remaining = amount - available;
                borrower.deductCaution(available);
                fine.markPaid("CAUTION+CASH");
                DataStore.fines.add(fine);
                System.out.println("₹" + available + " deducted from caution.");
                System.out.println("Please pay ₹" + remaining + " in cash.");
            }
        } else {
            System.out.println("Invalid choice. Fine recorded as unpaid.");
            DataStore.fines.add(fine);
        }
    }
}