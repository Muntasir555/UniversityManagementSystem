package org.example.universitymanagementsystem.database;

import org.example.universitymanagementsystem.models.Faculty;
import org.example.universitymanagementsystem.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FacultyDatabase {

    public static boolean addFaculty(Faculty faculty) {
        String sql = "INSERT INTO faculty(name, email, department, password) VALUES(?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, faculty.getName());
            pstmt.setString(2, faculty.getEmail());
            pstmt.setString(3, faculty.getDepartment());
            pstmt.setString(4, faculty.getPassword());

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error adding faculty: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static Faculty validateLogin(String email, String password, String department) {
        String sql = "SELECT * FROM faculty WHERE email = ? AND password = ? AND department = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);
            pstmt.setString(3, department);

            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Faculty faculty = new Faculty();
                    faculty.setId(rs.getInt("id"));
                    faculty.setName(rs.getString("name"));
                    faculty.setEmail(rs.getString("email"));
                    faculty.setDepartment(rs.getString("department"));
                    faculty.setPassword(rs.getString("password"));
                    return faculty;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static java.util.List<Faculty> getAllFaculty() {
        java.util.List<Faculty> facultyList = new java.util.ArrayList<>();
        String sql = "SELECT * FROM faculty";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                java.sql.ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Faculty faculty = new Faculty();
                faculty.setId(rs.getInt("id"));
                faculty.setName(rs.getString("name"));
                faculty.setEmail(rs.getString("email"));
                faculty.setDepartment(rs.getString("department"));
                // Not exposing password
                facultyList.add(faculty);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facultyList;
    }

    public static java.util.List<Faculty> getFacultyByDepartment(String department) {
        java.util.List<Faculty> facultyList = new java.util.ArrayList<>();
        String sql = "SELECT * FROM faculty WHERE department = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, department);

            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Faculty faculty = new Faculty();
                    faculty.setId(rs.getInt("id"));
                    faculty.setName(rs.getString("name"));
                    faculty.setEmail(rs.getString("email"));
                    faculty.setDepartment(rs.getString("department"));
                    // Not exposing password
                    facultyList.add(faculty);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facultyList;
    }

    public static boolean updatePassword(int facultyId, String newPassword) {
        String sql = "UPDATE faculty SET password = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, facultyId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
