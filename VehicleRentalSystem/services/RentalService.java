package services;

import models.*;
import data.DataStore;
import utils.InputHelper;
import java.time.LocalDate;
import java.util.ArrayList;
public class RentalService {

    // VIEW available vehicles for borrower
    // Only shows vehicles that are available AND serviced
    public static void viewAvailableVehicles() {
        System.out.println("\n--- AVAILABLE VEHICLES ---");
        boolean found = false;

        for (Vehicle v : DataStore.vehicles) {
            // Skip if no stock
            if (v.getAvailableCount() <= 0) continue;

            // Skip if due for service
            if (v.getTotalKms() >= v.getServiceLimit()) continue;

            v.displayInfo();
            found = true;
        }

        if (!found) System.out.println("No vehicles available right now.");
    }

    // RENT a vehicle
    public static void rentVehicle(Borrower borrower) {
        System.out.println("\n--- RENT A VEHICLE ---");
        viewAvailableVehicles();

        String input = InputHelper.getString("\nEnter Vehicle ID or Name to rent: ");
        Vehicle selected = findVehicle(input);

        if (selected == null) {
            System.out.println("Vehicle not found.");
            return;
        }

        // Check service limit
        if (selected.getTotalKms() >= selected.getServiceLimit()) {
            System.out.println("This vehicle is due for service. Cannot rent.");
            return;
        }

        // Check availability
        if (selected.getAvailableCount() <= 0) {
            System.out.println("No units available for this vehicle.");
            return;
        }

        // Check if borrower already has a car or bike rented
        String type = selected.getType();
        if (hasActiveRental(borrower, type)) {
            System.out.println("You already have a " + type + " rented. Return it first.");
            return;
        }

        // Check security deposit
        double requiredDeposit = getRequiredDeposit(selected);
        if (borrower.getSecurityDeposit() < requiredDeposit) {
            System.out.println("Insufficient security deposit.");
            System.out.println("Required: ₹" + requiredDeposit + 
                             " | Your deposit: ₹" + borrower.getSecurityDeposit());
            return;
        }

        // All checks passed — create rental
        String rentalId = "R" + DataStore.rentalCounter++;
        LocalDate today = LocalDate.now();
        Rental rental = new Rental(rentalId, borrower, selected, today);

        DataStore.rentals.add(rental);
        selected.setAvailableCount(selected.getAvailableCount() - 1);
        DataStore.saveToDisk();

        System.out.println("\n✅ Vehicle rented successfully!");
        System.out.println("Rental ID : " + rentalId);
        System.out.println("Vehicle   : " + selected.getName());
        System.out.println("Date      : " + today);
        System.out.println("Due back  : Same day (before midnight)");
    }

    // RETURN a vehicle
    public static void returnVehicle(Borrower borrower) {
        System.out.println("\n--- RETURN VEHICLE ---");

        Rental rental = getActiveRental(borrower);
        if (rental == null) {
            System.out.println("You have no active rentals.");
            return;
        }

        System.out.println("Active Rental: " + rental.getVehicle().getName() +
                         " | Rental ID: " + rental.getRentalId());

        double kms = InputHelper.getDouble("Kilometers driven: ");
        rental.setKmsDriven(kms);
        rental.getVehicle().addKms(kms);

        double totalFine = 0;
        double rentalCharge = rental.getVehicle().getRentalPrice();

        // Extra charge if driven more than 500 kms
        if (kms > 500) {
            double extraCharge = rentalCharge * 0.15;
            System.out.println("Extra charge for exceeding 500 kms: ₹" + extraCharge);
            totalFine += extraCharge;
        }

        // Damage fine
        System.out.println("\nAny damage? (YES/NO): ");
        String damaged = InputHelper.getString("").toUpperCase();
        if (damaged.equals("YES")) {
            System.out.println("Damage level: LOW / MEDIUM / HIGH");
            String level = InputHelper.getString("Enter level: ").toUpperCase();
            double damageFine = calculateDamageFine(rentalCharge, level);
            System.out.println("Damage fine: ₹" + damageFine);
            totalFine += damageFine;
        }

        // Deduct fine from caution deposit
        if (totalFine > 0) {
            System.out.println("\nTotal fine: ₹" + totalFine);
            System.out.println("Pay by: 1. Cash   2. Deduct from caution deposit");
            int payChoice = InputHelper.getInt("Choose: ");
            if (payChoice == 2) {
                borrower.deductCaution(totalFine);
                System.out.println("Deducted from caution deposit.");
                System.out.println("Remaining caution: ₹" + borrower.getCautionDeposit());
            } else {
                System.out.println("Please pay ₹" + totalFine + " in cash at the counter.");
            }
        }

        // Mark rental as returned
        rental.setReturned(true);
        rental.setStatus("RETURNED");
        rental.getVehicle().setAvailableCount(rental.getVehicle().getAvailableCount() + 1);
        DataStore.saveToDisk();

        System.out.println("\n✅ Vehicle returned successfully. Thank you!");
    }

    // EXTEND rental
    public static void extendRental(Borrower borrower) {
        System.out.println("\n--- EXTEND RENTAL ---");

        Rental rental = getActiveRental(borrower);
        if (rental == null) {
            System.out.println("No active rental found.");
            return;
        }

        boolean extended = rental.extendDueDate();
        if (extended) {
            System.out.println("Rental extended. New due date: " + rental.getDueDate());
            System.out.println("Extensions used: " + rental.getExtensionCount() + "/2");
            DataStore.saveToDisk();
        } else {
            System.out.println("Cannot extend. Maximum 2 extensions already used.");
        }
    }

