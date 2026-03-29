package menus;

import models.*;
import services.*;
import utils.InputHelper;

public class BorrowerMenu {

    public static void show(Borrower borrower) {
        while (true) {
            System.out.println("\n=================================");
            System.out.println("  BORROWER MENU — " + borrower.getName());
            System.out.println("=================================");
            System.out.println("Caution Deposit : ₹" + borrower.getCautionDeposit());
            System.out.println("Security Deposit: ₹" + borrower.getSecurityDeposit());
            System.out.println("Active Borrows  : " + borrower.getActiveBorrowCount() + "/3");
            System.out.println("---------------------------------");
            System.out.println("--- Books ---");
            System.out.println("1. View Available Books");
            System.out.println("2. Borrow a Book");
            System.out.println("3. Return a Book");
            System.out.println("4. Extend Borrow");
            System.out.println("5. Exchange Book");
            System.out.println("6. Mark Book as Lost");
            System.out.println("7. Report Membership Card Lost");
            System.out.println("--- History ---");
            System.out.println("8. My Borrow History");
            System.out.println("9. My Fine History");
            System.out.println("---------------------------------");
            System.out.println("10. Logout");

            int choice = InputHelper.getInt("Choose: ");

            switch (choice) {
                case 1:  BorrowService.viewAvailableBooks(); break;
                case 2:  BorrowService.borrowBook(borrower); break;
                case 3:  BorrowService.returnBook(borrower); break;
                case 4:  BorrowService.extendBorrow(borrower); break;
                case 5:  BorrowService.exchangeBook(borrower); break;
                case 6:  BorrowService.markBookLost(borrower); break;
                case 7:  BorrowService.reportCardLost(borrower); break;
                case 8:  ReportService.borrowerBorrowHistory(borrower); break;
                case 9:  ReportService.borrowerFineHistory(borrower); break;
                case 10:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}