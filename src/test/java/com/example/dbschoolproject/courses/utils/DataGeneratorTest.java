package com.example.dbschoolproject.courses.utils;

import com.example.dbschoolproject.courses.domain.Course;
import com.example.dbschoolproject.courses.domain.Group;
import com.example.dbschoolproject.courses.domain.Student;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DataGeneratorTest {
    private DataGenerator dataGenerator;

    public DataGeneratorTest() {
        this.dataGenerator = new DataGenerator();
    }

    @Test
    void generateGroup_shouldGenerateTheSameNumberRandomGroupsAsInSize_whenWeSendNumber() {
        List<Group> groups = dataGenerator.generateGroup(10);
        assertEquals(10, groups.size());
    }

    @Test
    void generateStudents_shouldGenerateTheSameNumberRandomCoursesAsInSize_whenWeSendNumber() {
        List<Student> students = dataGenerator.generateStudents(10);
        assertEquals(10, students.size());
    }

    @Test
    void generateCourse_shouldGenerateTheSameNumberOfRandomStudentsAsInSize_whenWeSendNumber() {
        List<Course> courses = dataGenerator.generateCourse(10);
        assertEquals(10, courses.size());
    }

    @Test
    void assignStudentsToGroups_shouldAssignRandomStudentsToRandomGroups_whenPassingParametersInMethod() {
        List<Student> students = new DataGenerator().generateStudents(10);
        List<Group> groups = new DataGenerator().generateGroup(10);
        dataGenerator.assignStudentsToGroups(groups, students, 12, 15);
        assertNotEquals(0, students.stream().filter(student -> {
            return (student.getGroup_id() != 0);
        }));
    }

    @Test
    void assignCoursesToStudents_shouldExpelStudentFromCourse() {
        List<Student> students = new DataGenerator().generateStudents(10);
        List<Course> courses = new DataGenerator().generateCourse(10);
        dataGenerator.assignCoursesToStudents(courses, students, 1, 10);
    }
}