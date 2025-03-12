package passoff.server;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

import static dataaccess.DatabaseManager.*;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseSetupTests {
    static DBAuthDAO dbAuthDao = new DBAuthDAO();
    static DBUserDAO dbUserDao = new DBUserDAO();

    @BeforeEach
    public void setup() {

        UserData user = new UserData("adminUsername", "adminPassword", "admin@gmail.com");
        try {
            createDatabase();
            dbUserDao.createUser(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void clear() {
        dbAuthDao.clearAllAuth();
        dbUserDao.clearAllUsers();
    }

    @Test
    @Order(1)
    @DisplayName("Create Database works")
    public void create() {
        assertDoesNotThrow(DatabaseManager::createDatabase, "CreateDatabase should not throw an exception.");
    }

    @Test
    @Order(2)
    @DisplayName("DBAuthDAO CreateAuth")
    public void createAuth() {
        AuthData testAuth = new AuthData("potato", "adminUsername");
        dbAuthDao.createAuth(testAuth);
        //add an assert equals statement
    }

    @Test
    @Order(3)
    @DisplayName("DBUserDAO CreateUser")
    public void addUser() {
        UserData user = new UserData("anotherUser", "anotherPass", "another@gmail.com");
        try {
            dbUserDao.createUser(user);
        } catch (AlreadyTakenException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Clear authdata")
    public void clearAuthData() {
        dbAuthDao.clearAllAuth();

    }

    @Test
    @DisplayName("Clear userdata")
    public void clearUserData() {
        dbUserDao.clearAllUsers();

    }

//    @Test
//    @Order(2)
//    @DisplayName("Test Access database")
//    public void testAccess() {
//        try (var connection = getConnection()) {
//            // execute SQL statements.
//            connection
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

}
