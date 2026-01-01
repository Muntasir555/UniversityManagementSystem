package org.example.universitymanagementsystem.database;

import org.example.universitymanagementsystem.models.Result;
import org.example.universitymanagementsystem.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultDatabase {

    public static boolean addResult(Result result) {
        String sql = "INSERT INTO results(student_id, subject_id, marks, grade, semester) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, result.getStudentId());
            pstmt.setInt(2, result.getSubjectId());
            pstmt.setDouble(3, result.getMarks());
            pstmt.setString(4, result.getGrade());
            pstmt.setString(5, result.getSemester());

            pstmt.executeUpdate();


            recalculateAndSaveCGPA(result.getStudentId());

            return true;

        } catch (SQLException e) {
            System.err.println("Error adding result: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static void recalculateAndSaveCGPA(String studentId) {
        List<Result> allResults = getResultsByStudentId(studentId);
        // We need subject credits.
        // Optimization: Fetch only needed subjects or cache them. simpler: Fetch all
        // subjects and make a map.
        List<org.example.universitymanagementsystem.models.Subject> allSubjects = SubjectDatabase.getAllSubjects();
        java.util.Map<Integer, Integer> subjectCredits = new java.util.HashMap<>();
        for (org.example.universitymanagementsystem.models.Subject s : allSubjects) {
            subjectCredits.put(s.getId(), s.getCredit());
        }

        double newCgpa = org.example.universitymanagementsystem.util.CGPAUtility.calculateCGPA(allResults,
                subjectCredits);
        StudentDatabase.updateCGPA(studentId, newCgpa);
    }

    public static List<Result> getResultsByStudentId(String studentId) {
        List<Result> results = new ArrayList<>();
        String sql = "SELECT * FROM results WHERE student_id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Result result = new Result();
                    result.setId(rs.getInt("id"));
                    result.setStudentId(rs.getString("student_id"));
                    result.setSubjectId(rs.getInt("subject_id"));
                    result.setMarks(rs.getDouble("marks"));
                    result.setGrade(rs.getString("grade"));
                    result.setSemester(rs.getString("semester")); // Fetch semester
                    results.add(result);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
}
