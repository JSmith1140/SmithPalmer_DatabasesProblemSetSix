package ProblemSetSix.PresentationLayer;

import java.util.Scanner;
import ProblemSetSix.BLL.InventoryController;
import ProblemSetSix.BLL.OrderController;
import ProblemSetSix.BLL.ReportController;
import ProblemSetSix.BLL.ServiceController;
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
            System.out.println("3. Library Return");
            System.out.println("4. Generate Report");
            System.out.println("5. Exit");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine();

            InventoryController controller = new InventoryController();

            // switch case for user input
            switch (choice) {
                case "1":
                    handleInventoryInput(scanner, controller);
                    break;
                case "2":
                    handleOrder(scanner);
                    break;
                case "3":
                    handleLibraryReturn(scanner);
                    break;
                case "4":
                    handleReport();
                    break;
                case "5":
                    System.out.println("Exiting system");
                    DataMgr.closeConnection();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * handle inventory input when user presses 1
     * @param scanner
     * @param controller
     */
    private static void handleInventoryInput(Scanner scanner, InventoryController controller) {

        System.out.println("\n=== Enter New Inventory Item ===");

        try {
            String result = controller.addInventoryInteractive(scanner);
            System.out.println("\n" + result);
        } catch (Exception e) {
            System.out.println("\nError updating inventory: " + e.getMessage());
        }
    }

    /**
     * handle order when user presses 2
     * @param scanner
     */
    private static void handleOrder(Scanner scanner) {
        OrderController controller = new OrderController();

        System.out.println("\n=== Place Order ===");

        System.out.print("Enter Student ID: ");
        int studentId = Integer.parseInt(scanner.nextLine());

        try {
            String result = controller.placeOrderInteractive(studentId, scanner);
            System.out.println("\n" + result);
        } catch (Exception e) {
            System.out.println("\n" + e.getMessage());
        }
    }

    /**
     * handle library return when user presses 3
     * @param scanner
     */
    private static void handleLibraryReturn(Scanner scanner) {

        ServiceController controller = new ServiceController();

        System.out.println("\n=== Library Return Request ===");

        System.out.print("Enter Student ID: ");
        int studentId = Integer.parseInt(scanner.nextLine());

        try {
            String result = controller.requestLibraryReturn(studentId);
            System.out.println("\n" + result);
        } catch (Exception e) {
            System.out.println("\nError: " + e.getMessage());
        }
    }

    /**
     * handle a report when user presses 4
     */
    private static void handleReport() {
        ReportController controller = new ReportController();

        try {
            controller.generateWeeklyReport();
        } catch (Exception e) {
            System.out.println("Error generating report: " + e.getMessage());
        }
    }
}
