package com.example.dbschoolproject.courses.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    private static String PROPERTIES_FILE_IMPORT = null;
    private static final String PROPERTIES_FILE_BASIC = "src/main/resources/db.properties";
    private static final String PROPERTIES_URL = "db.url";
    private static final String PROPERTIES_USER = "db.user";
    private static final String PROPERTIES_PASSWORD = "db.password";
    public ConnectionFactory(String PROPERTIES_FILE_IMPORT) {
        this.PROPERTIES_FILE_IMPORT = PROPERTIES_FILE_IMPORT;
    }
    public static Connection getConnection() throws SQLException {
        if (PROPERTIES_FILE_IMPORT != null) {
            return getConnection(PROPERTIES_FILE_IMPORT);
        } else {
            return getConnection(PROPERTIES_FILE_BASIC);
        }
    }

    public static Connection getConnection(String propertiesFile) throws SQLException {
        PROPERTIES_FILE_IMPORT = null;
        Properties properties = new Properties();
        try {
            InputStream inputStream = new FileInputStream(propertiesFile);
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read properties file", e);
        }

        String url = properties.getProperty(PROPERTIES_URL);
        String user = properties.getProperty(PROPERTIES_USER);
        String password = properties.getProperty(PROPERTIES_PASSWORD);

        return DriverManager.getConnection(url, user, password);
    }

}
