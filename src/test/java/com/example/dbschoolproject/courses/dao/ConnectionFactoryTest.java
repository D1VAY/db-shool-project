package com.example.dbschoolproject.courses.dao;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionFactoryTest {
    @Test
    void getConnection_shouldEstablishConnection() {
        assertDoesNotThrow(() -> {
            Connection connection = ConnectionFactory.getConnection();
            connection.close();
        });
    }


    @Test
    void getConnection_shouldThrowNoDBPropertiesExceptionWhenGetsWrongPropertiesFilePath() {
        assertDoesNotThrow(() -> {
                    Connection connection = ConnectionFactory.getConnection("src/main/resources/db.properties");
                    connection.close();
                }
        );
    }
}