    // MARK vehicle as lost
    public static void markAsLost(Borrower borrower) {
        System.out.println("\n--- MARK AS LOST ---");

        Rental rental = getActiveRental(borrower);
        if (rental == null) {
            System.out.println("No active rental found.");
            return;
        }

        rental.setStatus("LOST");
        rental.setReturned(true);

        // Deduct full caution deposit as penalty
        double penalty = borrower.getCautionDeposit();
        borrower.deductCaution(penalty);
        DataStore.saveToDisk();

        System.out.println("Vehicle marked as lost: " + rental.getVehicle().getName());
        System.out.println("Full caution deposit of ₹" + penalty + " has been forfeited.");
    }

    // VIEW rental history for borrower
    public static void viewRentalHistory(Borrower borrower) {
        System.out.println("\n--- YOUR RENTAL HISTORY ---");
        boolean found = false;

        for (Rental r : DataStore.rentals) {
            if (r.getBorrower().getEmail().equals(borrower.getEmail())) {
                System.out.println("Rental ID : " + r.getRentalId());
                System.out.println("Vehicle   : " + r.getVehicle().getName());
                System.out.println("Date      : " + r.getRentalDate());
                System.out.println("Status    : " + r.getStatus());
                System.out.println("KMs driven: " + r.getKmsDriven());
                System.out.println("-----------------------------");
                found = true;
            }
        }

        if (!found) System.out.println("No rental history found.");
    }

    // ─── HELPER METHODS ───────────────────────────────────────

    // Find vehicle by ID or name
    private static Vehicle findVehicle(String input) {
        for (Vehicle v : DataStore.vehicles) {
            if (v.getVehicleId().equalsIgnoreCase(input) ||
                v.getName().equalsIgnoreCase(input)) {
                return v;
            }
        }
        return null;
    }

    // Check if borrower already has an active rental of given type
    private static boolean hasActiveRental(Borrower borrower, String type) {
        for (Rental r : DataStore.rentals) {
            if (r.getBorrower().getEmail().equals(borrower.getEmail()) &&
                r.getVehicle().getType().equals(type) &&
                !r.isReturned()) {
                return true;
            }
        }
        return false;
    }

    // Get the one active rental of a borrower (any type)
    public static Rental getActiveRental(Borrower borrower) {
        for (Rental r : DataStore.rentals) {
            if (r.getBorrower().getEmail().equals(borrower.getEmail()) &&
                !r.isReturned()) {
                return r;
            }
        }
        return null;
    }

    // Get required security deposit based on vehicle type
    private static double getRequiredDeposit(Vehicle v) {
        if (v instanceof Car) return ((Car) v).getSecurityDepositRequired();
        if (v instanceof Bike) return ((Bike) v).getSecurityDepositRequired();
        return 0;
    }

    // Calculate damage fine based on level
    private static double calculateDamageFine(double rentalCharge, String level) {
        switch (level) {
            case "LOW":    return rentalCharge * 0.20;
            case "MEDIUM": return rentalCharge * 0.50;
            case "HIGH":   return rentalCharge * 0.75;
            default:
                System.out.println("Invalid damage level. No fine applied.");
                return 0;
        }
    }

    // for module D - fines and penalties
    
    // EXCHANGE a vehicle during active rental
public static void exchangeVehicle(Borrower borrower) {
    System.out.println("\n--- EXCHANGE VEHICLE ---");

    Rental rental = getActiveRental(borrower);
    if (rental == null) {
        System.out.println("No active rental to exchange.");
        return;
    }

    Vehicle current = rental.getVehicle();
    System.out.println("Current vehicle: " + current.getName() + " (" + current.getType() + ")");
    System.out.println("\nAvailable vehicles for exchange:");

    // Show vehicles of same type only
    boolean found = false;
    for (Vehicle v : DataStore.vehicles) {
        if (v.getType().equals(current.getType()) &&
            !v.getVehicleId().equals(current.getVehicleId()) &&
            v.getAvailableCount() > 0 &&
            v.getTotalKms() < v.getServiceLimit()) {
            v.displayInfo();
            found = true;
        }
    }

    if (!found) {
        System.out.println("No other " + current.getType() + " available for exchange.");
        return;
    }

    String input = InputHelper.getString("\nEnter Vehicle ID or Name to exchange with: ");
    Vehicle newVehicle = findVehicle(input);

    if (newVehicle == null) {
        System.out.println("Vehicle not found.");
        return;
    }

    if (!newVehicle.getType().equals(current.getType())) {
        System.out.println("You can only exchange with the same type of vehicle.");
        return;
    }

    if (newVehicle.getTotalKms() >= newVehicle.getServiceLimit()) {
        System.out.println("That vehicle is due for service. Cannot exchange.");
        return;
    }

    // Do the exchange
    current.setAvailableCount(current.getAvailableCount() + 1); // return old one
    newVehicle.setAvailableCount(newVehicle.getAvailableCount() - 1); // take new one

    rental.getVehicle().addKms(0); // no kms added on exchange
    // Update rental to point to new vehicle
    DataStore.rentals.remove(rental);

    String newRentalId = "R" + DataStore.rentalCounter++;
    Rental newRental = new Rental(newRentalId, borrower, newVehicle, LocalDate.now());
    DataStore.rentals.add(newRental);
    DataStore.saveToDisk();

    System.out.println("✅ Vehicle exchanged successfully!");
    System.out.println("New Vehicle  : " + newVehicle.getName());
    System.out.println("New Rental ID: " + newRentalId);
}
}