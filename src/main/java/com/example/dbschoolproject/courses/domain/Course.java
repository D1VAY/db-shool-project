package com.example.dbschoolproject.courses.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Course {
    private int id;
    private String name;
    private String description;
    private Set<Student> students;

    public Course(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.students = new HashSet<Student>();
    }

    public List<Student> getStudents() {
        List<Student> studentList = new ArrayList<>(students.size());
        studentList.addAll(students);
        return studentList;
    }

    public void assignStudent(Student student) {
        students.add(student);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Course [id=" + id + ", name=" + name + ", description=" + description + ", students=" + students + "]\n";
    }
}
