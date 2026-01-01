package org.example.universitymanagementsystem.models;

public class Result {
    private int id;
    private String studentId;
    private int subjectId;
    private double marks;
    private String grade;
    private String semester;

    public Result() {
    }

    public Result(int id, String studentId, int subjectId, double marks, String grade) {
        this.id = id;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.marks = marks;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
