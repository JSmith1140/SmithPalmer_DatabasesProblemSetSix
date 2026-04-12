package ProblemSetSix.PresentationLayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import ProblemSetSix.BLL.InventoryController;
import ProblemSetSix.BLL.OrderController;
import ProblemSetSix.DAL.DataMgr;
import ProblemSetSix.DAL.InventoryDataProvider;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter DB username: ");
        String user = scanner.nextLine();

        System.out.print("Enter DB password: ");
        String pass = scanner.nextLine();

        try {
            DataMgr.initialize(user, pass);
            System.out.println("Connected to database successfully!");
        } catch (Exception e) {
            System.out.println("Connection failed: " + e.getMessage());
            return;
        }

        while (true) {
            System.out.println("\n===== DormDash Menu =====");
            System.out.println("1. Enter New Inventory");
            System.out.println("2. Submit a DormDash Order");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine();

            InventoryController controller = new InventoryController();

            switch (choice) {
                case "1":
                    handleInventoryInput(scanner, controller);
                    break;
                case "2":
                    handleOrder(scanner);
                    break;
                case "3":
                    System.out.println("Exiting system");
                    DataMgr.closeConnection();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void handleInventoryInput(Scanner scanner, InventoryController controller) {
        System.out.println("\n=== Enter New Inventory Item ===");

        System.out.print("Item Name: ");
        String name = scanner.nextLine();

        String category;
        while (true) {
            System.out.print("Category (Snack / Beverage / School Supply): ");
            category = scanner.nextLine().trim();

            if (category.equalsIgnoreCase("Snack") ||
                category.equalsIgnoreCase("Beverage") ||
                category.equalsIgnoreCase("School Supply")) {
                break;
            } else {
                System.out.println("Please enter a valid category.");
            }
        }

        int quantity;
        while (true) {
            try {
                System.out.print("Quantity: ");
                quantity = Integer.parseInt(scanner.nextLine());
                if (quantity < 0) throw new NumberFormatException();
                break;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid non-negative number.");
            }
        }

        double price;
        while (true) {
            try {
                System.out.print("Price: ");
                price = Double.parseDouble(scanner.nextLine());
                if (price < 0) throw new NumberFormatException();
                break;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid non-negative price.");
            }
        }

        try {
            controller.addOrUpdateItem(name, category, quantity, price);
            System.out.println("\nInventory updated successfully!");
        } catch (Exception e) {
            System.out.println("\nError updating inventory: " + e.getMessage());
        }
    }

    private static void handleOrder(Scanner scanner) {
        OrderController controller = new OrderController();
        InventoryDataProvider inventoryDP = new InventoryDataProvider();

        System.out.println("\n=== Place Order ===");

        System.out.print("Enter Student ID: ");
        int studentId = Integer.parseInt(scanner.nextLine());

        Map<String, Integer> items = new HashMap<>();

        while (true) {
            System.out.print("Enter Item Name (or 'done'): ");
            String name = scanner.nextLine();

            if (name.equalsIgnoreCase("done")) break;

            System.out.print("Enter Quantity: ");
            int qty = Integer.parseInt(scanner.nextLine());

            try {
                if (!inventoryDP.isInStock(name, qty)) {
                    System.out.println("Not enough stock for " + name);
                    continue;
                }

                items.put(name, qty);
                System.out.println(name + " added to order");

            } catch (Exception e) {
                System.out.println("Error checking item: " + e.getMessage());
            }
        }

        if (items.isEmpty()) {
            System.out.println("\n No items added. Order cancelled.");
            return;
        }

        try {
            String result = controller.placeOrder(studentId, items);
            System.out.println("\n" + result);
        } catch (Exception e) {
            System.out.println("\n" + e.getMessage());
        }
    }
}
