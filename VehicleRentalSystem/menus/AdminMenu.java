package menus;

import models.*;
import services.VehicleService;
import services.ReportService;
import utils.InputHelper;

public class AdminMenu {

public static void show(Admin admin) {
    while (true) {
        System.out.println("\n=============================");
        System.out.println("   ADMIN MENU — " + admin.getName());
        System.out.println("=============================");
        System.out.println("--- Vehicle Management ---");
        System.out.println("1. Add Vehicle");
        System.out.println("2. Delete Vehicle");
        System.out.println("3. Modify Vehicle");
        System.out.println("4. View All Vehicles");
        System.out.println("5. Search Vehicle");
        System.out.println("6. Change Security Deposit");
        System.out.println("--- Reports ---");
        System.out.println("7. Vehicles Due for Service");
        System.out.println("8. Vehicles Sorted by Price");
        System.out.println("9. Search & Filter Vehicles");
        System.out.println("10. Rental Status Report");
        System.out.println("-----------------------------");
        System.out.println("11. Logout");

        int choice = InputHelper.getInt("Choose: ");

        switch (choice) {
            case 1:  VehicleService.addVehicle(); break;
            case 2:  VehicleService.deleteVehicle(); break;
            case 3:  VehicleService.modifyVehicle(); break;
            case 4:  VehicleService.viewAllVehicles(); break;
            case 5:  VehicleService.searchVehicle(); break;
            case 6:  VehicleService.changeSecurityDeposit(); break;
            case 7:  ReportService.vehiclesDueForService(); break;
            case 8:  ReportService.vehiclesSortedByPrice(); break;
            case 9:  ReportService.searchAndFilter(); break;
            case 10: ReportService.rentalStatusReport(); break;
            case 11:
                System.out.println("Logging out...");
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }
}
}