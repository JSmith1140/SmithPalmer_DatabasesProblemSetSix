package ProblemSetSix.DAL;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryDataProvider {

    /**
     * Method to add or update item
     * @param name
     * @param category
     * @param quantity
     * @param price
     * @throws SQLException
     */
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

    /**
     * Method to see if an item is in stock
     * @param name
     * @param qty
     * @return
     * @throws SQLException
     */
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

    /**
     * Method to get item id
     * @param itemName
     * @return
     * @throws SQLException
     */
    public int getItemId(String itemName) throws SQLException {

        Connection conn = DataMgr.getConnection();

        String sql = "SELECT item_id FROM inventory_items WHERE item_name = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, itemName);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("item_id");
            }
        }

        throw new SQLException("Item not found: " + itemName);
    }

    /**
     * Method to get price of an item
     * @param name
     * @return
     * @throws SQLException
     */
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