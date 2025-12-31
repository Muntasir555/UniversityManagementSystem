package org.example.universitymanagementsystem.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

    private static final String CONNECTION_STRING = "jdbc:sqlite:university.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONNECTION_STRING);
    }

    public static void initializeDatabase() {
        String createStudentTable = "CREATE TABLE IF NOT EXISTS students ("
                + "id TEXT PRIMARY KEY, "
                + "name TEXT NOT NULL, "
                + "email TEXT NOT NULL UNIQUE, "
                + "department TEXT, "
                + "batch TEXT, "
                + "password TEXT NOT NULL"
                + ");";

        String createFacultyTable = "CREATE TABLE IF NOT EXISTS faculty ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "email TEXT NOT NULL UNIQUE, "
                + "department TEXT, "
                + "password TEXT NOT NULL"
                + ");";

        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {

            stmt.execute(createStudentTable);
            stmt.execute(createFacultyTable);

            String createAdminTable = "CREATE TABLE IF NOT EXISTS admins ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "username TEXT NOT NULL UNIQUE, "
                    + "password TEXT NOT NULL"
                    + ");";
            stmt.execute(createAdminTable);

            String createSubjectTable = "CREATE TABLE IF NOT EXISTS subjects ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "name TEXT NOT NULL, "
                    + "code TEXT NOT NULL UNIQUE, "
                    + "credit INTEGER NOT NULL, "
                    + "department TEXT NOT NULL"
                    + ");";
            stmt.execute(createSubjectTable);

            String createResultTable = "CREATE TABLE IF NOT EXISTS results ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "student_id TEXT NOT NULL, "
                    + "subject_id INTEGER NOT NULL, "
                    + "marks REAL NOT NULL, "
                    + "grade TEXT NOT NULL, "
                    + "FOREIGN KEY(student_id) REFERENCES students(id), "
                    + "FOREIGN KEY(subject_id) REFERENCES subjects(id)"
                    + ");";
            stmt.execute(createResultTable);

            String createAttendanceTable = "CREATE TABLE IF NOT EXISTS attendance ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "student_id TEXT NOT NULL, "
                    + "date TEXT NOT NULL, "
                    + "status TEXT NOT NULL, "
                    + "FOREIGN KEY(student_id) REFERENCES students(id)"
                    + ");";
            stmt.execute(createAttendanceTable);

            String createCourseAssignmentTable = "CREATE TABLE IF NOT EXISTS course_assignments ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "student_id TEXT NOT NULL, "
                    + "subject_id INTEGER NOT NULL, "
                    + "faculty_id INTEGER NOT NULL, "
                    + "semester TEXT NOT NULL, "
                    + "UNIQUE(student_id, subject_id, semester), "
                    + "FOREIGN KEY(student_id) REFERENCES students(id), "
                    + "FOREIGN KEY(subject_id) REFERENCES subjects(id),"
                    + "FOREIGN KEY(faculty_id) REFERENCES faculty(id)"
                    + ");";
            stmt.execute(createCourseAssignmentTable);

            // Schema Upgrade: Add cgpa to students if not exists
            try {
                stmt.execute("ALTER TABLE students ADD COLUMN cgpa REAL DEFAULT 0.0");
            } catch (SQLException e) {
                // Column likely exists
            }

            // Schema Upgrade: Add semester to results if not exists
            try {
                stmt.execute("ALTER TABLE results ADD COLUMN semester TEXT");
            } catch (SQLException e) {
                // Column likely exists
            }

            try (java.sql.ResultSet rs = stmt.executeQuery("SELECT count(*) FROM admins")) {
                if (rs.next() && rs.getInt(1) == 0) {
                    stmt.execute("INSERT INTO admins(username, password) VALUES('admin', 'admin123')");
                    System.out.println("Default Admin user created: admin / admin123");
                }
            }

            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
