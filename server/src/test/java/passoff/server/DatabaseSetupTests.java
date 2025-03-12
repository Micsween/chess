package passoff.server;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseSetupTests {
    DBAuthDAO dbAuthDAO = new DBAuthDAO();
    DBUserDAO dbUserDAO = new DBUserDAO();

    @BeforeEach
    public void setup() {
        UserData user = new UserData("adminUsername", "adminPassword", "admin@gmail.com");
        try {
            dbUserDAO.createUser(user);
        } catch (AlreadyTakenException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void clearUser() {

        //server.stop();
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
        AuthData testAuth = new AuthData("potato", "testusername");
        dbAuthDAO.createAuth(testAuth);
        //add an assert equals statement
    }

    @Test
    @Order(3)
    @DisplayName("DBUserDAO CreateUser")
    public void addUser() {
        UserData user = new UserData("anotherUser", "anotherPass", "another@gmail.com");
        try {
            dbUserDAO.createUser(user);
        } catch (AlreadyTakenException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Delete Database")
    public void delete() {

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
