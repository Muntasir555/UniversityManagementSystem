package org.example.universitymanagementsystem.database;

import org.example.universitymanagementsystem.models.Notice;
import org.example.universitymanagementsystem.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoticeDatabase {

    public static void addNotice(String title, String content, String date) {
        String query = "INSERT INTO notices (title, content, date) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setString(3, date);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Notice> getAllNotices() {
        List<Notice> notices = new ArrayList<>();
        String query = "SELECT * FROM notices ORDER BY id DESC";

        try (Connection conn = DBUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                notices.add(new Notice(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("date")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notices;
    }

    public static void deleteNotice(int id) {
        String query = "DELETE FROM notices WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
