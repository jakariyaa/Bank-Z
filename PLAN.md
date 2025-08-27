# Bank-Z: Project Plan

## 1. Project Overview

Bank-Z is a modern, desktop-based bank management system designed for simplicity, security, and ease of use. It will provide essential banking functionalities for both customers and bank administrators. The application will be built using Java 24 and JavaFX for a rich client experience, with a lightweight SQLite database for data persistence. Maven will be used to manage the project's build lifecycle and dependencies.

## 2. Core Features

### 2.1. User/Customer Features

- **Login:** Secure user authentication.
- **Dashboard:** A summary view of all accounts, balances, and recent transactions.
- **View Accounts:** Detailed view of individual account information and transaction history.
- **Fund Transfer:** Transfer funds between the user's own accounts or to another customer's account.
- **Withdraw/Deposit:** Simulate cash withdrawal and deposit operations.

### 2.2. Admin/Employee Features

- **Admin Login:** Separate, secure login for bank staff.
- **Customer Management:**
  - Create new customers.
  - View and search for existing customers.
  - Update customer information.
- **Account Management:**
  - Open new bank accounts for customers.
  - View all accounts in the system.
  - Freeze or close accounts.
- **Transaction Viewer:** View a log of all transactions in the system.

## 3. Technology Stack

- **Programming Language:** Java 24
- **UI Framework:** JavaFX
- **Database:** SQLite
- **Build & Dependency Management:** Apache Maven
- **UI Enhancements:** ControlsFX (for modern UI controls and validation)
- **Testing:** JUnit 5 (for unit and integration testing)

## 4. Project Structure (Maven)

The project will follow the Standard Directory Layout for Maven projects to ensure maintainability.

```
Bank-Z/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/bankz/
    │   │       ├── App.java           // Main application entry point
    │   │       ├── controllers/       // JavaFX controllers for each view
    │   │       ├── dao/               // Data Access Objects for DB interaction
    │   │       ├── models/            // Plain Old Java Objects (POJOs) for data
    │   │       ├── services/          // Business logic
    │   │       └── util/              // Utility classes (e.g., DB Connector)
    │   └── resources/
    │       └── com/bankz/
    │           ├── fxml/              // FXML layout files
    │           └── styles/            // CSS stylesheets
    └── test/
        └── java/
            └── com/bankz/
                ├── dao/
                └── services/
```

## 5. Database Schema (SQLite)

A preliminary schema design for the SQLite database.

**1. `customers` Table**
| Column | Type | Constraints | Description |
|---|---|---|---|
| `customer_id` | INTEGER | PRIMARY KEY AUTOINCREMENT | Unique identifier for the customer |
| `first_name` | TEXT | NOT NULL | Customer's first name |
| `last_name` | TEXT | NOT NULL | Customer's last name |
| `username` | TEXT | NOT NULL UNIQUE | Unique username for login |
| `password_hash` | TEXT | NOT NULL | Hashed password for security |
| `date_created` | TEXT | NOT NULL | Date the customer profile was created |

**2. `employees` Table**
| Column | Type | Constraints | Description |
|---|---|---|---|
| `employee_id` | INTEGER | PRIMARY KEY AUTOINCREMENT | Unique identifier for the employee |
| `first_name` | TEXT | NOT NULL | Employee's first name |
| `last_name` | TEXT | NOT NULL | Employee's last name |
| `username` | TEXT | NOT NULL UNIQUE | Unique username for admin login |
| `password_hash` | TEXT | NOT NULL | Hashed password for security |
| `role` | TEXT | NOT NULL | e.g., 'Admin', 'Manager' |
| `date_hired` | TEXT | NOT NULL | Date the employee was hired |

**3. `accounts` Table**
| Column | Type | Constraints | Description |
|---|---|---|---|
| `account_id` | INTEGER | PRIMARY KEY AUTOINCREMENT | Unique identifier for the account |
| `customer_id` | INTEGER | NOT NULL, FOREIGN KEY(customer_id) REFERENCES customers(customer_id) | Links to the customer |
| `account_number` | TEXT | NOT NULL UNIQUE | Public account number |
| `account_type` | TEXT | NOT NULL | e.g., 'Checking', 'Savings' |
| `balance` | REAL | NOT NULL DEFAULT 0.0 | Current account balance |
| `date_opened` | TEXT | NOT NULL | Date the account was opened |
| `status` | TEXT | NOT NULL DEFAULT 'ACTIVE' | e.g., 'ACTIVE', 'FROZEN', 'CLOSED' |

