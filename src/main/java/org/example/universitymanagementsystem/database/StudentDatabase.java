package org.example.universitymanagementsystem.database;

import org.example.universitymanagementsystem.models.Student;
import org.example.universitymanagementsystem.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StudentDatabase {

    public static boolean addStudent(Student student) {
        String countSql = "SELECT COUNT(*) FROM students WHERE department = ? AND batch = ?";
        String insertSql = "INSERT INTO students(id, name, email, department, batch, password) VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection()) {
            // Generate ID
            int sequence = 1;
            try (PreparedStatement countStmt = conn.prepareStatement(countSql)) {
                countStmt.setString(1, student.getDepartment());
                countStmt.setString(2, student.getBatch());
                try (java.sql.ResultSet rs = countStmt.executeQuery()) {
                    if (rs.next()) {
                        sequence = rs.getInt(1) + 1;
                    }
                }
            }

            // Format: Batch + Department + Sequence (e.g., 22CSE01)
            // Assuming sequence should be at least 2 digits
            String generatedId = String.format("%s%s%02d", student.getBatch(), student.getDepartment(), sequence);
            student.setId(generatedId);

            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setString(1, student.getId());
                pstmt.setString(2, student.getName());
                pstmt.setString(3, student.getEmail());
                pstmt.setString(4, student.getDepartment());
                pstmt.setString(5, student.getBatch());
                pstmt.setString(6, student.getPassword());

                pstmt.executeUpdate();
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error adding student: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static Student validateLogin(String email, String password, String department) {
        String sql = "SELECT * FROM students WHERE email = ? AND password = ? AND department = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);
            pstmt.setString(3, department);

            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getString("id"));
                    student.setName(rs.getString("name"));
                    student.setEmail(rs.getString("email"));
                    student.setDepartment(rs.getString("department"));
                    student.setBatch(rs.getString("batch"));
                    student.setPassword(rs.getString("password"));
                    student.setCgpa(rs.getDouble("cgpa"));
                    return student;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Student getStudentById(String id) {
        String sql = "SELECT * FROM students WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getString("id"));
                    student.setName(rs.getString("name"));
                    student.setEmail(rs.getString("email"));
                    student.setDepartment(rs.getString("department"));
                    student.setBatch(rs.getString("batch"));
                    student.setCgpa(rs.getDouble("cgpa"));
                    return student;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Student not found
    }

    public static java.util.List<Student> getAllStudents() {
        java.util.List<Student> students = new java.util.ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                java.sql.ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getString("id"));
                student.setName(rs.getString("name"));
                student.setEmail(rs.getString("email"));
                student.setDepartment(rs.getString("department"));
                student.setBatch(rs.getString("batch"));
                student.setCgpa(rs.getDouble("cgpa"));
                // Not exposing password
                students.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public static java.util.List<Student> getStudentsByDepartment(String department) {
        java.util.List<Student> students = new java.util.ArrayList<>();
        String sql = "SELECT * FROM students WHERE department = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, department);

            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getString("id"));
                    student.setName(rs.getString("name"));
                    student.setEmail(rs.getString("email"));
                    student.setDepartment(rs.getString("department"));
                    student.setBatch(rs.getString("batch"));
                    student.setCgpa(rs.getDouble("cgpa"));
                    // Not exposing password
                    students.add(student);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public static java.util.List<Student> getStudentsByBatch(String batch) {
        java.util.List<Student> students = new java.util.ArrayList<>();
        String sql = "SELECT * FROM students WHERE batch = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, batch);

            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getString("id"));
                    student.setName(rs.getString("name"));
                    student.setEmail(rs.getString("email"));
                    student.setDepartment(rs.getString("department"));
                    student.setBatch(rs.getString("batch"));
                    student.setCgpa(rs.getDouble("cgpa"));
                    students.add(student);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public static java.util.List<Student> getStudentsByDepartmentAndBatch(String department, String batch) {
        java.util.List<Student> students = new java.util.ArrayList<>();
        String sql = "SELECT * FROM students WHERE department = ? AND batch = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, department);
            pstmt.setString(2, batch);

            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getString("id"));
                    student.setName(rs.getString("name"));
                    student.setEmail(rs.getString("email"));
                    student.setDepartment(rs.getString("department"));
                    student.setBatch(rs.getString("batch"));
                    student.setCgpa(rs.getDouble("cgpa"));
                    students.add(student);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public static java.util.List<String> getDistinctBatches() {
        java.util.List<String> batches = new java.util.ArrayList<>();
        String sql = "SELECT DISTINCT batch FROM students ORDER BY batch";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                java.sql.ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                batches.add(rs.getString("batch"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return batches;
    }

    public static boolean updateCGPA(String studentId, double cgpa) {
        String sql = "UPDATE students SET cgpa = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, cgpa);
            pstmt.setString(2, studentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updatePassword(String studentId, String newPassword) {
        String sql = "UPDATE students SET password = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, studentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
