package ProblemSetSix.Tests;

import ProblemSetSix.DAL.DataMgr;
import ProblemSetSix.DAL.EmployeeDataProvider;
import ProblemSetSix.DAL.InventoryDataProvider;
import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;

public class FunctionalDependencyTests {

    @BeforeClass
    public static void setupDatabase() throws Exception {

        DataMgr.initialize("username", "password"); // put your username and password here
    }

    @Test
    public void testItemPriceConsistency() throws Exception {

        InventoryDataProvider dp = new InventoryDataProvider();

        double price1 = dp.getPrice("Chips");
        double price2 = dp.getPrice("Chips");

        assertEquals(price1, price2, 0.01);
    }

    @Test
    public void testEmployeeDormDependency() throws Exception {

        EmployeeDataProvider dp = new EmployeeDataProvider();

        int dorm1 = dp.getEmployeeDorm(2);
        int dorm2 = dp.getEmployeeDorm(2);

        assertEquals(dorm1, dorm2);
    }
}