package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import model.requests.*;
import model.responses.*;
import org.junit.jupiter.api.*;
import dataaccess.*;
import model.UserData;


import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {
    public static AuthService authService;
    public static GameService gameService;
    public static UserService userService;

    public static UserData user;

    @BeforeEach
    public void setup() {
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
        MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        ServerDaos daos = new ServerDaos(memoryAuthDAO, memoryUserDAO, memoryGameDAO);
        authService = new AuthService(daos);
        gameService = new GameService(daos);
        userService = new UserService(daos);
        user = new UserData("NewUser", "newUser", "newUser@email");


    }

    @Test
    @Order(1)
    @DisplayName("Create user Positive Test")
    public void createUserTest() throws AlreadyTakenException {
        //create a user using userData
        UserData testUser = new UserData("admin", "admin", "admin");
        MemoryUserDAO userDAO = new MemoryUserDAO();
        userDAO.createUser(testUser);
        assertTrue(userDAO.allUsers.contains(testUser));
    }

    @Test
    @Order(2)
    @DisplayName("Create user Negative Test Case")
    public void createExisting() throws AlreadyTakenException {
        //create a user using userData
        UserData testUser = new UserData("admin", "admin", "admin");
        UserData duplicateUser = new UserData("admin", "admin", "admin");
        MemoryUserDAO userDAO = new MemoryUserDAO();
        userDAO.createUser(testUser);
        assertThrows(AlreadyTakenException.class, () -> userDAO.createUser(duplicateUser));
    }

    @Test
    @Order(3)
    @DisplayName("Get user postive test case")
    public void getUserTest() throws DataAccessException, AlreadyTakenException {
        //create a user using userData
        UserData testUser = new UserData("admin", "admin", "admin");
        MemoryUserDAO userDAO = new MemoryUserDAO();
        userDAO.createUser(testUser);
        assertEquals(userDAO.getUser(testUser.username()), testUser);
    }

    @Test
    @Order(4)
    @DisplayName("Get user negative test case")
    public void getInvalidUser() {
        //create a user using userData
        MemoryUserDAO userDAO = new MemoryUserDAO();
        assertThrows(DataAccessException.class, () -> userDAO.getUser(new UserData("admin", "admin", "admin").username()));
    }

    @Test
    @Order(5)
    @DisplayName("Test Clear AuthData")
    public void clearAuthData() {
        RegisterRequest registerRequest = new RegisterRequest(user.username(), user.password(), user.email());
        RegisterResponse registerResponse = userService.register(registerRequest);
        authService.clear();
        assertThrows(UnauthorizedException.class, () -> authService.getAuth(registerResponse.authToken()));
    }

    @Test
    @Order(5)
    @DisplayName("clear tests")
    public void clearTest() throws AlreadyTakenException {
        //create a user using userData
        UserData testUser = new UserData("admin", "admin", "admin");
        MemoryUserDAO userDAO = new MemoryUserDAO();
        userDAO.createUser(testUser);
        userDAO.clearAllUsers();
        assertTrue(userDAO.allUsers.isEmpty());
    }

    @Test
    @Order(7)
    @DisplayName("Create game good")
    public void createGame() {
        RegisterRequest registerRequest = new RegisterRequest(user.username(), user.password(), user.email());
        RegisterResponse registerResponse = userService.register(registerRequest);
        CreateGameRequest createGameRequest = new CreateGameRequest(registerResponse.authToken(), "newGame");
        assertDoesNotThrow(() -> gameService.createGame(createGameRequest));
    }

    @Test
    @Order(8)
    @DisplayName("create game bad")
    public void createBadGame() {
        RegisterRequest registerRequest = new RegisterRequest(user.username(), user.password(), user.email());
        userService.register(registerRequest);
        CreateGameRequest createGameRequest = new CreateGameRequest("I'm a bad authToken", "newGame");
        assertThrows(ServiceException.class, () -> gameService.createGame(createGameRequest));
    }

    @Test
    @Order(9)
    @DisplayName("join game good")
    public void joinGame() {
        RegisterRequest registerRequest = new RegisterRequest(user.username(), user.password(), user.email());
        RegisterResponse registerResponse = userService.register(registerRequest);
        CreateGameRequest createGameRequest = new CreateGameRequest(registerResponse.authToken(), "newGame");
        CreateGameResponse gameResponse = gameService.createGame(createGameRequest);
        JoinGameRequest joinGameRequest = new JoinGameRequest(registerResponse.authToken(), "WHITE", gameResponse.gameID());
        assertDoesNotThrow(() -> gameService.joinGame(joinGameRequest));
    }

    @Test
    @Order(10)
    @DisplayName("join game bad")
    public void joinBadGame() {
        RegisterRequest registerRequest = new RegisterRequest(user.username(), user.password(), user.email());
        RegisterResponse registerResponse = userService.register(registerRequest);
        CreateGameRequest createGameRequest = new CreateGameRequest(registerResponse.authToken(), "newGame");
        CreateGameResponse gameResponse = gameService.createGame(createGameRequest);
        JoinGameRequest joinGameRequest = new JoinGameRequest("dsfsas", "WHITE", gameResponse.gameID());
        assertThrows(ServiceException.class, () -> gameService.joinGame(joinGameRequest));
    }


    @Test
    @Order(11)
    @DisplayName("clear games")
    public void clearAllGames() {
        Integer[] expectedGames = new Integer[2];
        RegisterRequest registerRequest = new RegisterRequest(user.username(), user.password(), user.email());
        RegisterResponse registerResponse = userService.register(registerRequest);
        CreateGameRequest createGameRequest = new CreateGameRequest(registerResponse.authToken(), "newGame");
        CreateGameResponse gameResponse = gameService.createGame(createGameRequest);
        expectedGames[0] = gameResponse.gameID();

        CreateGameRequest createGameRequest1 = new CreateGameRequest(registerResponse.authToken(), "anotherGame");
        CreateGameResponse gameResponse1 = gameService.createGame(createGameRequest1);
        expectedGames[1] = gameResponse1.gameID();
        ListGamesResponse listGamesResponse = gameService.list(registerResponse.authToken());
        gameService.clear();
        assertNotEquals(expectedGames, listGamesResponse.games());
    }

    @Test
    @Order(12)
    @DisplayName("listGames pos test")
    public void listGames() {

        RegisterRequest registerRequest = new RegisterRequest(user.username(), user.password(), user.email());
        RegisterResponse registerResponse = userService.register(registerRequest);
        CreateGameRequest createGameRequest = new CreateGameRequest(registerResponse.authToken(), "newGame");
        gameService.createGame(createGameRequest);

        CreateGameRequest createGameRequest1 = new CreateGameRequest(registerResponse.authToken(), "anotherGame");
        gameService.createGame(createGameRequest1);

        assertDoesNotThrow(() -> gameService.list(registerResponse.authToken()));
    }

    @Test
    @Order(13)
    @DisplayName("listGames neg test")
    public void badListGames() {
        RegisterRequest registerRequest = new RegisterRequest(user.username(), user.password(), user.email());
        RegisterResponse registerResponse = userService.register(registerRequest);
        CreateGameRequest createGameRequest = new CreateGameRequest(registerResponse.authToken(), "newGame");
        gameService.createGame(createGameRequest);

        CreateGameRequest createGameRequest1 = new CreateGameRequest(registerResponse.authToken(), "anotherGame");
        gameService.createGame(createGameRequest1);

        assertThrows(ServiceException.class, () -> gameService.list("bad authToken"));
    }

    @Test
    @Order(14)
    @DisplayName("register")
    public void register() {
        RegisterRequest registerRequest = new RegisterRequest(user.username(), user.password(), user.email());
        RegisterResponse registerResponse = userService.register(registerRequest);
        assertNotNull(registerResponse);
    }

    @Test
    @Order(15)
    @DisplayName("register")
    public void registerBad() {
        RegisterRequest registerRequest = new RegisterRequest(null, user.password(), user.email());
        assertThrows(ServiceException.class, () -> userService.register(registerRequest));
    }

    @Test
    @Order(16)
    @DisplayName("login good")
    public void login() {
        RegisterRequest registerRequest = new RegisterRequest(user.username(), user.password(), user.email());
        userService.register(registerRequest);
        assertDoesNotThrow(() -> userService.login(new LoginRequest(user.username(), user.password())));
    }

    @Test
    @Order(17)
    @DisplayName("login bad")
    public void loginBad() {
        RegisterRequest registerRequest = new RegisterRequest(user.username(), user.password(), user.email());
        userService.register(registerRequest);
        assertThrows(ServiceException.class, () -> userService.login(new LoginRequest(user.username(), "badPassword")));
    }

    @Test
    @Order(18)
    @DisplayName("logout")
    public void logout() {
        RegisterRequest registerRequest = new RegisterRequest(user.username(), user.password(), user.email());
        userService.register(registerRequest);
        LoginResponse loginResponse = userService.login(new LoginRequest(user.username(), user.password()));
        assertDoesNotThrow(() -> userService.logout(new LogoutRequest(loginResponse.authToken())));
    }

    @Test
    @Order(19)
    @DisplayName("bad logout")
    public void badLogout() {
        RegisterRequest registerRequest = new RegisterRequest(user.username(), user.password(), user.email());
        userService.register(registerRequest);
        userService.login(new LoginRequest(user.username(), user.password()));
        assertThrows(ServiceException.class, () -> userService.logout(new LogoutRequest("badAuth")));
    }

    @Test
    @Order(20)
    @DisplayName("clear")
    public void clearUsers() {
        RegisterRequest registerRequest = new RegisterRequest(user.username(), user.password(), user.email());
        userService.register(registerRequest);
        assertDoesNotThrow(() -> userService.login(new LoginRequest(user.username(), user.password())));
        assertDoesNotThrow(() -> userService.clear());
    }


}
