# ☕ Java Console Projects Collection

![Java](https://img.shields.io/badge/Java-8+-orange?style=for-the-badge&logo=java)
![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Complete-green?style=for-the-badge)

This repository is a comprehensive, monolith collection of three console-based applications built in Java. Each project demonstrates core **Object-Oriented Programming (OOP)**, **Clean Architecture**, and robust logic for real-world scenarios.

---

## 📽️ Projects in this Repository

### 1. 📚 Library Management System
A full-featured system for managing a library's book inventory, borrower lifecycles, and fine calculations.
- **Key Features**: Admin/Borrower roles, exponential fine growth, book inventory limits, and detailed reporting.
- **Concepts**: Inheritance, Static DataStore, exponential math logic.
- [🔗 View Project Details](./LibraryManagementSystem/README.md)

### 2. 🛒 SuperMarket Billing System
A robust retail billing system with cart management and a smart loyalty rewards engine.
- **Key Features**: Wallet-based payments, itemized billing, loyalty points, and cashback rewards for high-value bills.
- **Concepts**: Snapshot patterns (for billing), Collections sorting, reward logic.
- [🔗 View Project Details](./SuperMarketBillingSystem/README.md)

### 3. 🚗 Vehicle Rental System
A rental platform for cars and bikes with security deposit checks and damage-based fine management.
- **Key Features**: Categorized vehicle rentals, KM-based service tracking, and damage severity fines.
- **Concepts**: Polymorphism (Car/Bike under Vehicle), date handling, service intervals.
- [🔗 View Project Details](./VehicleRentalSystem/README.md)

---

## 🚀 How to Run

Each project is self-contained. To run any of them, follow these general steps:

1. **Navigate to the project folder**:
   ```bash
   cd [ProjectName]
   ```

2. **Compile the project**:
   ```bash
   javac -d out -sourcepath . Main.java
   ```

3. **Run the application**:
   ```bash
   java -cp out Main
   ```

> **Requirement**: Java 8 or above.

---

## 🧠 Common Architecture Patterns
Across all three projects, the following design choices were made:
- **Monolith Clean Architecture**: Separated into `auth`, `models`, `data`, `services`, `menus`, and `utils`.
- **In-Memory Storage**: Uses a static `DataStore` to manage state during runtime (resets on restart).
- **Role-Based Access Control (RBAC)**: Different menus and capabilities for Admins and Customers/Borrowers.

---

## 👤 Author
Developed with ❤️ by [ha-re-ram](https://github.com/ha-re-ram)
