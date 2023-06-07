package com.dmdev.integration;

import com.dmdev.util.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;

import java.sql.SQLException;

public abstract class IntegrationTestBase {

    private static final String CLEAN_SQL = "DROP TABLE IF EXISTS users;";
    private static final String CREATE_SQL = """
            CREATE TABLE IF NOT EXISTS users
            (
                id INT AUTO_INCREMENT PRIMARY KEY ,
                name VARCHAR(64),
                birthday DATE NOT NULL ,
                email VARCHAR(64) NOT NULL UNIQUE ,
                password VARCHAR(64) NOT NULL ,
                role VARCHAR(32) NOT NULL ,
                gender VARCHAR(16)
            );
            """;

    @BeforeEach
    void prepareDatabase() throws SQLException {
        try (var connection = ConnectionManager.get();
             var statement = connection.createStatement()) {
            statement.execute(CLEAN_SQL);
            statement.execute(CREATE_SQL);
        }
    }
}