**4. `transactions` Table**
| Column | Type | Constraints | Description |
|---|---|---|---|
| `transaction_id` | INTEGER | PRIMARY KEY AUTOINCREMENT | Unique identifier for the transaction |
| `source_account_id` | INTEGER | FOREIGN KEY(source_account_id) REFERENCES accounts(account_id) | The source account (NULL for deposits) |
| `destination_account_id` | INTEGER | FOREIGN KEY(destination_account_id) REFERENCES accounts(account_id) | The destination account (NULL for withdrawals) |
| `type` | TEXT | NOT NULL | 'DEPOSIT', 'WITHDRAWAL', 'TRANSFER' |
| `amount` | REAL | NOT NULL | Transaction amount (always positive) |
| `timestamp` | TEXT | NOT NULL | Date and time of the transaction |
| `description` | TEXT | | Optional description |

## 6. Development Roadmap

### Phase 1: Project Setup & Foundation (1-2 Days)

1.  Initialize Maven project.
2.  Configure `pom.xml` with Java 24, JavaFX, SQLite, and JUnit 5 dependencies.
3.  Create the project directory structure as outlined above.
4.  Implement the main `App.java` class to launch a basic, empty JavaFX window.

### Phase 2: Database and Data Access Layer (DAO) (2-3 Days)

1.  Create a `DatabaseManager` utility class to handle SQLite connections.
2.  Implement the data `models` (Customer, Employee, Account, Transaction).
3.  Develop DAO interfaces and their implementations for each model to handle CRUD operations.
4.  Write unit tests for the DAO layer to ensure database interactions are working correctly.

### Phase 3: Business Logic (Service Layer) (2-3 Days)

1.  Create service classes to orchestrate DAO operations.
2.  Implement core business logic:
    - `AuthenticationService`: Handle customer and employee login.
    - `AccountService`: Manage accounts, transfers, deposits, withdrawals.
    - `CustomerService`: Manage customer creation and updates.
3.  Write unit tests for the service layer.

### Phase 4: User Interface (JavaFX) (4-5 Days)

1.  Design and create FXML files for all views:
    - Login Window (unified for customer/admin)
    - Main Dashboard (Admin and Customer versions)
    - Accounts View
    - Transfer Funds Modal/View
    - New Customer/Account Creation Forms
2.  Create the corresponding JavaFX controller classes for each FXML file.
3.  Develop a modern look and feel using CSS. Apply styling to all UI components.

### Phase 5: Integration & Finalization (2-3 Days)

1.  Connect the JavaFX controllers (UI) to the service layer (business logic).
2.  Perform end-to-end integration testing.
3.  Refine the UI/UX based on testing.
4.  Package the application into an executable JAR file using Maven.

## 7. UI/UX Design Philosophy

- **Clean & Modern:** Utilize a clean layout with ample whitespace. Use a modern font and a consistent color palette.
- **Intuitive:** The user flow should be logical and predictable. Navigation should be clear and easy to use.
- **Responsive:** The UI should resize gracefully.
- **Separation of Concerns:** FXML will be used for layout, CSS for styling, and Java controllers for logic, making the UI easier to maintain and update.

## 8. Testing Strategy

- **Unit Testing:** JUnit 5 will be used to test individual methods in the DAO and Service layers. In-memory databases (like SQLite in-memory mode) will be used to isolate tests from the main database.
- **Integration Testing:** Tests will be written to verify the interaction between the service layer and the database layer, ensuring that business logic correctly manipulates the data.
- **UI Testing:** Manual testing will be conducted to ensure that the UI behaves as expected, all controls work, and the application flow is correct. Automated UI testing is out of scope for this initial version.

## 9. Risks and Mitigations

- **Risk 1: UI Complexity:** JavaFX development can become complex.
  - **Mitigation:** Strictly adhere to the FXML/Controller separation. Use CSS for styling to keep the FXML clean. Re-use components where possible.
- **Risk 2: Data Integrity:** Incorrectly handled transactions could lead to data corruption (e.g., a transfer failing halfway).
  - **Mitigation:** Use database transactions (ACID properties) for all financial operations to ensure that multi-step actions (like transfers) are atomic.
- **Risk 3: Scope Creep:** Adding unplanned features can delay the project.
  - **Mitigation:** Stick to the core features defined in this plan. New features will be documented and considered for a future version.
