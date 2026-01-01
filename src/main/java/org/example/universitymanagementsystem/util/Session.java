package org.example.universitymanagementsystem.util;

import org.example.universitymanagementsystem.models.Admin;
import org.example.universitymanagementsystem.models.Faculty;
import org.example.universitymanagementsystem.models.Student;

public class Session {

    public static Student currentStudent;
    public static Faculty currentFaculty;
    public static Admin currentAdmin;

    public static void clear() {
        currentStudent = null;
        currentFaculty = null;
        currentAdmin = null;
    }
}
