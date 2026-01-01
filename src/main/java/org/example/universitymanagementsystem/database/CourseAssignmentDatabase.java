package org.example.universitymanagementsystem.database;

import org.example.universitymanagementsystem.models.CourseAssignment;
import org.example.universitymanagementsystem.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseAssignmentDatabase {

    public static boolean assignCourse(CourseAssignment assignment) {
        String sql = "INSERT INTO course_assignments(student_id, subject_id, faculty_id, semester) VALUES(?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, assignment.getStudentId());
            pstmt.setInt(2, assignment.getSubjectId());
            pstmt.setInt(3, assignment.getFacultyId());
            pstmt.setString(4, assignment.getSemester());

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error assigning course: " + e.getMessage());
            // Consume duplicate error silently if needed, or propagate.
            return false;
        }
    }

    public static List<Integer> getSubjectIdsByFacultyAndSemester(int facultyId, String semester) { // Return Subject
                                                                                                    // IDs or
                                                                                                    // Assignments?
        // For Faculty Dash: We need distinct Subjects assigned to this Faculty for a
        // sem? Or all assignments?
        // FacultyAddResult needs List of Courses.
        // Let's return CourseAssignment objects or distinct Subject IDs.
        return null; // Implemented below more specifically
    }

    public static List<CourseAssignment> getAssignmentsByFaculty(int facultyId) {
        List<CourseAssignment> assignments = new ArrayList<>();
        String sql = "SELECT * FROM course_assignments WHERE faculty_id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, facultyId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    CourseAssignment ca = new CourseAssignment();
                    ca.setId(rs.getInt("id"));
                    ca.setStudentId(rs.getString("student_id"));
                    ca.setSubjectId(rs.getInt("subject_id"));
                    ca.setFacultyId(rs.getInt("faculty_id"));
                    ca.setSemester(rs.getString("semester"));
                    assignments.add(ca);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assignments;
    }

    // Helper to check if a student is assigned to this faculty for this subject
    public static boolean isAssigned(String studentId, int subjectId, int facultyId) {
        String sql = "SELECT count(*) FROM course_assignments WHERE student_id = ? AND subject_id = ? AND faculty_id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setInt(2, subjectId);
            pstmt.setInt(3, facultyId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next())
                    return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get all students assigned to a specific subject taught by a specific faculty
    public static List<String> getStudentIdsByFacultyAndSubject(int facultyId, int subjectId) {
        List<String> studentIds = new ArrayList<>();
        String sql = "SELECT student_id FROM course_assignments WHERE faculty_id = ? AND subject_id = ?";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, facultyId);
            pstmt.setInt(2, subjectId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    studentIds.add(rs.getString("student_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studentIds;
    }
}
