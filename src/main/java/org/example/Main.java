package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;

class InventoryItem {
    private int id;
    private String name;
    private int quantity;
    private double price;
    private int userId; // Add user ID for added/updated items

    public InventoryItem() {}

    public InventoryItem(int id, String name, int quantity, double price, int userId) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String toCSV() {
        return id + "," + name + "," + quantity + "," + price + "," + userId;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Quantity: " + quantity + ", Price: " + price + " soms.";
    }
}

public class Main {
    private static final String JSON_FILE_PATH = "src/main/resources/inventory.json";
    private static final String CSV_FILE_PATH = "src/main/resources/inventory.csv";
    private static final String USER_LOG_FILE_PATH = "src/main/resources/user_log.csv";
    private static final String EMAIL_FILE_PATH = "src/main/resources/user_emails.csv";

    private static List<InventoryItem> inventory = new ArrayList<>();
    private static HashMap<String, Integer> userData = new HashMap<>(); // Track user emails
    private static HashMap<String, Integer> userActions = new HashMap<>(); // Track user actions

    private static int userIdCounter = 1; // Unique User ID counter

    public static void main(String[] args) {
        loadInventoryFromJSON();
        loadUserLogs(); // Load existing user logs
        loadUserEmails(); // Load saved emails at startup
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nEnter your email:");
        String email = scanner.nextLine();

        if (!userData.containsKey(email)) {
            userData.put(email, userIdCounter++);
            saveUserEmails(); // Save updated email list
        }

        int userId = userData.get(email);

        while (true) {
            System.out.println("\nInventory Management System:");
            System.out.println("1. Add Item");
            System.out.println("2. View Inventory");
            System.out.println("3. Update Item");
            System.out.println("4. Remove Item");
            System.out.println("5. Export Data");
            System.out.println("6. Import Data");
            System.out.println("7. Generate Report");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear buffer
                trackUserAction(email, choice);

                switch (choice) {
                    case 1 -> addItem(scanner, userId);
                    case 2 -> viewInventory();
                    case 3 -> updateItem(scanner, userId);
                    case 4 -> removeItem(scanner);
                    case 5 -> exportData(scanner);
                    case 6 -> importData(scanner);
                    case 7 -> generateReport();
                    case 8 -> {
                        saveInventoryToJSON();
                        saveUserLogs();
                        saveUserEmails(); // Save emails before exiting
                        System.out.println("Exiting. Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear buffer
            }
        }
    }
    private static void loadUserEmails() {
        try (Scanner fileScanner = new Scanner(new File(EMAIL_FILE_PATH))) {
            while (fileScanner.hasNextLine()) {
                String email = fileScanner.nextLine().trim();
                if (!email.isEmpty() && !userData.containsKey(email)) {
                    userData.put(email, userIdCounter++);
                }
            }
            System.out.println("User emails loaded successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("Email file not found. Starting fresh.");
        }
    }

    private static void saveUserEmails() {
        try (FileWriter writer = new FileWriter(EMAIL_FILE_PATH)) {
            for (String email : userData.keySet()) {
                writer.write(email + "\n");
            }
            System.out.println("User emails saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving user emails: " + e.getMessage());
        }
    }

    private static void trackUserAction(String email, int action) {
        String actionKey = email + " : " + action;
        userActions.put(actionKey, userActions.getOrDefault(actionKey, 0) + 1);
    }

    // Load inventory from JSON
    private static void loadInventoryFromJSON() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File(JSON_FILE_PATH);
            if (file.exists()) {
                InventoryItem[] items = objectMapper.readValue(file, InventoryItem[].class);
                for (InventoryItem item : items) {
                    inventory.add(item);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading inventory: " + e.getMessage());
        }
    }

    // Save inventory to JSON
    private static void saveInventoryToJSON() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(JSON_FILE_PATH), inventory);
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    // Export data to CSV or JSON
    private static void exportData(Scanner scanner) {
        System.out.println("Choose export format: ");
        System.out.println("1. CSV");
        System.out.println("2. JSON");
        int choice = scanner.nextInt();

        if (choice == 1) {
            exportToCSV();
        } else if (choice == 2) {
            saveInventoryToJSON();
            System.out.println("Data exported to JSON successfully!");
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private static void exportToCSV() {
        try (FileWriter writer = new FileWriter(CSV_FILE_PATH)) {
            for (InventoryItem item : inventory) {
                writer.write(item.toCSV() + '\n');
            }
            System.out.println("Data exported to CSV successfully!");
        } catch (IOException e) {
            System.out.println("Error exporting data: " + e.getMessage());
        }
    }

    // Import data from CSV or JSON
    private static void importData(Scanner scanner) {
        System.out.println("Choose import format: ");
        System.out.println("1. CSV");
        System.out.println("2. JSON");
        int choice = scanner.nextInt();

        if (choice == 1) {
            importFromCSV();
        } else if (choice == 2) {
            loadInventoryFromJSON();
            System.out.println("Data imported from JSON successfully!");
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private static void importFromCSV() {
        try (Scanner fileScanner = new Scanner(new File(CSV_FILE_PATH))) {
            inventory.clear(); // Clear current inventory
            while (fileScanner.hasNextLine()) {
                String[] data = fileScanner.nextLine().split(",");
                if (data.length < 5) {
                    System.out.println("Invalid CSV format. Each line should have 5 values (id, name, quantity, price, userId).");
                    continue;
                }

                int id = Integer.parseInt(data[0]);
                String name = data[1];
                int quantity = Integer.parseInt(data[2]);
                double price = Double.parseDouble(data[3]);
                int userId = Integer.parseInt(data[4]); // Extract userId from CSV

                inventory.add(new InventoryItem(id, name, quantity, price, userId)); // Add item with userId
            }
            System.out.println("Data imported from CSV successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("Error importing data: File not found.");
        } catch (NumberFormatException e) {
            System.out.println("Error importing data: Invalid number format in CSV file.");
        }
    }

    private static void addItem(Scanner scanner, int userId) {
        try {
            System.out.print("Enter ID: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Clear buffer
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Quantity: ");
            int quantity = scanner.nextInt();
            System.out.print("Enter Price: ");
            double price = scanner.nextDouble();

            inventory.add(new InventoryItem(id, name, quantity, price, userId));
            System.out.println("Item added successfully!");
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please try again.");
            scanner.nextLine(); // Clear buffer
        }
    }

    private static void viewInventory() {
        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            for (InventoryItem item : inventory) {
                System.out.println(item);
            }
        }
    }

    private static void updateItem(Scanner scanner, int userId) {
        try {
            System.out.print("Enter ID of the item to update: ");
            int id = scanner.nextInt();
            boolean itemFound = false;

            for (InventoryItem item : inventory) {
                if (item.getId() == id) {
                    scanner.nextLine(); // Clear buffer
                    System.out.print("Enter New Name: ");
                    item.setName(scanner.nextLine());
                    System.out.print("Enter New Quantity: ");
                    item.setQuantity(scanner.nextInt());
                    System.out.print("Enter New Price: ");
                    item.setPrice(scanner.nextDouble());
                    item.setUserId(userId);
                    System.out.println("Item updated successfully!");
                    itemFound = true;
                    break;
                }
            }

            if (!itemFound) {
                System.out.println("Item not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please try again.");
            scanner.nextLine(); // Clear buffer
        }
    }

    private static void removeItem(Scanner scanner) {
        try {
            System.out.print("Enter ID of the item to remove: ");
            int id = scanner.nextInt();
            if (inventory.removeIf(item -> item.getId() == id)) {
                System.out.println("Item removed successfully!");
            } else {
                System.out.println("Item not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please try again.");
            scanner.nextLine(); // Clear the invalid input
        }
    }

    private static void loadUserLogs() {
        try (Scanner fileScanner = new Scanner(new File(USER_LOG_FILE_PATH))) {
            if (fileScanner.hasNextLine()) {
                fileScanner.nextLine(); // Skip header row
            }

            while (fileScanner.hasNextLine()) {
                String[] data = fileScanner.nextLine().split(",");

                // Check for proper format (must have exactly 3 fields)
                if (data.length == 3) {
                    String emailAction = data[0] + ":" + data[1];
                    int count = Integer.parseInt(data[2]); // Parse count
                    userActions.put(emailAction, userActions.getOrDefault(emailAction, 0) + count);
                } else {
                    System.out.println("Skipping invalid line: " + Arrays.toString(data));
                }
            }
            System.out.println("User logs loaded successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("User log file not found. Starting fresh.");
        } catch (NumberFormatException e) {
            System.out.println("Error loading user logs: Invalid format.");
        }
    }

    private static void saveUserLogs() {
        try (FileWriter writer = new FileWriter(USER_LOG_FILE_PATH)) {
            writer.write("Email,Action,Count\n");
            for (Map.Entry<String, Integer> entry : userActions.entrySet()) {
                String[] keyParts = entry.getKey().split(":");
                String email = keyParts[0];
                String action = keyParts[1];
                writer.write(email + "," + action + "," + entry.getValue() + "\n");
            }
            System.out.println("User logs saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving user logs: " + e.getMessage());
        }
    }

    private static void generateReport() {
        System.out.println("\nUser Activity Report:");
        HashMap<String, Integer> actionCount = new HashMap<>();

        for (Map.Entry<String, Integer> entry : userActions.entrySet()) {
            String[] keyParts = entry.getKey().split(":");
            String action = keyParts[1];
            actionCount.put(action, actionCount.getOrDefault(action, 0) + entry.getValue());
            System.out.println("Email & Action: " + entry.getKey() + ", Count: " + entry.getValue());
        }

        System.out.println("\nMost Frequent Actions:");
        actionCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> System.out.println("Action: " + entry.getKey() + ", Count: " + entry.getValue()));

        System.out.println("\nTotal Number of Users: " + userData.size()); // Total unique users
    }
}
