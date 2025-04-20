# 🚗 Car Rental Management System

A robust Java-based Car Rental Management System that simulates real-world vehicle rental operations with core features such as vehicle listing, customer data management, and rental tracking. Built with JDBC for database connectivity and modular DAO architecture.

## 📌 Features

- 🔍 View available cars for rent
- 🧍 Customer registration and data management
- 📅 Rent and return vehicle management
- 🗂️ Modular DAO-based architecture (Separation of Concerns)
- 🛢️ JDBC-based database integration
- 🧩 Scalable and maintainable codebase

## 🛠️ Tech Stack

- **Java** (Core + JDBC)
- **MySQL** (or any compatible RDBMS)
- **IDE**:  Eclipse

## 🗃️ Project Structure

```plaintext
src/
├── CarDAO.java          # CRUD operations for cars
├── CustomerDAO.java     # Handles customer data
├── RentalDAO.java       # Manages rentals and returns
├── DBConnection.java    # Database connection logic
└── CarRentalApp.java    # Main class / entry point
```
