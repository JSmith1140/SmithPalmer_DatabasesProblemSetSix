package ProblemSetSix.DAL;

import java.sql.*;

public class StudentDataProvider {

    /**
     * Method to get student dorm
     * @param studentId
     * @return
     * @throws SQLException
     */
    public int getStudentDorm(int studentId) throws SQLException {
        Connection conn = DataMgr.getConnection();

        String sql = """
            SELECT d.dorm_id
            FROM students s
            JOIN rooms r ON s.room_id = r.room_id
            JOIN dorms d ON r.dorm_id = d.dorm_id
            WHERE s.student_id = ?
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("dorm_id");
            }
        }

        throw new SQLException("Student dorm not found");
    }
}