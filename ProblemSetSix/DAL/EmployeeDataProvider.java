package ProblemSetSix.DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDataProvider {
    /**
     * Method to get all employees
     * @return
     * @throws SQLException
     */
    public List<Integer> getAllEmployees() throws SQLException {
        Connection conn = DataMgr.getConnection();

        String sql = "SELECT employee_id FROM employees";

        List<Integer> employees = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                employees.add(rs.getInt("employee_id"));
            }
        }

        return employees;
    }

    /**
     * Method to see if an employee is on shift
     * @param employeeId
     * @return
     * @throws SQLException
     */
    public boolean isEmployeeOnShift(int employeeId) throws SQLException {
        Connection conn = DataMgr.getConnection();

        String sql = """
            SELECT 1 FROM employees
            WHERE employee_id = ?
            AND CURRENT_TIME BETWEEN delivery_start_time AND delivery_end_time
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            return rs.next();
        }
    }

    /**
     * Method to get employees dorm room
     * @param employeeId
     * @return
     * @throws SQLException
     */
    public int getEmployeeDorm(int employeeId) throws SQLException {
        Connection conn = DataMgr.getConnection();

        String sql = "SELECT dorm_id FROM employees WHERE employee_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("dorm_id");
            }
        }

        throw new SQLException("Employee dorm not found");
    }

    /**
     * Method to see if employee is busy with a delivery
     * @param employeeId
     * @return
     * @throws SQLException
     */
    public boolean isEmployeeBusy(int employeeId) throws SQLException {

        Connection conn = DataMgr.getConnection();

        String sql = """
            SELECT 1 FROM deliveries
            WHERE employee_id = ?
            AND estimated_delivery_time > NOW()

            UNION

            SELECT 1 FROM service_deliveries
            WHERE employee_id = ?
            AND estimated_delivery_time > NOW()
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);
            stmt.setInt(2, employeeId);

            ResultSet rs = stmt.executeQuery();

            return rs.next();
        }
    }
}
