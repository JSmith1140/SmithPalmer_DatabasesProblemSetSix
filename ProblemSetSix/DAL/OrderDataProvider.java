package ProblemSetSix.DAL;

import java.sql.*;

public class OrderDataProvider {

    /**
     * Method to create an order
     * @param studentId
     * @return
     * @throws SQLException
     */
     public int createOrder(int studentId) throws SQLException {
        Connection conn = DataMgr.getConnection();

        String sql = """
            INSERT INTO orders (student_id, order_date, status)
            VALUES (?, CURDATE(), 'Placed')
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, studentId);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        throw new SQLException("Failed to create order");
    }

    /**
     * Method to add an order item
     * @param orderId
     * @param itemId
     * @param qty
     * @param price
     * @throws SQLException
     */
    public void addOrderItem(int orderId, int itemId, int qty, double price) throws SQLException {
        Connection conn = DataMgr.getConnection();

        String sql = """
            INSERT INTO order_items (order_id, item_id, quantity, price_at_purchase)
            VALUES (?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, itemId);
            stmt.setInt(3, qty);
            stmt.setDouble(4, price * qty);
            stmt.executeUpdate();
        }
    }
}