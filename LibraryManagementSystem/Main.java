import models.*;
import auth.AuthService;
import data.DataStore;
import menus.*;
import utils.InputHelper;

public class Main {

    public static void main(String[] args) {
        DataStore.init();

        System.out.println("=================================");
        System.out.println("    LIBRARY MANAGEMENT SYSTEM    ");
        System.out.println("=================================");

        while (true) {
            System.out.println("\n1. Login");
            System.out.println("2. Exit");
            int choice = InputHelper.getInt("Choose: ");

            switch (choice) {
                case 1:
                    User loggedIn = AuthService.login();
                    if (loggedIn != null) {
                        if (loggedIn.getRole().equals("ADMIN")) {
                            AdminMenu.show((Admin) loggedIn);
                        } else {
                            BorrowerMenu.show((Borrower) loggedIn);
                        }
                    }
                    break;

                case 2:
                    System.out.println("Goodbye!");
                    System.exit(0);

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}