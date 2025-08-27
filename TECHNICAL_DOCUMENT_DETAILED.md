# Bank-Z: Detailed Technical Documentation

## 1. Architecture Overview

Bank-Z is a desktop-based bank management system built using JavaFX. It follows a layered architecture pattern with clear separation of concerns, combining elements of the Model-View-Controller (MVC) and Data Access Object (DAO) patterns.

### 1.1. Architectural Layers

The application is organized into the following layers:

1. **Presentation Layer (View)**
   - Implemented using JavaFX FXML for UI layout
   - CSS for styling and theming
   - Controllers that handle user interactions and coordinate with services

2. **Business Logic Layer (Service)**
   - Service classes that implement core business logic
   - Orchestrate operations between controllers and data access objects
   - Handle exceptions and business rules

3. **Data Access Layer (DAO)**
   - Data Access Objects that abstract database operations
   - Provide CRUD operations for each entity
   - Handle database connections and transactions

4. **Data Model Layer (Model)**
   - Plain Old Java Objects (POJOs) representing domain entities
   - Encapsulate application data and provide basic behavior

5. **Utility Layer**
   - Helper classes for common operations (database management, password hashing)

### 1.2. Component Relationships

```
┌────────────────────┐
│   Presentation     │
│   (FXML/CSS)       │
└─────────┬──────────┘
          │
┌─────────▼──────────┐
│   Controllers      │
│   (JavaFX)         │
└─────────┬──────────┘
          │
┌─────────▼──────────┐
│   Services         │
│   (Business Logic) │
└─────────┬──────────┘
          │
┌─────────▼──────────┐
│   DAOs             │
│   (Data Access)    │
└─────────┬──────────┘
          │
┌─────────▼──────────┐
│   Database         │
│   (SQLite)         │
└────────────────────┘
```

### 1.3. Key Components

1. **Models**: `Customer`, `Account`, `Employee`, `Transaction` - Represent core domain entities
2. **Controllers**: JavaFX controllers for each UI view (Login, Dashboard, Account Management, etc.)
3. **Services**: Business logic implementations (`AccountService`, `CustomerService`, `AuthenticationService`)
4. **DAOs**: Data access objects for database operations (`CustomerDao`, `AccountDao`, `TransactionDao`)
5. **Utilities**: Helper classes (`DatabaseManager`, `PasswordUtils`)

## 2. Code Flow

### 2.1. Application Startup

1. **Main Entry Point**: `App.java` extends `javafx.application.Application`
   - Initializes the database using `DatabaseManager.initializeDatabase()`
   - Loads the login screen (`login.fxml`)
   - Sets up the primary stage and scene

2. **Database Initialization**: 
   - `DatabaseManager.initializeDatabase()` creates required tables if they don't exist
   - Uses SQLite with a file-based database (`bankz.db`) by default
   - Supports in-memory databases for testing

### 2.2. User Authentication Flow

1. **Login Screen**: `LoginController`
   - Handles username/password input
   - Uses `AuthenticationService` to verify credentials
   - Supports both customer and employee authentication
   - Navigates to appropriate dashboard based on user type

2. **Authentication Service**:
   - `AuthenticationServiceImpl` coordinates with `CustomerDao` and `EmployeeDao`
   - Uses `PasswordUtils` to securely hash and verify passwords
   - Returns authenticated user objects on successful login

### 2.3. Customer Dashboard Flow

1. **Dashboard Controller**: `CustomerDashboardController`
   - Manages navigation between different views (Dashboard, Accounts, Transactions)
   - Loads customer data and displays account information
   - Handles fund transfer, deposit, and withdrawal operations

2. **Account Management**:
   - `AccountService` handles account operations
   - `AccountDao` provides data access for account entities
   - Supports opening new accounts, viewing balances, and managing account status

3. **Transaction Processing**:
   - `TransactionService` (part of `AccountService`) handles financial operations
   - `TransactionDao` records all transaction history
   - Supports deposits, withdrawals, and transfers between accounts

### 2.4. Data Flow

```
User Input → Controller → Service → DAO → Database
                    ↑        ↑      ↑
               Business  Data    SQL
               Logic     Access
```

## 3. Core Logic

### 3.1. Authentication and Security

- **Password Hashing**: `PasswordUtils` uses SHA-256 with random salt generation
- **Secure Authentication**: Separate authentication paths for customers and employees
- **Input Validation**: Basic validation in service layers

### 3.2. Financial Operations

1. **Account Management**:
   - Unique account number generation using UUID
   - Account status tracking (ACTIVE, FROZEN, CLOSED)
   - Balance validation before transactions

2. **Transaction Processing**:
   - Deposit operations increase account balance
   - Withdrawal operations decrease account balance with funds validation
   - Transfer operations atomically move funds between accounts
   - All transactions are recorded with timestamps and descriptions

3. **Exception Handling**:
   - `InsufficientFundsException` for inadequate account balances
   - Proper SQL exception handling with logging

### 3.3. Data Integrity

- **Database Constraints**: Primary keys, foreign keys, and unique constraints
- **Atomic Operations**: Financial transactions update multiple records consistently
- **Data Validation**: Service layer validates inputs before database operations

## 4. File-Level Notes

### 4.1. Main Application Structure

