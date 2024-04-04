package com.example.dbschoolproject.courses.utils;

import com.example.dbschoolproject.courses.dao.ConnectionFactory;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryExecutor {
    private static final String DROP_DB_QUERY =
            "src/main/resources/drop-db-courses.sql";
    private static final String CREATE_DB_QUERY =
            "src/main/resources/create-db-courses.sql";
    private static final String CREATE_TABLE_COURSE_QUERY =
            "src/main/resources/create-table-courses.sql";
    private static final String CREATE_TABLE_GROUP_QUERY =
            "src/main/resources/create-table-group.sql";
    private static final String CREATE_TABLE_STUDENT_QUERY =
            "src/main/resources/create-table-student.sql";
    private static final String CREATE_TABLE_COURSES_STUDENTS_QUERY =
            "src/main/resources/create-table-course_student.sql";
    private static String PROPERTIES_FILE = null;

    public QueryExecutor(String PROPERTIES_FILE) {
        this.PROPERTIES_FILE = PROPERTIES_FILE;
    }

    public QueryExecutor() {
    }

    public static void createDb(String nameDB) {
        ConnectionFactory connectionFactory = new ConnectionFactory(
                "src/main/resources/db.create.properties");
        try (Connection connection = ConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE DATABASE " + nameDB);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void dropDataBase(String nameDB) {
        ConnectionFactory connectionFactory = new ConnectionFactory(
                "src/main/resources/db.create.properties");
        try (Connection connection = ConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP DATABASE IF EXISTS " + nameDB);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createTables() {
        try {
            computeQuery(CREATE_TABLE_COURSE_QUERY);
            computeQuery(CREATE_TABLE_GROUP_QUERY);
            computeQuery(CREATE_TABLE_STUDENT_QUERY);
            computeQuery(CREATE_TABLE_COURSES_STUDENTS_QUERY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void computeQuery(String filePath) {
        ConnectionFactory connectionFactory = new ConnectionFactory(PROPERTIES_FILE);
        try (Connection connection = ConnectionFactory.getConnection();
             FileReader fileReader = new FileReader(filePath)) {
            ScriptRunner scriptRunner = new ScriptRunner(connection);
            scriptRunner.runScript(fileReader);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
