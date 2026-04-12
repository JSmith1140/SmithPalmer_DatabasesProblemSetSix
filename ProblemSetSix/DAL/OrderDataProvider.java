package ProblemSetSix.DAL;

import java.sql.*;

public class OrderDataProvider {

    public boolean hasOrderToday(int studentId) throws SQLException {
        Connection conn = DataMgr.getConnection();

        String sql = "SELECT COUNT(*) FROM orders WHERE student_id = ? AND order_date = CURDATE()";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            rs.next();
            return rs.getInt(1) > 0;
        }
    }

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