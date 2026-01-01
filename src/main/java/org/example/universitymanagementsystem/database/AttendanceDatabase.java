package org.example.universitymanagementsystem.database;

import org.example.universitymanagementsystem.models.Attendance;
import org.example.universitymanagementsystem.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDatabase {

    public static boolean markAttendance(Attendance attendance) {
        String sql = "INSERT INTO attendance(student_id, date, status, subject_id) VALUES(?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, attendance.getStudentId());
            pstmt.setString(2, attendance.getDate());
            pstmt.setString(3, attendance.getStatus());
            pstmt.setInt(4, attendance.getSubjectId());

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error marking attendance: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static List<Attendance> getAttendanceByStudentId(String studentId) {
        List<Attendance> attendanceList = new ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE student_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Attendance attendance = new Attendance();
                    attendance.setId(rs.getInt("id"));
                    attendance.setStudentId(rs.getString("student_id"));
                    attendance.setDate(rs.getString("date"));
                    attendance.setStatus(rs.getString("status"));
                    attendance.setSubjectId(rs.getInt("subject_id"));
                    attendanceList.add(attendance);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendanceList;
    }
}
