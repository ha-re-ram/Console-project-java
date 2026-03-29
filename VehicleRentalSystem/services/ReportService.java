package services;

import models.*;
import data.DataStore;
import utils.InputHelper;
import java.util.ArrayList;
import java.util.Comparator;

public class ReportService {

    // REPORT 1 — Vehicles due for service
    public static void vehiclesDueForService() {
        System.out.println("\n--- VEHICLES DUE FOR SERVICE ---");
        boolean found = false;

        for (Vehicle v : DataStore.vehicles) {
            if (v.getTotalKms() >= v.getServiceLimit()) {
                System.out.println("⚠️  " + v.getName() +
                    " | Type: " + v.getType() +
                    " | KMs: " + v.getTotalKms() +
                    " | Service limit: " + v.getServiceLimit());
                found = true;
            }
        }

        if (!found) System.out.println("All vehicles are good. No service due.");
    }

    // REPORT 2 — All vehicles sorted by rental price
    public static void vehiclesSortedByPrice() {
        System.out.println("\n--- VEHICLES SORTED BY RENTAL PRICE ---");

        ArrayList<Vehicle> list = new ArrayList<>(DataStore.vehicles);
        list.sort(Comparator.comparingDouble(Vehicle::getRentalPrice));

        if (list.isEmpty()) {
            System.out.println("No vehicles found.");
            return;
        }

        for (Vehicle v : list) {
            v.displayInfo();
        }
    }

    // REPORT 3 — Search by name, filter by CAR or BIKE
    public static void searchAndFilter() {
        System.out.println("\n--- SEARCH & FILTER VEHICLES ---");
        String keyword = InputHelper.getString("Enter vehicle name (or press Enter to skip): ").toLowerCase();
        System.out.println("Filter by type: 1. CAR   2. BIKE   3. Both");
        int filterChoice = InputHelper.getInt("Choose: ");

        String typeFilter = "";
        if (filterChoice == 1) typeFilter = "CAR";
        else if (filterChoice == 2) typeFilter = "BIKE";

        boolean found = false;
        for (Vehicle v : DataStore.vehicles) {
            // Apply name filter
            boolean nameMatch = keyword.isEmpty() ||
                                v.getName().toLowerCase().contains(keyword);

            // Apply type filter
            boolean typeMatch = typeFilter.isEmpty() ||
                                v.getType().equals(typeFilter);

            if (nameMatch && typeMatch) {
                v.displayInfo();
                found = true;
            }
        }

        if (!found) System.out.println("No vehicles matched your search.");
    }

    // REPORT 4 — Vehicles currently rented out vs never rented
    public static void rentalStatusReport() {
        System.out.println("\n--- VEHICLE RENTAL STATUS REPORT ---");

        // Collect IDs of currently rented vehicles
        ArrayList<String> rentedIds = new ArrayList<>();
        for (Rental r : DataStore.rentals) {
            if (!r.isReturned()) {
                rentedIds.add(r.getVehicle().getVehicleId());
            }
        }

        // Collect IDs of vehicles that were ever rented
        ArrayList<String> everRentedIds = new ArrayList<>();
        for (Rental r : DataStore.rentals) {
            everRentedIds.add(r.getVehicle().getVehicleId());
        }

        System.out.println("\n🔴 Currently Rented Out:");
        boolean anyRented = false;
        for (Vehicle v : DataStore.vehicles) {
            if (rentedIds.contains(v.getVehicleId())) {
                System.out.println("  → " + v.getName() + " | Plate: " + v.getNumberPlate());
                anyRented = true;
            }
        }
        if (!anyRented) System.out.println("  None currently rented.");

        System.out.println("\n⚪ Never Been Rented:");
        boolean anyNever = false;
        for (Vehicle v : DataStore.vehicles) {
            if (!everRentedIds.contains(v.getVehicleId())) {
                System.out.println("  → " + v.getName() + " | Plate: " + v.getNumberPlate());
                anyNever = true;
            }
        }
        if (!anyNever) System.out.println("  All vehicles have been rented at least once.");
    }

    // REPORT 5 — Borrower's own rental history (called from BorrowerMenu)
    public static void borrowerRentalHistory(Borrower borrower) {
        System.out.println("\n--- YOUR RENTAL HISTORY ---");
        boolean found = false;

        for (Rental r : DataStore.rentals) {
            if (r.getBorrower().getEmail().equals(borrower.getEmail())) {
                System.out.println("Rental ID  : " + r.getRentalId());
                System.out.println("Vehicle    : " + r.getVehicle().getName());
                System.out.println("Type       : " + r.getVehicle().getType());
                System.out.println("Date       : " + r.getRentalDate());
                System.out.println("Due Date   : " + r.getDueDate());
                System.out.println("KMs Driven : " + r.getKmsDriven());
                System.out.println("Status     : " + r.getStatus());
                System.out.println("-----------------------------");
                found = true;
            }
        }

        if (!found) System.out.println("No rental history found.");
    }
}