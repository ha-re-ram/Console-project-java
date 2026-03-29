package menus;

import models.*;
import services.RentalService;
import services.ReportService;
import utils.InputHelper;
public class BorrowerMenu {

    public static void show(Borrower borrower) {
    while (true) {
        System.out.println("\n=============================");
        System.out.println(" BORROWER MENU — " + borrower.getName());
        System.out.println("=============================");
        System.out.println("1. View Available Vehicles");
        System.out.println("2. Rent a Vehicle");
        System.out.println("3. Return a Vehicle");
        System.out.println("4. Extend Rental");
        System.out.println("5. Exchange Vehicle");       // ← new
        System.out.println("6. Mark Vehicle as Lost");
        System.out.println("7. View My Balance");
        System.out.println("8. View Rental History");
        System.out.println("9. Logout");

        int choice = InputHelper.getInt("Choose: ");

        switch (choice) {
            case 1: RentalService.viewAvailableVehicles(); break;
            case 2: RentalService.rentVehicle(borrower); break;
            case 3: RentalService.returnVehicle(borrower); break;
            case 4: RentalService.extendRental(borrower); break;
            case 5: RentalService.exchangeVehicle(borrower); break;  // ← new
            case 6: RentalService.markAsLost(borrower); break;
            case 7: viewBalance(borrower); break;
            case 8: ReportService.borrowerRentalHistory(borrower); break;
            case 9:
                System.out.println("Logging out...");
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }
}

    private static void viewBalance(Borrower borrower) {
        System.out.println("\n=============================");
        System.out.println(" YOUR BALANCE — " + borrower.getName());
        System.out.println("=============================");
        System.out.println("Caution Deposit: ₹" + borrower.getCautionDeposit());
        System.out.println("Security Deposit: ₹" + borrower.getSecurityDeposit());
        System.out.println("Total Balance: ₹" + (borrower.getCautionDeposit() + borrower.getSecurityDeposit()));
        System.out.println("=============================");
    }
}