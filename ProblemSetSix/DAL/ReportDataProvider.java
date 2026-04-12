package ProblemSetSix.DAL;

import java.sql.*;
import java.util.*;

public class ReportDataProvider {

    public static class DeliveryRecord {
        public int employeeId;
        public int dormId;        
        public String dormName;
        public Timestamp time;
        public String itemName;
        public int quantity;
        public boolean isService;

        // constructor
        public DeliveryRecord(int employeeId, int dormId, String dormName,
                            Timestamp time, String itemName,
                            int quantity, boolean isService) {
            this.employeeId = employeeId;
            this.dormId = dormId;
            this.dormName = dormName;
            this.time = time;
            this.itemName = itemName;
            this.quantity = quantity;
            this.isService = isService;
        }
    }

    /**
     * Method to get all deliveries
     * @return
     * @throws SQLException
     */
    public List<DeliveryRecord> getAllDeliveries() throws SQLException {

        Connection conn = DataMgr.getConnection();
        List<DeliveryRecord> list = new ArrayList<>();

        String ordersSql = """
           SELECT d.employee_id, dorm.dorm_id, dorm.dorm_name, d.estimated_delivery_time,
                i.item_name, di.quantity
            FROM deliveries d
            JOIN orders o ON d.order_id = o.order_id
            JOIN students s ON o.student_id = s.student_id
            JOIN rooms r ON s.room_id = r.room_id
            JOIN dorms dorm ON r.dorm_id = dorm.dorm_id
            JOIN delivery_items di ON d.delivery_id = di.delivery_id
            JOIN inventory_items i ON di.item_id = i.item_id
        """;

        try (PreparedStatement stmt = conn.prepareStatement(ordersSql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new DeliveryRecord(
                        rs.getInt("employee_id"),
                        rs.getInt("dorm_id"),
                        rs.getString("dorm_name"),   
                        rs.getTimestamp("estimated_delivery_time"),
                        rs.getString("item_name"),
                        rs.getInt("quantity"),
                        false
                ));
            }
        }

        String serviceSql = """
            SELECT sd.employee_id, dorm.dorm_id, dorm.dorm_name, sd.estimated_delivery_time
            FROM service_deliveries sd
            JOIN service_orders so ON sd.service_order_id = so.service_order_id
            JOIN students s ON so.student_id = s.student_id
            JOIN rooms r ON s.room_id = r.room_id
            JOIN dorms dorm ON r.dorm_id = dorm.dorm_id
        """;

        try (PreparedStatement stmt = conn.prepareStatement(serviceSql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new DeliveryRecord(
                        rs.getInt("employee_id"),
                        rs.getInt("dorm_id"),
                        rs.getString("dorm_name"),
                        rs.getTimestamp("estimated_delivery_time"),
                        "Library Return",
                        1,
                        true
                ));
            }
        }

        return list;
    }
}