package ProblemSetSix.BLL;

import ProblemSetSix.DAL.InventoryDataProvider;
import java.util.Scanner;

public class InventoryController {

    private InventoryDataProvider inventoryDP = new InventoryDataProvider();

    /**
     * Method to call addOrUpdateItem from data provider
     * @param name
     * @param category
     * @param quantity
     * @param price
     * @throws Exception
     */
    public void addOrUpdateItem(String name, String category, int quantity, double price) throws Exception {
        inventoryDP.addOrUpdateItem(name, category, quantity, price);
    }

    /**
     * Method to add inventory used in main
     * @param scanner
     * @return
     * @throws Exception
     */
    public String addInventoryInteractive(Scanner scanner) throws Exception {

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
            } catch (Exception e) {
                System.out.println("Invalid quantity.");
            }
        }

        double price;
        while (true) {
            try {
                System.out.print("Price: ");
                price = Double.parseDouble(scanner.nextLine());
                if (price < 0) throw new NumberFormatException();
                break;
            } catch (Exception e) {
                System.out.println("Invalid price.");
            }
        }

        addOrUpdateItem(name, category, quantity, price);

        return "Inventory updated successfully!";
    }
}