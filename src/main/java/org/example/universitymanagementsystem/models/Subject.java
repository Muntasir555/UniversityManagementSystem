package org.example.universitymanagementsystem.models;

public class Subject {
    private int id;
    private String name;
    private String code;
    private int credit;
    private String department;

    public Subject() {
    }

    public Subject(int id, String name, String code, int credit, String department) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.credit = credit;
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
