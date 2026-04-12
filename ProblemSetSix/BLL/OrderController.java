package ProblemSetSix.BLL;

import java.time.LocalDateTime;
import java.util.Map;

import ProblemSetSix.DAL.*;

public class OrderController {

    private OrderDataProvider orderDP = new OrderDataProvider();
    private InventoryDataProvider inventoryDP = new InventoryDataProvider();
    private DeliveryScheduler deliveryScheduler = new DeliveryScheduler();

    public String placeOrder(int studentId, Map<String, Integer> items) throws Exception {

        if (items == null || items.isEmpty()) {
            throw new Exception("Cannot place empty order.");
        }

        if (orderDP.hasOrderToday(studentId)) {
            throw new Exception("Student has already placed an order today.");
        }

        int orderId = orderDP.createOrder(studentId);

        for (String itemName : items.keySet()) {

            int qty = items.get(itemName);

            int itemId = inventoryDP.getItemId(itemName);
            double price = inventoryDP.getPrice(itemName);

            orderDP.addOrderItem(orderId, itemId, qty, price);

            inventoryDP.reduceStock(itemName, qty);
        }

        LocalDateTime deliveryTime = deliveryScheduler.schedule(orderId, studentId);

        return "Order placed! Estimated delivery: " + deliveryTime;
    }
}