package ProblemSetSix.DAL;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryDataProvider {

    /**
     * Adds a new item or updates an existing one using a stored procedure
     */
    public void addOrUpdateItem(String name, int quantity, double price) throws SQLException {

        Connection conn = DataMgr.getConnection();

        // Stored Procedure Call
        String sql = "{CALL add_or_update_inventory(?, ?, ?)}";

        try (CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, name);
            stmt.setInt(2, quantity);
            stmt.setDouble(3, price);

            stmt.execute();
        }
    }

    public boolean isInStock(String name, int quantity) throws SQLException {
        Connection conn = DataMgr.getConnection();

        String sql = "SELECT quantity FROM inventory WHERE name = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("quantity") >= quantity;
            }
            return false;
        }
    }

    public int getItemId(String name) throws SQLException {
        Connection conn = DataMgr.getConnection();

        String sql = "SELECT item_id FROM inventory WHERE name = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("item_id");
            }
            throw new SQLException("Item not found");
        }
    }
}