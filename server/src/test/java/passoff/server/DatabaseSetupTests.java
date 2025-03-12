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
    UserData user = new UserData("adminUsername", "adminPassword", "admin@gmail.com");

    @BeforeEach
    public void setup() {
        try {
            createDatabase();
            dbUserDao.createUser(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void clear() {
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
        UserData otherUser = new UserData("anotherUser", "anotherPass", "another@gmail.com");
        try {
            dbUserDao.createUser(otherUser);
        } catch (AlreadyTakenException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(4)
    @DisplayName("DBUserDAO add already existing user")
    public void addAlreadyExistingUser() {
        assertThrows(AlreadyTakenException.class, () -> dbUserDao.createUser(user));
    }

    @Test
    @Order(4)
    @DisplayName("getUser")
    public void getUser() {
        try {
            UserData userData = dbUserDao.getUser(user.username());
            assertEquals(user, userData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(4)
    @DisplayName("get nonexistent User")
    public void getBadUser() {
        try {
            assertThrows(DataAccessException.class, () -> dbUserDao.getUser("I don't exist"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(5)
    @DisplayName("verify user")
    public void verifyUser() {
        try {
            UserData userData = dbUserDao.verifyUser(user.username(), user.password());
            assertEquals(user, userData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(6)
    @DisplayName("Incorrect Password")
    public void verifyBadUser() {
        try {
            assertNull(dbUserDao.verifyUser(user.username(), "this is a bad password"));
        } catch (Exception e) {
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