```
src/main/java/com/bankz/
├── App.java                      # Main application entry point
├── controllers/                  # JavaFX controllers for UI views
│   ├── LoginController.java
│   ├── CustomerDashboardController.java
│   ├── EmployeeDashboardController.java
│   ├── Account Management Controllers
│   └── Transaction Controllers
├── models/                       # Domain entities
│   ├── Customer.java
│   ├── Account.java
│   ├── Employee.java
│   └── Transaction.java
├── services/                     # Business logic implementations
│   ├── AuthenticationService.java/Impl
│   ├── AccountService.java/Impl
│   ├── CustomerService.java/Impl
│   └── Exception classes
├── dao/                          # Data access objects
│   ├── Dao.java                 # Generic DAO interface
│   ├── CustomerDao.java/Impl
│   ├── AccountDao.java/Impl
│   ├── EmployeeDao.java/Impl
│   └── TransactionDao.java/Impl
└── util/                         # Utility classes
    ├── DatabaseManager.java
    └── PasswordUtils.java
```

### 4.2. Resource Files

```
src/main/resources/com/bankz/
├── fxml/                         # UI layout files
│   ├── login.fxml
│   ├── customer_dashboard.fxml
│   ├── employee_dashboard.fxml
│   └── Various dialog and form FXML files
└── styles/                       # CSS styling
    └── style.css                 # Application-wide styling
```

### 4.3. Test Structure

```
src/test/java/com/bankz/
├── dao/
│   └── CustomerDaoImplTest.java  # DAO layer testing
└── services/
    ├── AuthenticationServiceImplTest.java
    └── CustomerServiceImplTest.java
```

### 4.4. Configuration Files

- `pom.xml`: Maven build configuration with dependencies
- `PLAN.md`: Project planning and roadmap
- `UI_Guide.md`: UI/UX design standards and guidelines
- `TECHNICAL_DOCUMENT.md`: High-level technical overview

## 5. Key Classes and Their Responsibilities

### 5.1. Models

- **Customer**: Represents bank customers with personal information and authentication data
- **Account**: Represents bank accounts with balances, types, and status
- **Employee**: Represents bank employees with roles and permissions
- **Transaction**: Records all financial activities with source/destination accounts

### 5.2. Controllers

- **LoginController**: Handles user authentication and registration
- **CustomerDashboardController**: Manages customer views and operations
- **EmployeeDashboardController**: Manages employee administrative functions

### 5.3. Services

- **AuthenticationServiceImpl**: Handles user login and registration
- **AccountServiceImpl**: Manages accounts and financial transactions
- **CustomerServiceImpl**: Handles customer data management

### 5.4. DAOs

- **CustomerDaoImpl**: Provides CRUD operations for customers
- **AccountDaoImpl**: Provides CRUD operations for accounts
- **TransactionDaoImpl**: Provides CRUD operations for transactions

### 5.5. Utilities

- **DatabaseManager**: Handles database connections and initialization
- **PasswordUtils**: Provides secure password hashing and verification

## 6. Database Schema

The application uses SQLite with the following tables:

1. **customers**: Customer information and authentication data
2. **employees**: Employee information and authentication data
3. **accounts**: Bank account details and balances
4. **transactions**: Record of all financial transactions

Foreign key relationships ensure data integrity between related entities.

## 7. Improvements

### 7.1. Architecture Improvements

1. **Dependency Injection**: 
   - Current implementation uses direct instantiation of DAOs in controllers
   - Consider using a dependency injection framework to improve testability and reduce coupling

2. **Transaction Management**:
   - Financial operations should use database transactions to ensure atomicity
   - Currently, balance updates and transaction recording are separate operations

3. **Error Handling**:
   - More comprehensive exception handling with user-friendly error messages
   - Centralized error handling mechanism

### 7.2. Security Improvements

1. **Input Validation**:
   - Add more robust input validation at the service layer
   - Implement validation for account numbers, amounts, and other user inputs

2. **Password Security**:
   - Consider using a more modern hashing algorithm like bcrypt or Argon2
   - Implement password strength requirements

3. **Session Management**:
   - Add session timeout functionality
   - Implement secure logout that clears user data

### 7.3. Performance Improvements

1. **Connection Pooling**:
   - Implement database connection pooling for better performance
   - Current implementation creates a new connection for each operation

2. **Caching**:
   - Cache frequently accessed data to reduce database queries
   - Implement caching for user sessions and account information

### 7.4. Code Quality Improvements

1. **Logging**:
   - Replace `printStackTrace()` with proper logging framework (e.g., SLF4J)
   - Add more detailed logging for debugging and monitoring

2. **Code Documentation**:
   - Add more comprehensive JavaDoc comments
   - Document complex business logic and edge cases

3. **Testing Coverage**:
   - Increase unit test coverage for service and DAO layers
   - Add integration tests for end-to-end functionality
   - Implement UI testing for critical user flows

### 7.5. Feature Improvements

1. **Audit Trail**:
   - Add comprehensive audit logging for all user actions
   - Track changes to customer and account information

2. **Reporting**:
   - Add financial reporting capabilities
   - Generate transaction summaries and account statements

3. **User Experience**:
   - Add loading indicators for long-running operations
   - Implement better form validation with real-time feedback
   - Add keyboard shortcuts for common operations