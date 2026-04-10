package ProblemSetSix.PresentationLayer;

import java.util.Scanner;

import ProblemSetSix.BLL.InventoryController;
import ProblemSetSix.BLL.OrderController;
import ProblemSetSix.DAL.DataMgr;

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
            controller.addOrUpdateItem(name, quantity, price);
            System.out.println("\nInventory updated successfully!");
        } catch (Exception e) {
            System.out.println("\nError updating inventory: " + e.getMessage());
        }
    }

    private static void handleOrder(Scanner scanner) {
        OrderController controller = new OrderController();
        System.out.println("\n=== Place Order ===");
       

        System.out.print("Enter Student ID: ");
        int studentId = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter Item Name: ");
        String itemName = scanner.nextLine();

        System.out.print("Enter Quantity: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        try {
            String result = controller.placeOrder(studentId, itemName, quantity);
            System.out.println("\n✅ " + result);
        } catch (Exception e) {
            System.out.println("\n❌ " + e.getMessage());
        }
    }
}
