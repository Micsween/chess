package passoff.server;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import static dataaccess.DatabaseManager.*;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseSetupTests {
    static DBAuthDAO dbAuthDao = new DBAuthDAO();
    static DBUserDAO dbUserDao = new DBUserDAO();
    DBGameDAO dbGameDao = new DBGameDAO();

    UserData user = new UserData("adminUsername", "adminPassword", "admin@gmail.com");
    AuthData auth = new AuthData("imcool22", "adminUsername");
    GameData gameData = new GameData(1,
            "imausername", "imanotherusername",
            "coolGame", new ChessGame());

    @BeforeEach
    public void setup() {
        try {
            createDatabase();
            dbUserDao.createUser(user);
            dbAuthDao.createAuth(auth);
            dbGameDao.createGame(gameData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void clear() {
        dbAuthDao.clearAllAuth();
        dbUserDao.clearAllUsers();
        dbGameDao.clearAllGames();
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
    @Order(5)
    @DisplayName("getUser")
    public void getUser() {
        try {
            UserData userData = dbUserDao.getUser(user.username());
            assertEquals(user.username(), userData.username());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(6)
    @DisplayName("get nonexistent User")
    public void getBadUser() {
        try {
            assertThrows(DataAccessException.class, () -> dbUserDao.getUser("I don't exist"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(7)
    @DisplayName("verify user")
    public void verifyUser() {
        try {
            UserData userData = dbUserDao.verifyUser(user.username(), user.password());
            assertEquals(user.username(), userData.username());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(8)
    @DisplayName("Incorrect Password")
    public void verifyBadUser() {
        try {
            assertThrows(DataAccessException.class, () -> dbUserDao.verifyUser(user.username(), "this is a bad password"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(9)
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
    @Order(10)
    @DisplayName("get bad auth data")
    public void getBadAuth() {
        assertThrows(UnauthorizedException.class, () -> dbAuthDao.getAuth("this auth does not exist"));
    }

    @Test
    @Order(11)
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
    @Order(12)
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
    @Order(13)
    @DisplayName("create good game")
    public void createGoodGame() {
        //this is a second game created after setup, should have a gameID of 2.
        GameData gameData = new GameData(null,
                "white", "black",
                "another", new ChessGame());
        try {
            GameData game = dbGameDao.createGame(gameData);
            assertEquals(2, game.gameID());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(14)
    @DisplayName("list all games")
    public void listAllGames() {
        GameData secondGame = new GameData(null,
                "white", "black",
                "another", new ChessGame());
        GameData thirdGame = new GameData(null,
                "white", "black",
                "third", new ChessGame());
        GameData fourthGame = new GameData(null,
                "white", "black",
                "third", new ChessGame());
        try {
            dbGameDao.createGame(secondGame);
            dbGameDao.createGame(thirdGame);
            dbGameDao.createGame(fourthGame);
            System.out.println(dbGameDao.listGames());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(12)
    @DisplayName("get good game")
    public void getGoodGame() {
        try {
            GameData newGameData = dbGameDao.getGame(gameData.gameID());
            assertEquals(gameData.gameName(), newGameData.gameName());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(13)
    @DisplayName("get bad game")
    public void getBadGame() {
        assertThrows(DataAccessException.class, () -> dbGameDao.getGame(5435));
    }

    @Test
    @Order(14)
    @DisplayName("update game")
    public void updateGame() {
        GameData gameToUpdate = new GameData(gameData.gameID(), "This is an updated username",
                gameData.blackUsername(), gameData.gameName(), gameData.game());
        try {
            dbGameDao.updateGame(gameToUpdate);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(15)
    @DisplayName("update game")
    public void updateBadGame() {
        GameData gameToUpdate = new GameData(3849204, "This is an updated username",
                gameData.blackUsername(), gameData.gameName(), gameData.game());
        assertThrows(DataAccessException.class, () -> dbGameDao.updateGame(gameToUpdate));


    }

}
