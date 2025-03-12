package dataaccess;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static final String DATABASE_NAME;
    private static final String USER;
    private static final String PASSWORD;
    private static final String CONNECTION_URL;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) {
                    throw new Exception("Unable to load db.properties");
                }
                Properties props = new Properties();
                props.load(propStream);
                DATABASE_NAME = props.getProperty("db.name");
                USER = props.getProperty("db.user");
                PASSWORD = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                CONNECTION_URL = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    private static void createTables() {
        String[] tables = {
                """
CREATE TABLE IF NOT EXISTS gamedata (
    gameID INT AUTO_INCREMENT PRIMARY KEY,
    whiteUsername TEXT,
    blackUsername TEXT,
    gameName TEXT NOT NULL,
    game JSON NOT NULL
);""",
                """
CREATE TABLE IF NOT EXISTS userdata (
   username VARCHAR(256) PRIMARY KEY,
   password TEXT NOT NULL,
   email TEXT NOT NULL)""",
                """
CREATE TABLE IF NOT EXISTS authdata(
    authToken TEXT NOT NULL,
    username VARCHAR(256) NOT NULL,
    FOREIGN KEY (username) REFERENCES userdata(username)
)"""};
        try (var connection = getConnection()) {
            for (var table : tables) {
                try (var tableStatement = connection.prepareStatement(table)) {
                    tableStatement.executeUpdate();
                }
            }

        } catch (Exception ex) {
            throw new RuntimeException("unable to create tables. " + ex.getMessage());
        }
    }


    public static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;

//            var createAuthTable = "CREATE TABLE IF NOT EXISTS authdata (\n" +
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
                createTables();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    public static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            conn.setCatalog(DATABASE_NAME);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
