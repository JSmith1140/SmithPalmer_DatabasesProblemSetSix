package ProblemSetSix.Tests;

import ProblemSetSix.BLL.DeliveryScheduler;
import ProblemSetSix.BLL.InventoryController;
import ProblemSetSix.BLL.OrderController;
import ProblemSetSix.DAL.DataMgr;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class JavaWorkflowTests {

    @BeforeClass
    public static void setupDatabase() throws Exception {

        DataMgr.initialize("username", "password"); // put your username and password here
    }

    @Test
    public void testEmptyOrderThrowsException() {
        OrderController oc = new OrderController();

        Exception ex = assertThrows(Exception.class, () -> {
            oc.placeOrder(1, new HashMap<>());
        });

        assertEquals("Cannot place empty order.", ex.getMessage());
    }

    @Test
    public void testValidOrder() throws Exception {
        OrderController oc = new OrderController();

        HashMap<String, Integer> items = new HashMap<>();
        items.put("Chips", 2);

        String result = oc.placeOrder(5, items);

        assertTrue(result.contains("Order placed successfully"));
    }

    @Test
    public void testAddInventoryValid() throws Exception {
        InventoryController ic = new InventoryController();

        ic.addOrUpdateItem("TestItem", "Snack", 5, 2.5);

        assertTrue(true);
    }

    @Test
    public void testAddInventoryInvalidQuantity() {
        InventoryController ic = new InventoryController();

        Exception ex = assertThrows(Exception.class, () -> {
            ic.addOrUpdateItem("BadItem", "Snack", -5, 2.5);
        });

        assertNotNull(ex);
    }

    @Test
    public void testSchedulerNoEmployeeAvailable() {
        DeliveryScheduler ds = new DeliveryScheduler();

        Exception ex = assertThrows(Exception.class, () -> {
            ds.schedule(9999, 1, null, false);
        });

        assertTrue(ex.getMessage().contains("No available"));
    }
}