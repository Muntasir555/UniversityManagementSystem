package org.example.universitymanagementsystem.database;

import org.example.universitymanagementsystem.models.Subject;
import org.example.universitymanagementsystem.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubjectDatabase {

    public static boolean addSubject(Subject subject) {
        String sql = "INSERT INTO subjects(name, code, credit, department) VALUES(?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, subject.getName());
            pstmt.setString(2, subject.getCode());
            pstmt.setInt(3, subject.getCredit());
            pstmt.setString(4, subject.getDepartment());

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error adding subject: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static List<Subject> getSubjectsByDepartment(String department) {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT * FROM subjects WHERE department = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, department);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Subject subject = new Subject();
                    subject.setId(rs.getInt("id"));
                    subject.setName(rs.getString("name"));
                    subject.setCode(rs.getString("code"));
                    subject.setCredit(rs.getInt("credit"));
                    subject.setDepartment(rs.getString("department"));
                    subjects.add(subject);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    public static List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        String sql = "SELECT * FROM subjects";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Subject subject = new Subject();
                subject.setId(rs.getInt("id"));
                subject.setName(rs.getString("name"));
                subject.setCode(rs.getString("code"));
                subject.setCredit(rs.getInt("credit"));
                subject.setDepartment(rs.getString("department"));
                subjects.add(subject);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subjects;
    }

    public static Subject getSubjectByCode(String code) {
        String sql = "SELECT * FROM subjects WHERE code = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, code);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Subject subject = new Subject();
                    subject.setId(rs.getInt("id"));
                    subject.setName(rs.getString("name"));
                    subject.setCode(rs.getString("code"));
                    subject.setCredit(rs.getInt("credit"));
                    subject.setDepartment(rs.getString("department"));
                    return subject;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Subject getSubjectById(int id) {
        String sql = "SELECT * FROM subjects WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Subject subject = new Subject();
                    subject.setId(rs.getInt("id"));
                    subject.setName(rs.getString("name"));
                    subject.setCode(rs.getString("code"));
                    subject.setCredit(rs.getInt("credit"));
                    subject.setDepartment(rs.getString("department"));
                    return subject;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
