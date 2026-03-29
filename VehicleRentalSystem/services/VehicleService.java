package services;

import models.*;
import data.DataStore;
import utils.InputHelper;
import java.util.ArrayList;
import java.util.Comparator;

public class VehicleService {

    // ADD a vehicle
    public static void addVehicle() {
        System.out.println("\n--- ADD VEHICLE ---");
        String type = InputHelper.getString("Type (CAR/BIKE): ").toUpperCase();
        String id = InputHelper.getString("Vehicle ID (e.g. C003): ");
        String name = InputHelper.getString("Vehicle Name: ");
        String plate = InputHelper.getString("Number Plate: ");
        double price = InputHelper.getDouble("Rental Price per day: ₹");
        int count = InputHelper.getInt("Available Count: ");

        // Check duplicate ID
        for (Vehicle v : DataStore.vehicles) {
            if (v.getVehicleId().equalsIgnoreCase(id)) {
                System.out.println("Vehicle ID already exists.");
                return;
            }
        }

        if (type.equals("CAR")) {
            DataStore.vehicles.add(new Car(id, name, plate, price, count));
        } else if (type.equals("BIKE")) {
            DataStore.vehicles.add(new Bike(id, name, plate, price, count));
        } else {
            System.out.println("Invalid type.");
            return;
        }

        System.out.println("Vehicle added successfully.");
    }

    // DELETE a vehicle
    public static void deleteVehicle() {
        System.out.println("\n--- DELETE VEHICLE ---");
        String id = InputHelper.getString("Enter Vehicle ID to delete: ");

        Vehicle target = findById(id);
        if (target == null) {
            System.out.println("Vehicle not found.");
            return;
        }

        DataStore.vehicles.remove(target);
        System.out.println("Vehicle deleted: " + target.getName());
    }

    // MODIFY a vehicle
    public static void modifyVehicle() {
        System.out.println("\n--- MODIFY VEHICLE ---");
        String id = InputHelper.getString("Enter Vehicle ID to modify: ");

        Vehicle target = findById(id);
        if (target == null) {
            System.out.println("Vehicle not found.");
            return;
        }

        System.out.println("What to modify?");
        System.out.println("1. Available Count");
        System.out.println("2. Rental Price");
        int choice = InputHelper.getInt("Choose: ");

        if (choice == 1) {
            int count = InputHelper.getInt("New Available Count: ");
            target.setAvailableCount(count);
            System.out.println("Count updated.");
        } else if (choice == 2) {
            double price = InputHelper.getDouble("New Rental Price: ₹");
            target.setRentalPrice(price);
            System.out.println("Price updated.");
        } else {
            System.out.println("Invalid choice.");
        }
    }

    // CHANGE security deposit (Admin can change it)
    public static void changeSecurityDeposit() {
        System.out.println("\n--- CHANGE SECURITY DEPOSIT ---");
        String type = InputHelper.getString("Change deposit for (CAR/BIKE): ").toUpperCase();
        double amount = InputHelper.getDouble("New Security Deposit Amount: ₹");

        for (Vehicle v : DataStore.vehicles) {
            if (v.getType().equals(type)) {
                if (v instanceof Car) {
                    ((Car) v).setSecurityDepositRequired(amount);
                } else if (v instanceof Bike) {
                    ((Bike) v).setSecurityDepositRequired(amount);
                }
            }
        }
        System.out.println("Security deposit updated for all " + type + "s.");
    }

    // VIEW all vehicles sorted
    public static void viewAllVehicles() {
        System.out.println("\n--- ALL VEHICLES ---");
        System.out.println("Sort by: 1. Name   2. Available Count");
        int choice = InputHelper.getInt("Choose: ");

        ArrayList<Vehicle> list = new ArrayList<>(DataStore.vehicles);

        if (choice == 1) {
            list.sort(Comparator.comparing(Vehicle::getName));
        } else {
            list.sort(Comparator.comparingInt(Vehicle::getAvailableCount).reversed());
        }

        if (list.isEmpty()) {
            System.out.println("No vehicles found.");
            return;
        }

        for (Vehicle v : list) {
            v.displayInfo();
        }
    }

    // SEARCH by name or number plate
    public static void searchVehicle() {
        System.out.println("\n--- SEARCH VEHICLE ---");
        String keyword = InputHelper.getString("Enter Name or Number Plate: ").toLowerCase();

        boolean found = false;
        for (Vehicle v : DataStore.vehicles) {
            if (v.getName().toLowerCase().contains(keyword) ||
                v.getNumberPlate().toLowerCase().contains(keyword)) {
                v.displayInfo();
                found = true;
            }
        }

        if (!found) System.out.println("No vehicles matched your search.");
    }

    // HELPER — find vehicle by ID (used internally)
    public static Vehicle findById(String id) {
        for (Vehicle v : DataStore.vehicles) {
            if (v.getVehicleId().equalsIgnoreCase(id)) {
                return v;
            }
        }
        return null;
    }
}