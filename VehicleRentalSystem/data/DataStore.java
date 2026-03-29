package data;
import models.*;
import java.util.ArrayList;
import java.io.*;
import java.time.LocalDate;

public class DataStore {
    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<Vehicle> vehicles = new ArrayList<>();
    public static ArrayList<Rental> rentals = new ArrayList<>();
    public static int rentalCounter = 1; // for generating rental IDs

    private static final String USERS_FILE = "data/users.csv";
    private static final String VEHICLES_FILE = "data/vehicles.csv";
    private static final String RENTALS_FILE = "data/rentals.csv";

    // Preload some default data so we can test without typing everything
    public static void init() {
        // Try to load from disk first
        if (!loadFromDisk()) {
            // If no saved data, create defaults
            users.add(new Admin("Admin", "admin@rental.com", "admin123"));

            Borrower b = new Borrower("Ha-re-Ram", "hareram@gmail.com", "pass123");
            b.setSecurityDeposit(10000);
            users.add(b);

            vehicles.add(new Car("C001", "Toyota Innova", "TN01AB1234", 1500, 3));
            vehicles.add(new Car("C002", "Honda City", "TN02CD5678", 1200, 2));
            vehicles.add(new Bike("B001", "Royal Enfield", "TN03EF9012", 500, 4));
            vehicles.add(new Bike("B002", "Honda Activa", "TN04GH3456", 300, 5));
        }
    }

    // Save all data to CSV files
    public static void saveToDisk() {
        try {
            new File("data").mkdirs(); // ensure data folder exists
            saveUsers();
            saveVehicles();
            saveRentals();
        } catch (Exception e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    // Load all data from CSV files
    public static boolean loadFromDisk() {
        try {
            if (!new File(USERS_FILE).exists()) {
                return false; // no saved data
            }
            loadUsers();
            loadVehicles();
            loadRentals();
            return true;
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
            return false;
        }
    }

    // Save users to CSV: role,name,email,password,securityDeposit,cautionDeposit
    private static void saveUsers() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (User u : users) {
                if (u instanceof Borrower) {
                    Borrower b = (Borrower) u;
                    writer.println("BORROWER," + b.getName() + "," + b.getEmail() + "," +
                                 b.getPassword() + "," + b.getSecurityDeposit() + "," + b.getCautionDeposit());
                } else if (u instanceof Admin) {
                    writer.println("ADMIN," + u.getName() + "," + u.getEmail() + "," + u.getPassword());
                }
            }
        }
    }

    // Load users from CSV
    private static void loadUsers() throws IOException {
        users.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals("ADMIN")) {
                    users.add(new Admin(parts[1], parts[2], parts[3]));
                } else if (parts[0].equals("BORROWER")) {
                    Borrower b = new Borrower(parts[1], parts[2], parts[3]);
                    b.setSecurityDeposit(Double.parseDouble(parts[4]));
                    // cautionDeposit is set in constructor, can be modified if needed
                    users.add(b);
                }
            }
        }
    }

    // Save vehicles to CSV: vehicleId,name,numberPlate,type,rentalPrice,availableCount,totalKms,isAvailable
    private static void saveVehicles() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(VEHICLES_FILE))) {
            for (Vehicle v : vehicles) {
                writer.println(v.getVehicleId() + "," + v.getName() + "," + v.getNumberPlate() + "," +
                             v.getType() + "," + v.getRentalPrice() + "," + v.getAvailableCount() + "," +
                             v.getTotalKms() + "," + v.isAvailable());
            }
        }
    }

    // Load vehicles from CSV
    private static void loadVehicles() throws IOException {
        vehicles.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(VEHICLES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Vehicle v;
                if (parts[3].equals("CAR")) {
                    v = new Car(parts[0], parts[1], parts[2], Double.parseDouble(parts[4]), Integer.parseInt(parts[5]));
                } else {
                    v = new Bike(parts[0], parts[1], parts[2], Double.parseDouble(parts[4]), Integer.parseInt(parts[5]));
                }
                v.addKms(Double.parseDouble(parts[6]) - v.getTotalKms()); // set total kms
                v.setAvailable(Boolean.parseBoolean(parts[7]));
                vehicles.add(v);
            }
        }
    }

    // Save rentals to CSV: rentalId,borrowerEmail,vehicleId,rentalDate,dueDate,returned,extensionCount,kmsDriven,status
    private static void saveRentals() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RENTALS_FILE))) {
            for (Rental r : rentals) {
                writer.println(r.getRentalId() + "," + r.getBorrower().getEmail() + "," +
                             r.getVehicle().getVehicleId() + "," + r.getRentalDate() + "," +
                             r.getDueDate() + "," + r.isReturned() + "," + r.getExtensionCount() + "," +
                             r.getKmsDriven() + "," + r.getStatus());
            }
        }
    }

    // Load rentals from CSV
    private static void loadRentals() throws IOException {
        rentals.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(RENTALS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                // Find borrower and vehicle by email and ID
                Borrower borrower = null;
                Vehicle vehicle = null;

                for (User u : users) {
                    if (u instanceof Borrower && u.getEmail().equals(parts[1])) {
                        borrower = (Borrower) u;
                        break;
                    }
                }

                for (Vehicle v : vehicles) {
                    if (v.getVehicleId().equals(parts[2])) {
                        vehicle = v;
                        break;
                    }
                }

                if (borrower != null && vehicle != null) {
                    Rental r = new Rental(parts[0], borrower, vehicle, LocalDate.parse(parts[3]));
                    r.setReturned(Boolean.parseBoolean(parts[5]));
                    r.setKmsDriven(Double.parseDouble(parts[7]));
                    r.setStatus(parts[8]);
                    // Note: We can't fully restore extensionCount and dueDate from this simple format
                    // For now, dueDate is set in constructor. In production, you'd store it too.
                    rentals.add(r);
                }
            }
        }
    }
}