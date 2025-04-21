# Inventory Management System

## Project Title and Description
### Inventory Management System
This project is a robust and user-friendly system designed to manage inventory operations efficiently. It supports adding, updating, removing, and viewing inventory items, while tracking user activities and providing data persistence via JSON and CSV formats.

---

## Student Name
Esenkulov Tilek

---

## Description
The Inventory Management System simplifies inventory operations and user activity tracking. It features data import/export capabilities in both JSON and CSV formats, along with user logs for accountability. The system integrates analytics through reports to optimize decision-making.

---

## Presentation
https://docs.google.com/presentation/d/1-bFOPfam9f6HheQGshqoXyHEXVJeB7Ua9OY6yj-BaYs/edit#slide=id.ge7f9c668d6_0_23 

---

## Objectives
- To create a reliable and user-friendly inventory management system.
- To ensure traceability and accountability by tracking user actions.
- To offer analytics through activity reports for optimizing inventory processes.
- To implement persistent storage using JSON and CSV file formats.
- To make the system flexible and compatible across different environments.

---

## Project Requirement List
The project requires the following key functionalities:
1. CRUD Operations: Supports Create, Read, Update, and Delete for events.
2. Command Line Interface: Interactive menu for user-friendly CLI-based operations.
3. Input Validation: Prevents empty fields and ensures correct formats for date and guest inputs.
4. Data Persistence: Saves and loads event data using file handling (serialization and CSV).
5. Modular Design: Uses separate classes (Event, EventManager) for clean and reusable code.
6. Report Generation: Provides summary reports like total events and invited guests.
7. Documentation: Code is documented with explanations in README and structured naming conventions.
8. Test Cases & Outputs: Includes example inputs/outputs for all major features (see below).
9. Error Handling: Detects and handles invalid choices, file errors, and format issues.
10. Import/Export CSV: Allows event data to be exported/imported from .csv files.

---

## Documentation
### Algorithms
- **Inventory Tracking:** The inventory is stored in a `List` for easy access and manipulation.
- **User Action Tracking:** Uses `HashMap` to record actions performed by users and their frequency.
- **Data Import/Export:** Implements file I/O operations to handle JSON and CSV files.

### Data Structures
- **ArrayList:** Stores inventory items with attributes like ID, Name, Quantity, Price, and User ID.
- **HashMap:** Tracks user actions and associates them with specific emails.

### Functions/Modules
- **Add Item:** Handles addition of new inventory items, linked to a unique user ID.
- **Update Item:** Modifies existing inventory items and logs updates made by users.
- **Generate Reports:** Analyzes user activity and provides a summary of frequent actions.
- **View Inventory:** Displays the inventory items.
- **Remove Item:** Deletes the inventory item by the given ID.
- **Export Data:** Saves the whole inventory to a file format of choice (CSV, JSON).
- **Import Data:** Downloads the inventory items saved in a file format of choice (CSV, JSON).
- **Exit:** Closes the program and saves current inventory items to a JSON file; and user's activity and information in a CSV files.

### Challenges Faced
- **Data Validation:** Ensuring consistency in CSV and JSON import/export formats.
- **Error Handling:** Managing input mismatches and file I/O errors efficiently.
- **User Activity Analytics:** Designing user-friendly reports while maintaining data accuracy.

---

