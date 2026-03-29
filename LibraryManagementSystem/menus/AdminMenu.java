package menus;

import models.*;
import auth.AuthService;
import services.*;
import utils.InputHelper;

public class AdminMenu {

    public static void show(Admin admin) {
        while (true) {
            System.out.println("\n=================================");
            System.out.println("   ADMIN MENU — " + admin.getName());
            System.out.println("=================================");
            System.out.println("--- User Management ---");
            System.out.println("1.  Add Admin");
            System.out.println("2.  Add Borrower");
            System.out.println("--- Book Inventory ---");
            System.out.println("3.  Add Book");
            System.out.println("4.  Modify Book");
            System.out.println("5.  Delete Book");
            System.out.println("6.  View All Books");
            System.out.println("7.  Search Book");
            System.out.println("8.  Manage Borrower Fine Limit");
            System.out.println("--- Reports ---");
            System.out.println("9.  Low Stock Books");
            System.out.println("10. Books Never Borrowed");
            System.out.println("11. Heavily Borrowed Books");
            System.out.println("12. Outstanding Unreturned Books");
            System.out.println("13. Book Status by ISBN");
            System.out.println("---------------------------------");
            System.out.println("14. Logout");

            int choice = InputHelper.getInt("Choose: ");

            switch (choice) {
                case 1:  AuthService.addAdmin(); break;
                case 2:  AuthService.addBorrower(); break;
                case 3:  BookService.addBook(); break;
                case 4:  BookService.modifyBook(); break;
                case 5:  BookService.deleteBook(); break;
                case 6:  BookService.viewAllBooks(); break;
                case 7:  BookService.searchBook(); break;
                case 8:  BookService.manageFineLimit(); break;
                case 9:  ReportService.lowStockBooks(); break;
                case 10: ReportService.booksNeverBorrowed(); break;
                case 11: ReportService.heavilyBorrowedBooks(); break;
                case 12: ReportService.outstandingUnreturnedBooks(); break;
                case 13: ReportService.bookStatusByISBN(); break;
                case 14:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}