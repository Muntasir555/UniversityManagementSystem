package org.example.universitymanagementsystem.models;

public class Faculty {
    private int id;
    private String name;
    private String email;
    private String department;
    private String password;

    public Faculty() {
    }

    public Faculty(String name, String email, String department, String password) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.password = password;
    }

    public Faculty(int id, String name, String email, String department, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
