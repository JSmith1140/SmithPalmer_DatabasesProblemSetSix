package ProblemSetSix.DAL;

import java.sql.*;

public class ServiceOrderDataProvider {

    /**
     * Method to create a service order
     * @param studentId
     * @param serviceId
     * @return
     * @throws SQLException
     */
    public int createServiceOrder(int studentId, int serviceId) throws SQLException {

        Connection conn = DataMgr.getConnection();

        String sql = """
            INSERT INTO service_orders (student_id, service_id, order_date, status)
            VALUES (?, ?, CURDATE(), 'Scheduled')
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, studentId);
            stmt.setInt(2, serviceId);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }

        throw new SQLException("Failed to create service order");
    }
}

