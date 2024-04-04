package com.example.dbschoolproject.courses.dao;

import com.example.dbschoolproject.courses.daoInterface.CourseDao;
import com.example.dbschoolproject.courses.domain.Course;
import com.example.dbschoolproject.courses.domain.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class PostgresSQLCourseDao implements CourseDao {
    private static final String INSERTION_COURSE_ID = "course_id";
    private static final String INSERTION_COURSE_NAME = "course_name";
    private static final String INSERTION_COURSE_DESCRIPTION = "course_description";
    private static String PROPERTIES_FILE = null;

    public PostgresSQLCourseDao(String PROPERTIES_FILE) {
        this.PROPERTIES_FILE = PROPERTIES_FILE;
    }

    public PostgresSQLCourseDao() {
    }

    private static final String SELECTION_BY_STUDENT_ID_QUERY_TEMPLATE =
            "SELECT * " +
                    "FROM courses_students " +
                    "INNER JOIN courses " +
                    "ON courses_students.course_id = courses.course_id " +
                    "WHERE courses_students.student_id = ?";

    @Override
    public void saveAll(List<Course> courses) {
        ConnectionFactory connectionFactory = new ConnectionFactory(PROPERTIES_FILE);
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statementCourses = connection.prepareStatement(
                     "INSERT INTO courses (course_id, course_name, course_description) VALUES (?, ?, ?)");
             PreparedStatement statementStudents = connection.prepareStatement(
                     "INSERT INTO courses_students (course_id, student_id) VALUES (?, ?)")) {

            prepareCourseStatements(courses, statementCourses, statementStudents);
            statementCourses.executeBatch();
            statementStudents.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Course> findByStudentId(int studentId) {
        List<Course> courses = new ArrayList<Course>();
        ConnectionFactory connectionFactory = new ConnectionFactory(PROPERTIES_FILE);
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECTION_BY_STUDENT_ID_QUERY_TEMPLATE)) {
            statement.setInt(1, studentId);
            ResultSet resultSet = statement.executeQuery();
            courses = processResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return courses;
    }

    @Override
    public List<Course> findAll() {
        List<Course> courses = new ArrayList<Course>();
        ConnectionFactory connectionFactory = new ConnectionFactory(PROPERTIES_FILE);
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM courses");
             ResultSet resultSet = statement.executeQuery()) {
            courses = processResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return courses;
    }

    private void prepareCourseStatements(List<Course> courses, PreparedStatement statementCourse,
                                         PreparedStatement statementStudent) throws SQLException {
        for (Course course : courses) {
            statementCourse.setInt(1, course.getId());
            statementCourse.setString(2, course.getName());
            statementCourse.setString(3, course.getDescription());
            statementCourse.addBatch();

            for (Student student : course.getStudents()) {
                statementStudent.setInt(1, course.getId());
                statementStudent.setInt(2, student.getId());
                statementStudent.addBatch();
            }
        }
    }

    private List<Course> processResultSet(ResultSet resultSet) throws SQLException {
        List<Course> courses = new ArrayList<>();
        while (resultSet.next()) {
            Course course = new Course(resultSet.getInt(INSERTION_COURSE_ID),
                    resultSet.getString(INSERTION_COURSE_NAME),
                    resultSet.getString(INSERTION_COURSE_DESCRIPTION));
            courses.add(course);
        }
        return courses;
    }
}
