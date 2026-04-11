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
    //Unit tests for every code path in this java program, including the BLL and DAL, along with this program's stored procedures, to ensure that all functional dependencies are upheld.
    @Test
    public void testInventoryController() throws Exception {
        InventoryController controller = new InventoryController();
        // Test adding new item
        controller.addOrUpdateItem("Test Item", 10, 5.99);
        // Test updating existing item
        controller.addOrUpdateItem("Test Item", 20, 4.99);
    }
    public void testOrderController() {
        OrderController controller = new OrderController();
        // Test placing a valid order
        String result = controller.placeOrder(1, "Test Item", 5);
        assert(result.contains("Order placed successfully"));
        // Test placing an order with insufficient inventory
        try {
            controller.placeOrder(1, "Test Item", 100);
        } catch (Exception e) {
            assert(e.getMessage().contains("Insufficient inventory"));
        }
    }
    public void testDataMgr() {
        // Test database connection
        try {
            DataMgr.initialize("testUser", "testPass");
            assert(DataMgr.getConnection() != null);
        } catch (Exception e) {
            assert(false); // Fail if connection fails
        } finally {
            DataMgr.closeConnection();
        }
    }
    public void testStoredProcedures() throws Exception {
        // Test stored procedure for adding/updating inventory
        InventoryController controller = new InventoryController();
        controller.addOrUpdateItem("SP Test Item", 15, 3.99);
        // Test stored procedure for placing an order
        OrderController orderController = new OrderController();
        String result = orderController.placeOrder(1, "SP Test Item", 5);
        assert(result.contains("Order placed successfully"));
    }
    public void testFunctionalDependencies() throws Exception {
        // Test that inventory updates reflect in order placement
        InventoryController controller = new InventoryController();
        controller.addOrUpdateItem("Dependency Test Item", 10, 2.99);
        OrderController orderController = new OrderController();
        String result = orderController.placeOrder(1, "Dependency Test Item", 5);
        assert(result.contains("Order placed successfully"));
        // Now update inventory to a lower quantity and test order failure
        controller.addOrUpdateItem("Dependency Test Item", 3, 2.99);
        try {
            orderController.placeOrder(1, "Dependency Test Item", 5);
        } catch (Exception e) {
            assert(e.getMessage().contains("Insufficient inventory"));
        }
    }
    public void testEdgeCases() throws Exception {
        InventoryController controller = new InventoryController();
        // Test adding item with zero quantity
        controller.addOrUpdateItem("Edge Case Item", 0, 1.99);
        // Test placing order for item with zero inventory
        OrderController orderController = new OrderController();
        try {
            orderController.placeOrder(1, "Edge Case Item", 1);
        } catch (Exception e) {
            assert(e.getMessage().contains("Insufficient inventory"));
        }
    }
    public void testInvalidInputs() {
        InventoryController controller = new InventoryController();
        // Test adding item with negative quantity
        try {
            controller.addOrUpdateItem("Invalid Input Item", -5, 1.99);
        } catch (Exception e) {
            assert(e.getMessage().contains("Invalid quantity"));
        }
        // Test adding item with negative price
        try {
            controller.addOrUpdateItem("Invalid Input Item", 5, -1.99);
        } catch (Exception e) {
            assert(e.getMessage().contains("Invalid price"));
        }
    }
}
