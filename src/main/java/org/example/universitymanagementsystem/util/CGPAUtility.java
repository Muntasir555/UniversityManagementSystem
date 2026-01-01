package org.example.universitymanagementsystem.util;

import org.example.universitymanagementsystem.models.Result;
import java.util.List;
import java.util.Map;

public class CGPAUtility {

    public static String calculateGrade(double marks) {
        if (marks >= 80)
            return "A+";
        if (marks >= 75)
            return "A";
        if (marks >= 70)
            return "A-";
        if (marks >= 65)
            return "B+";
        if (marks >= 60)
            return "B";
        if (marks >= 55)
            return "B-";
        if (marks >= 50)
            return "C+";
        if (marks >= 45)
            return "C";
        if (marks >= 40)
            return "D";
        return "F";
    }

    public static double calculateGradePoint(String grade) {
        switch (grade) {
            case "A+":
                return 4.00;
            case "A":
                return 3.75;
            case "A-":
                return 3.50;
            case "B+":
                return 3.25;
            case "B":
                return 3.00;
            case "B-":
                return 2.75;
            case "C+":
                return 2.50;
            case "C":
                return 2.25;
            case "D":
                return 2.00;
            default:
                return 0.00;
        }
    }

    public static double calculateCGPA(List<Result> results, Map<Integer, Integer> subjectCredits) {
        if (results == null || results.isEmpty()) {
            return 0.0;
        }

        double totalGradePoints = 0.0;
        double totalCredits = 0.0;

        for (Result result : results) {
            // Only consider if not F? Usually F counts as 0 points but adds to credits
            // attempted.
            // Assumption: F counts in CGPA.

            Integer credit = subjectCredits.get(result.getSubjectId());
            if (credit != null) {
                double gp = calculateGradePoint(result.getGrade());
                totalGradePoints += (gp * credit);
                totalCredits += credit;
            }
        }

        if (totalCredits == 0)
            return 0.0;

        double cgpa = totalGradePoints / totalCredits;
        // Round to 2 decimal places
        return Math.round(cgpa * 100.0) / 100.0;
    }
}
