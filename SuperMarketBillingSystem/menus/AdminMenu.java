package menus;

import models.*;
import data.DataStore;
import services.*;
import utils.InputHelper;
import auth.AuthService;

public class AdminMenu {

    public static void show(Admin admin) {
        while (true) {
            System.out.println("\n=================================");
            System.out.println("   ADMIN MENU — " + admin.getName());
            System.out.println("=================================");
            System.out.println("--- User Management ---");
            System.out.println("1.  Add Admin");
            System.out.println("2.  Add Customer");
            System.out.println("--- Inventory ---");
            System.out.println("3.  Add Product");
            System.out.println("4.  Modify Product");
            System.out.println("5.  Delete Product");
            System.out.println("6.  View All Products");
            System.out.println("7.  Search Product");
            System.out.println("8.  Increase Customer Credit");
            System.out.println("--- Reports ---");
            System.out.println("9.  Low Stock Products");
            System.out.println("10. Products Never Bought");
            System.out.println("11. Top Spending Customers");
            System.out.println("12. Admin Sales Report");
            System.out.println("---------------------------------");
            System.out.println("13. Logout");

            int choice = InputHelper.getInt("Choose: ");

            switch (choice) {
                case 1:  AuthService.addAdmin(); break;
                case 2:  AuthService.addCustomer(); break;
                case 3:  InventoryService.addProduct(); break;
                case 4:  InventoryService.modifyProduct(); break;
                case 5:  InventoryService.deleteProduct(); break;
                case 6:  InventoryService.viewAllProducts(); break;
                case 7:  InventoryService.searchProduct(); break;
                case 8:  InventoryService.increaseCustomerCredit(); break;
                case 9:  ReportService.lowStockProducts(); break;
                case 10: ReportService.productsNeverBought(); break;
                case 11: ReportService.topSpendingCustomers(); break;
                case 12: ReportService.adminSalesReport(); break;
                case 13:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}