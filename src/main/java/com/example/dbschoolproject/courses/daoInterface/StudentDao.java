package com.example.dbschoolproject.courses.daoInterface;

import com.example.dbschoolproject.courses.domain.Student;

import java.util.List;

public interface StudentDao {
    List<Student> findAll();
    void saveAll(List<Student> students);
    void save(Student student);
    void delete(int studentId);
    void assignToCourse(int studentId, int courseId);
    void deleteFromCourse(int studentId, int courseId);


}
