package org.example.universitymanagementsystem.models;

public class CourseAssignment {
    private int id;
    private String studentId;
    private int subjectId;
    private int facultyId;
    private String semester;

    public CourseAssignment() {
    }

    public CourseAssignment(int id, String studentId, int subjectId, int facultyId, String semester) {
        this.id = id;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.facultyId = facultyId;
        this.semester = semester;
    }

    public CourseAssignment(String studentId, int subjectId, int facultyId, String semester) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.facultyId = facultyId;
        this.semester = semester;
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

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
