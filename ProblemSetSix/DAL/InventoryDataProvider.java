package ProblemSetSix.DAL;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryDataProvider {

    public void addOrUpdateItem(String name, String category, int quantity, double price) throws SQLException {

        Connection conn = DataMgr.getConnection();

        String sql = "{CALL add_or_update_inventory(?, ?, ?, ?)}";

        try (CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, category);
            stmt.setInt(3, quantity);
            stmt.setDouble(4, price);

            stmt.execute();
        }
    }

    public boolean isInStock(String name, int qty) throws SQLException {
        Connection conn = DataMgr.getConnection();

        String sql = "SELECT quantity_available FROM inventory_items WHERE item_name = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("quantity_available") >= qty;
            }
        }

        return false;
    }

    public int getItemId(String name) throws SQLException {
        Connection conn = DataMgr.getConnection();

        String sql = "SELECT item_id FROM inventory_items WHERE item_name = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("item_id");
            }
        }

        throw new SQLException("Item not found");
    }

    public void reduceStock(String name, int qty) throws SQLException {
        Connection conn = DataMgr.getConnection();

        String sql = """
            UPDATE inventory_items
            SET quantity_available = quantity_available - ?
            WHERE item_name = ?
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, qty);
            stmt.setString(2, name);
            stmt.executeUpdate();
        }
    }

    public double getPrice(String name) throws SQLException {
        Connection conn = DataMgr.getConnection();

        String sql = "SELECT price FROM inventory_items WHERE item_name = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("price");
            }
        }

        throw new SQLException("Item not found: " + name);
}
}