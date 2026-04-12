package ProblemSetSix.BLL;

import ProblemSetSix.DAL.InventoryDataProvider;
import ProblemSetSix.DAL.OrderDataProvider;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class OrderController {

    private InventoryDataProvider inventoryDP = new InventoryDataProvider();
    private OrderDataProvider orderDP = new OrderDataProvider();
    private DeliveryScheduler scheduler = new DeliveryScheduler();

    /**
     * Method to place an order
     * @param studentId
     * @param items
     * @return
     * @throws Exception
     */
    public String placeOrder(int studentId, Map<String, Integer> items) throws Exception {

        if (items == null || items.isEmpty()) {
            throw new Exception("Cannot place empty order.");
        }

        int orderId = orderDP.createOrder(studentId);

        for (String itemName : items.keySet()) {
            int qty = items.get(itemName);

            double price = inventoryDP.getPrice(itemName);
            int itemId = inventoryDP.getItemId(itemName);

            orderDP.addOrderItem(orderId, itemId, qty, price);
        }

        LocalDateTime eta = scheduler.schedule(orderId, studentId, null, false);

        return "Order placed successfully! ETA: " + eta;
    }

    /**
     * Method used in main to place an order
     * @param studentId
     * @param scanner
     * @return
     * @throws Exception
     */
    public String placeOrderInteractive(int studentId, Scanner scanner) throws Exception {

        Map<String, Integer> items = new HashMap<>();

        while (true) {
            System.out.print("Enter Item Name (or 'done'): ");
            String name = scanner.nextLine();

            if (name.equalsIgnoreCase("done")) break;

            System.out.print("Enter Quantity: ");
            int qty = Integer.parseInt(scanner.nextLine());

            if (!inventoryDP.isInStock(name, qty)) {
                System.out.println("Not enough stock for " + name);
                continue;
            }

            items.put(name, qty);
            System.out.println(name + " added to order");
        }

        if (items.isEmpty()) {
            return "No items added. Order cancelled.";
        }

        return placeOrder(studentId, items);
    }
}