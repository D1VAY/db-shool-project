package com.example.dbschoolproject.courses.utils;

import com.example.dbschoolproject.courses.domain.Course;
import com.example.dbschoolproject.courses.domain.Group;
import com.example.dbschoolproject.courses.domain.Student;
import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;
import java.util.stream.IntStream;

public class DataGenerator {

    private Random randomNumberGenerator;
    private Faker randomDataGenerator;

    public DataGenerator() {
        randomNumberGenerator = new Random();
    }

    public List<Group> generateGroup(int size) {
        List<Group> groups = new ArrayList<>(size);
        for (int index = 0; index < size; index++) {
            groups.add(createGroup(index + 1));
        }
        return groups;
    }

    public List<Student> generateStudents(int number) {
        List<Student> students = new ArrayList<>(number);

        for (int index = 0; index < number; index++) {
            students.add(createStudent(index + 1));
        }
        return students;
    }

    public List<Course> generateCourse(int number) {
        List<Course> courses = new ArrayList<>(number);
        for (int index = 0; index < number; index++) {
            courses.add(createCourses(index + 1));
        }
        return courses;
    }

    public void assignStudentsToGroups(List<Group> groups, List<Student> students, int minStudentsNumber,
                                       int maxStudentNumber) {
        LinkedList<Student> studentsAssign = new LinkedList<>(students);
        Collections.shuffle(studentsAssign);

        groups.
                forEach(group -> {
                    IntStream.
                            rangeClosed(1, getRandomNumberRange(minStudentsNumber, maxStudentNumber)).
                            forEach(i -> {
                                if (!studentsAssign.
                                        isEmpty()) {
                                    studentsAssign.
                                            removeFirst().
                                            setGroup_id(group.getId());
                                }
                            });
                });
    }

    public void assignCoursesToStudents(List<Course> courses, List<Student> students, int minCoursesNumber,
                                        int maxCoursesNumber) {
        students.forEach(student -> {
            IntStream.rangeClosed(1, getRandomNumberRange(minCoursesNumber, maxCoursesNumber)).
                    forEach(i -> {
                assignStudentToRandomCourse(courses, student);
            });
        });

    }

    private Group createGroup(int id) {
        return new Group(id, generateRandomGroupName());
    }

    private Student createStudent(int id) {
        randomDataGenerator = new Faker();
        String firstName = randomDataGenerator.name().firstName();
        String lastName = randomDataGenerator.name().lastName();
        return new Student(id, firstName, lastName);
    }

    private Course createCourses(int id) {
        randomDataGenerator = new Faker();
        String name = randomDataGenerator.educator().course();
        return new Course(id, name, "Course - " + name);
    }

    private static String generateRandomGroupName() {
        return RandomStringUtils.randomAlphabetic(2) + "-" + RandomStringUtils.randomNumeric(2);
    }

    private int getRandomNumberRange(int min, int max) {
        return randomNumberGenerator
                .ints(min, (max + 1))
                .limit(1)
                .findFirst()
                .getAsInt();
    }

    private void assignStudentToRandomCourse(List<Course> courses, Student student) {
        Course course = courses.get(getRandomNumberRange(0, courses.size() - 1));
        course.assignStudent(student);
    }
}
