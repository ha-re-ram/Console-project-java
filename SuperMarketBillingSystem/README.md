# 🛒 SuperMarket Billing System

A monolith console-based SuperMarket Billing System built in Java, supporting Admin and Customer workflows with cart management, smart payment, and loyalty rewards.

---

## 📁 Project Structure

```
SuperMarketBillingSystem/
│
├── Main.java
│
├── auth/
│   └── AuthService.java
│
├── models/
│   ├── User.java
│   ├── Admin.java
│   ├── Customer.java
│   ├── Product.java
│   ├── CartItem.java
│   └── Bill.java
│
├── data/
│   └── DataStore.java
│
├── services/
│   ├── InventoryService.java
│   ├── CartService.java
│   ├── PaymentService.java
│   └── ReportService.java
│
├── menus/
│   ├── AdminMenu.java
│   └── CustomerMenu.java
│
└── utils/
    └── InputHelper.java
```

---

## ⚙️ How to Compile and Run

```bash
# Navigate to the project root
cd SuperMarketBillingSystem

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
| Admin | admin@market.com | admin123 |
| Customer | hareram@gmail.com | pass123 |

> New users can only be added by an Admin. There is no self-signup.

---

## 📦 Modules

### Module A — Authentication
- Login with Email and Password
- Role-based menu routing (Admin / Customer)
- No public signup — Admin registers all users

### Module B — Inventory Management (Admin)
- Add, modify, and delete products
- View all products sorted by Name or Price
- Search products by Name
- Add new Admins and Customers into the system
- Increase a customer's credit limit and wallet balance

### Module C — Customer Purchase
- View all in-stock products
- Add products to cart by Product ID
- Adding the same product again increases quantity in cart
- View current cart with subtotals and total
- Edit item quantity in cart (set to 0 to remove)
- Remove items from cart individually

### Module D — Payment
- Wallet-based payment system (preloaded ₹1,000 on signup)
- Customers can only purchase within their wallet balance
- Loyalty Points system:
  - 1 point earned per ₹100 spent
  - On accumulating 50 points → ₹100 discount on next bill, points reset
- Big Bill Reward:
  - Bills of ₹5,000 or more → ₹100 cashback added to wallet
  - No loyalty points awarded for big bill purchases
- Itemized bill printed after every successful payment
- Stock automatically reduced after checkout

### Module E — Purchase History (Customer)
- View all past bills with date, bill number, items, and amounts

### Module F — Reports (Admin)
- Low stock products (quantity below 10)
- Products never purchased by any customer
- Customers ranked by total spending (highest first)
- Admins ranked by total sales processed (highest first)

---

## 🧠 Key Concepts Used

- **OOP**: Inheritance (`Admin extends User`, `Customer extends User`)
- **Polymorphism**: `instanceof` checks and casting
- **Collections**: `ArrayList` with lambda-based `Comparator` sorting
- **Date handling**: `java.time.LocalDate`
- **Packages**: Organized into `auth`, `models`, `data`, `services`, `menus`, `utils`
- **Static DataStore**: Shared in-memory state across the application
- **Snapshot pattern**: Cart copied before clearing to preserve bill history

---

## 💡 Reward Logic Summary

| Condition | Reward | Loyalty Points |
|---|---|---|
| Every ₹100 spent (bill < ₹5000) | None | +1 point per ₹100 |
| 50 points accumulated | ₹100 discount on next bill | Reset to 0 |
| Bill total ≥ ₹5000 | ₹100 cashback to wallet | Not awarded |

---

## 📝 Notes

- All data is stored in-memory. Data resets on every run.
- Default products and users are preloaded in `DataStore.init()` for testing.
- Cart is automatically cleared on logout or after successful checkout.
- The serving admin for billing is automatically assigned as the first registered admin.
