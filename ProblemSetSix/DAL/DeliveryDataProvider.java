package ProblemSetSix.DAL;

import java.sql.*;
import java.time.LocalDateTime;

public class DeliveryDataProvider {
    public LocalDateTime getNextAvailableSlot() {
        return LocalDateTime.now().plusMinutes(30);
    }

    public void assignDelivery(int orderId, int employeeId, LocalDateTime time) throws SQLException {
        Connection conn = DataMgr.getConnection();

        String sql = """
            INSERT INTO deliveries (order_id, employee_id, estimated_delivery_time)
            VALUES (?, ?, ?)
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, employeeId);
            stmt.setTimestamp(3, Timestamp.valueOf(time));
            stmt.executeUpdate();
        }
    }

    public boolean isEmployeeBusy(int employeeId) throws SQLException {

        Connection conn = DataMgr.getConnection();

        String sql = """
            SELECT 1
            FROM deliveries d
            JOIN orders o ON d.order_id = o.order_id
            WHERE d.employee_id = ?
            AND o.order_date = CURDATE()
            LIMIT 1
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            return rs.next();
        }
    }
}