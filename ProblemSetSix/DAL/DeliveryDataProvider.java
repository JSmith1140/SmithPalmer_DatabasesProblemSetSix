package ProblemSetSix.DAL;

import java.sql.*;
import java.time.LocalDateTime;

public class DeliveryDataProvider {
    public LocalDateTime getNextAvailableSlot() {
        return LocalDateTime.now().plusMinutes(30);
    }

    /**
     * Method to assign orders
     * @param orderId
     * @param employeeId
     * @param time
     * @throws SQLException
     */
    public void assignOrderDelivery(int orderId, int employeeId, LocalDateTime time) throws SQLException {

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

    /**
     * Method to assign services
     * @param serviceOrderId
     * @param employeeId
     * @param time
     * @throws SQLException
     */
    public void assignServiceDelivery(int serviceOrderId, int employeeId, LocalDateTime time) throws SQLException {

        String sql = """
            INSERT INTO service_deliveries (service_order_id, employee_id, estimated_delivery_time)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = DataMgr.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, serviceOrderId);
            stmt.setInt(2, employeeId);
            stmt.setTimestamp(3, java.sql.Timestamp.valueOf(time));

            stmt.executeUpdate();
        }
    }
}