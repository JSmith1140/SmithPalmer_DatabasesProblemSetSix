package ProblemSetSix.DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDataProvider {
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

    public List<Integer> getAvailableEmployees(LocalTime time) throws SQLException {
        Connection conn = DataMgr.getConnection();

        String sql = """
            SELECT employee_id
            FROM employees
            WHERE delivery_start_time <= ?
            AND delivery_end_time >= ?
        """;

        List<Integer> employees = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTime(1, Time.valueOf(time));
            stmt.setTime(2, Time.valueOf(time));

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                employees.add(rs.getInt("employee_id"));
            }
        }

        return employees;
    }
}
