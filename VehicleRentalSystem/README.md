# 🚗 Vehicle Rental System

A monolith console-based Vehicle Rental System built in Java, supporting both Admin and Borrower workflows with full rental lifecycle management.

---

## 📁 Project Structure

```
VehicleRentalSystem/
│
├── Main.java
├── AuthService.java
│
├── models/
│   ├── User.java
│   ├── Admin.java
│   ├── Borrower.java
│   ├── Vehicle.java
│   ├── Car.java
│   ├── Bike.java
│   └── Rental.java
│
├── data/
│   └── DataStore.java
│
├── services/
│   ├── VehicleService.java
│   ├── RentalService.java
│   └── ReportService.java
│
├── menus/
│   ├── AdminMenu.java
│   └── BorrowerMenu.java
│
└── utils/
    └── InputHelper.java
```

---

## ⚙️ How to Compile and Run

```bash
# Navigate to the project root
cd VehicleRentalSystem

# Compile
javac -d out -sourcepath . Main.java

# Run
java -cp out Main
```

> Make sure you have **Java 8 or above** installed.

---

## 🔐 Default Credentials

| Role | Email | Password |
|---|---|---|
| Admin | admin@rental.com | admin123 |
| Borrower | hareram@gmail.com | pass123 |

---

## 📦 Modules

### Module A — Authentication
- Login with Email and Password
- Role-based menu routing (Admin / Borrower)
- Borrower self-signup

### Module B — Vehicle Inventory (Admin)
- Add, modify, and delete vehicles (Car / Bike)
- View all vehicles sorted by Name or Available Count
- Search vehicles by Name or Number Plate
- Change security deposit amount for Cars and Bikes

### Module C — Renting a Vehicle (Borrower)
- View available vehicles (excludes out-of-stock and unserviced)
- Rent a vehicle by ID or Name
- Maximum one Car and one Bike at a time
- Security deposit check before renting (₹10,000 for Car, ₹3,000 for Bike)
- Return vehicle with KMs driven input
- Extend rental tenure (max 2 times, consecutive days)
- Exchange rented vehicle with another of same type
- Mark vehicle as lost

### Module D — Fines and Regulations
- Same day return policy
- Extra 15% charge if KMs driven exceeds 500
- Damage fine based on severity:
  - LOW → 20% of rental charge
  - MEDIUM → 50% of rental charge
  - HIGH → 75% of rental charge
- Fine payment: Cash or deduct from caution deposit (₹30,000 initial)
- Vehicles due for service are hidden from catalogue
  - Car: service every 3,000 KMs
  - Bike: service every 1,500 KMs

### Module E — Reports (Admin)
- List of vehicles due for service
- All vehicles sorted by rental price
- Search and filter vehicles by Name, Car, or Bike
- Rental status report: currently rented vs never rented

---

## 🧠 Key Concepts Used

- **OOP**: Inheritance (`Car extends Vehicle`, `Admin extends User`)
- **Polymorphism**: `instanceof` checks and casting
- **Collections**: `ArrayList` with `Comparator` sorting
- **Date handling**: `java.time.LocalDate`
- **Packages**: Organized into `models`, `data`, `services`, `menus`, `utils`
- **Static DataStore**: Shared in-memory data across the entire application

---

## 📝 Notes

- All data is stored in-memory. Data resets on every run.
- Default vehicles and users are preloaded in `DataStore.init()` for testing.
- The caution deposit starts at ₹30,000 per borrower and is deducted for fines or vehicle loss.
