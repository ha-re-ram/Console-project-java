# 📚 Library Management System

A monolith console-based Library Management System built in Java, supporting Admin and Borrower workflows with full book borrowing lifecycle, fine management, and detailed reporting.

---

## 📁 Project Structure

```
LibraryManagementSystem/
│
├── Main.java
│
├── auth/
│   └── AuthService.java
│
├── models/
│   ├── User.java
│   ├── Admin.java
│   ├── Borrower.java
│   ├── Book.java
│   ├── BorrowRecord.java
│   └── Fine.java
│
├── data/
│   └── DataStore.java
│
├── services/
│   ├── BookService.java
│   ├── BorrowService.java
│   ├── FineService.java
│   └── ReportService.java
│
├── menus/
│   ├── AdminMenu.java
│   └── BorrowerMenu.java
│
└── utils/
    ├── InputHelper.java
    └── DateHelper.java
```

---

## ⚙️ How to Compile and Run

```bash
# Navigate to the project root
cd LibraryManagementSystem

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
| Admin | admin@library.com | admin123 |
| Borrower | hareram@gmail.com | pass123 |

> New users can only be added by an Admin. There is no self-signup.

---

## 📦 Modules

### Module A — Authentication
- Login with Email and Password
- Role-based menu routing (Admin / Borrower)
- No public signup — Admin registers all users
- Borrower menu shows live caution deposit, security deposit, and active borrow count on every screen

### Module B — Book Inventory Management (Admin)
- Add, modify, and delete books
- Cannot delete a book if copies are currently borrowed
- View all books sorted by Title or Available Quantity
- Search books by Title or ISBN
- Manage borrower fine limit:
  - Set security deposit (minimum ₹500 required to borrow)
  - Add to caution deposit
- Add new Admins and Borrowers into the system

### Module C — Borrowing Books (Borrower)
- View all available books (only in-stock books shown)
- Borrow a book by Book ID, ISBN, or Title
- Maximum 3 books borrowed at a time
- Cannot borrow the same book twice simultaneously
- Minimum security deposit of ₹500 required to borrow
- Return a book with user-provided return date (DD/MM/YYYY)
- Extend borrow tenure (max 2 times, 15 days each extension)
- Exchange a borrowed book with another available book
- Mark a book as lost
- Report membership card as lost

### Module D — Fines and Regulations
- Late return fine with exponential growth:
  - Base rate: ₹2 per day for first 10 days late
  - Doubles every subsequent 10-day period (₹4/day, ₹8/day...)
  - Capped at 80% of the book's cost — whichever is lower
- Lost book fine: 50% of book's actual cost
- Lost membership card fine: flat ₹10
- Fine payment options:
  - Pay by cash
  - Deduct from caution deposit (₹1,500 preloaded on signup)
  - Partial deduction if caution is insufficient — remainder by cash
- All fines recorded with reason, amount, and payment mode

### Module E — Reports

**Admin Reports:**
1. Low stock books (available quantity below 3)
2. Books never borrowed by anyone
3. Heavily borrowed books sorted by borrow count
4. Outstanding unreturned books as on a given date
5. Book status by ISBN — shows who has borrowed it and expected return date

**Borrower Reports:**
1. Personal fine history with reason and payment status
2. Personal borrow history with dates and return status

---

## 🧠 Key Concepts Used

- **OOP**: Inheritance (`Admin extends User`, `Borrower extends User`)
- **Polymorphism**: `instanceof` checks and casting
- **Collections**: `ArrayList` with lambda-based `Comparator` sorting
- **Date handling**: `java.time.LocalDate`, `ChronoUnit.DAYS`, `DateTimeFormatter`
- **Exponential calculation**: `Math.pow()` for fine growth per 10-day period
- **Packages**: Organized into `auth`, `models`, `data`, `services`, `menus`, `utils`
- **Static DataStore**: Shared in-memory state across the entire application

---

## 💡 Fine Calculation Summary

| Scenario | Fine |
|---|---|
| Days 1–10 late | ₹2 per day |
| Days 11–20 late | ₹4 per day |
| Days 21–30 late | ₹8 per day |
| Every next 10 days | Doubles from previous period |
| Maximum cap | 80% of book cost |
| Lost book | 50% of book cost |
| Lost membership card | ₹10 flat |

### Example
Book cost ₹450, returned 25 days late:
- Days 1–10 → 10 × ₹2 = ₹20
- Days 11–20 → 10 × ₹4 = ₹40
- Days 21–25 → 5 × ₹8 = ₹40
- Total = ₹100 → Cap = ₹360 → **Final fine = ₹100**

---

## 📝 Notes

- All data is stored in-memory. Data resets on every run.
- Default books and users are preloaded in `DataStore.init()` for testing.
- Return date is entered manually by the user in DD/MM/YYYY format.
- Caution deposit starts at ₹1,500 per borrower and is refunded on account closure.
- Security deposit (minimum ₹500) must be set by admin before a borrower can borrow.
