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
    AuthData auth = new AuthData("imcool22", "adminUsername");

    @BeforeEach
    public void setup() {
        try {
            createDatabase();
            dbUserDao.createUser(user);
            dbAuthDao.createAuth(auth);
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
        dbAuthDao.createAuth(auth);
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
    @Order(7)
    @DisplayName("get correct auth data")
    public void getGoodAuth() {
        try {
            AuthData authData = dbAuthDao.getAuth(auth.authToken());
            assertEquals(auth, authData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(8)
    @DisplayName("get bad auth data")
    public void getBadAuth() {
        assertThrows(UnauthorizedException.class, () -> dbAuthDao.getAuth("this auth does not exist"));
    }

    @Test
    @Order(9)
    @DisplayName("delete correct auth data")
    public void deleteAuth() {
        try {
            assertDoesNotThrow((() -> dbAuthDao.getAuth(auth.authToken())));
            dbAuthDao.deleteAuth(auth.authToken());
            assertThrows(UnauthorizedException.class, () -> dbAuthDao.getAuth(auth.authToken()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(10)
    @DisplayName("delete bad auth Token")
    public void deleteBadAuth() {
        try {
            assertThrows(UnauthorizedException.class, () -> dbAuthDao.deleteAuth("this auth does not exist"));
            assertEquals(auth,
                    dbAuthDao.getAuth(auth.authToken()));
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
