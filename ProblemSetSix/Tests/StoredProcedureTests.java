package ProblemSetSix.Tests;

import ProblemSetSix.DAL.DataMgr;

import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.*;

public class StoredProcedureTests {

    @BeforeClass
    public static void setupDatabase() throws Exception {
        DataMgr.initialize("username", "password"); // put your username and password here
    }

    @Test
    public void testInventoryProcedureInsert() throws Exception {

        Connection conn = DataMgr.getConnection();

        CallableStatement stmt =
            conn.prepareCall("{CALL add_or_update_inventory(?, ?, ?, ?)}");

        stmt.setString(1, "ProcItem");
        stmt.setString(2, "Snack");
        stmt.setInt(3, 10);
        stmt.setDouble(4, 1.99);

        stmt.execute();

        ResultSet rs = conn.createStatement().executeQuery(
            "SELECT * FROM inventory_items WHERE item_name = 'ProcItem'"
        );

        assertTrue(rs.next());
    }

    @Test
    public void testInventoryProcedureRejectInvalidQuantity() throws Exception {

        Connection conn = DataMgr.getConnection();

        CallableStatement stmt =
            conn.prepareCall("{CALL add_or_update_inventory(?, ?, ?, ?)}");

        stmt.setString(1, "BadItem");
        stmt.setString(2, "Snack");
        stmt.setInt(3, -5);  
        stmt.setDouble(4, 1.99);

        try {
            stmt.execute();
            fail("Expected SQL exception due to invalid quantity");
        } catch (SQLException e) {
            assertTrue(true);
        }
    }
}