package com.example.dbschoolproject.courses.dao;

import com.example.dbschoolproject.courses.daoInterface.StudentDao;
import com.example.dbschoolproject.courses.domain.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresSqlStudentDao implements StudentDao {
    private static final String INSERTION_GENERATE_ID_QUERY_TEMPLATE = "INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?)";
    private static final int INSERTION_GENERATE_ID_GROUP_ID_PARAMETER = 1;
    private static final int INSERTION_GENERATE_ID_FIRST_NAME_PARAMETER = 2;
    private static final int INSERTION_GENERATE_ID_LAST_NAME_PARAMETER = 3;
    private static final String INSERTION_QUERY_TEMPLATE =
            "INSERT INTO students (student_id, group_id, first_name, last_name) VALUES (?, ?, ?, ?)";
    private static final int INSERTION_STUDENT_ID_PARAMETER = 1;
    private static final int INSERTION_GROUP_ID_PARAMETER = 2;
    private static final int INSERTION_FIRST_NAME_PARAMETER = 3;
    private static final int INSERTION_LAST_NAME_PARAMETER = 4;
    private static final String DELETION_QUERY_TEMPLATE = "DELETE FROM students WHERE student_id = ?";
    private static final int DELETION_PARAMETER_STUDENT_ID = 1;
    private static final String ASSIGNMENT_QUERY_TEMPLATE =
            "INSERT INTO courses_students (student_id, course_id) VALUES (?, ?)";
    private static final int ASSIGNMENT_STUDENT_ID_PARAMETER = 1;
    private static final int ASSIGNMENT_COURSE_ID_PARAMETER = 2;
    private static final String EXPULSION_QUERY_TEMPLATE =
            "DELETE FROM courses_students WHERE student_id = ? AND course_id = ?";
    private static final int EXPULSION_STUDENT_ID_PARAMETER = 1;
    private static final int EXPULSION_COURSE_ID_PARAMETER = 2;
    private static String PROPERTIES_FILE = null;

    public PostgresSqlStudentDao(String PROPERTIES_FILE) {
        this.PROPERTIES_FILE = PROPERTIES_FILE;
    }

    public PostgresSqlStudentDao() {
    }

    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        ConnectionFactory connectionFactory = new ConnectionFactory(PROPERTIES_FILE);
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM students");
             ResultSet resultSet = statement.executeQuery();) {
            students = processResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return students;
    }

    private List<Student> processResultSet(ResultSet resultSet) throws SQLException {
        List<Student> students = new ArrayList<>();
        while (resultSet.next()) {
            Student student = new Student(resultSet.getInt("student_id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"));
            student.setGroup_id(resultSet.getInt("group_id"));
            students.add(student);
        }

        return students;
    }
    @Override
    public void saveAll(List<Student> students) {
        students.forEach(s -> save(s));
    }
    @Override
    public void save(Student student) {
        ConnectionFactory connectionFactory = new ConnectionFactory(PROPERTIES_FILE);
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = preparedStatement(connection, student)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private PreparedStatement preparedStatement(Connection connection, Student student) throws SQLException {
        PreparedStatement statement;
        if (student.getId() == 0) {
            statement = connection.prepareStatement(INSERTION_GENERATE_ID_QUERY_TEMPLATE);
            statement.setInt(INSERTION_GENERATE_ID_GROUP_ID_PARAMETER, student.getGroup_id());
            statement.setString(INSERTION_GENERATE_ID_FIRST_NAME_PARAMETER, (student.getFirstName()));
            statement.setString(INSERTION_GENERATE_ID_LAST_NAME_PARAMETER, (student.getLastName()));
        } else {
            statement = connection.prepareStatement(INSERTION_QUERY_TEMPLATE);
            statement.setInt(INSERTION_STUDENT_ID_PARAMETER, student.getId());
            statement.setInt(INSERTION_GROUP_ID_PARAMETER, student.getGroup_id());
            statement.setString(INSERTION_FIRST_NAME_PARAMETER, (student.getFirstName()));
            statement.setString(INSERTION_LAST_NAME_PARAMETER, (student.getLastName()));
        }
        return statement;
    }

    @Override
    public void delete(int studentId) {
        ConnectionFactory connectionFactory = new ConnectionFactory(PROPERTIES_FILE);
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETION_QUERY_TEMPLATE)) {
            statement.setInt(DELETION_PARAMETER_STUDENT_ID, studentId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void assignToCourse(int studentId, int courseId) {
        ConnectionFactory connectionFactory = new ConnectionFactory(PROPERTIES_FILE);
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(ASSIGNMENT_QUERY_TEMPLATE)) {
            statement.setInt(ASSIGNMENT_STUDENT_ID_PARAMETER, studentId);
            statement.setInt(ASSIGNMENT_COURSE_ID_PARAMETER, courseId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteFromCourse(int studentId, int courseId) {
        ConnectionFactory connectionFactory = new ConnectionFactory(PROPERTIES_FILE);
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(EXPULSION_QUERY_TEMPLATE)) {
            statement.setInt(EXPULSION_STUDENT_ID_PARAMETER, studentId);
            statement.setInt(EXPULSION_COURSE_ID_PARAMETER, courseId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
