package com.example.dbschoolproject.courses.daoInterface;

import com.example.dbschoolproject.courses.domain.Course;

import java.util.List;

public interface CourseDao {
    void saveAll(List<Course> courses);
    List<Course> findAll();
    List<Course> findByStudentId(int studentId);

}
