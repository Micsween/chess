package client;

import model.UserData;
import model.responses.CreateGameResponse;
import model.responses.ListGamesResponse;
import model.responses.RegisterResponse;
import org.junit.jupiter.api.*;
import server.HttpServer;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ServerFacadeTests {
    static ServerFacade serverFacade;
    private static HttpServer server;
    UserData userData = new UserData("test-user", "test-pass", "test-email");

    @BeforeAll
    public static void init() {
        server = new HttpServer();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("localhost", Integer.toString(port));
    }

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void finish() {
        serverFacade.clear();
    }

    @Test
    @DisplayName("Register")
    void register() {
        RegisterResponse register = serverFacade.register(userData);
    }

    @Test
    void registerDuplicate() {
        RegisterResponse register = serverFacade.register(userData);
        assertThrows(ClientException.class, () -> serverFacade.register(userData));
    }


    @Test
    @DisplayName("Login")
    void login() {
        serverFacade.register(userData);
        assertDoesNotThrow(() -> serverFacade.login(userData));
    }

    @Test
    @DisplayName("Login Incorrect Password")
    void loginIncorrectPassword() {
        serverFacade.register(userData);
        assertThrows(ClientException.class, () -> serverFacade.login(new UserData("test-user", "bad-password", "test-email")));
    }

    @Test
    @DisplayName("Logout")
    void logout() {
        RegisterResponse registerResponse = serverFacade.register(userData);
        String authToken = registerResponse.authToken();
        assertDoesNotThrow(() -> serverFacade.logout(authToken));
    }

    @Test
    @DisplayName("Logout incorrect authToken")
    void logoutIncorrectAuthToken() {
        serverFacade.register(userData);
        assertThrows(ClientException.class, () -> serverFacade.logout("bad auth token"));
    }


    @Test
    @DisplayName("List Games")
    void listGames() {
        RegisterResponse registerResponse = serverFacade.register(userData);
        String authToken = registerResponse.authToken();
        ListGamesResponse listGamesResponse = serverFacade.listGames(authToken);
        assertNotNull(listGamesResponse);
    }

    @Test
    @DisplayName("List Games bad auth")
    void listGamesBadAuth() {
        serverFacade.register(userData);
        assertThrows(ClientException.class, () -> serverFacade.listGames("bad auth token"));
    }


    @Test
    void createGame() {
        RegisterResponse registerResponse = serverFacade.register(userData);
        CreateGameResponse createGameResponse = serverFacade.createGame("GAME", registerResponse.authToken());
        System.out.println(createGameResponse);
        assertNotNull(createGameResponse);
    }

    @Test
    @DisplayName("Bad Game")
    void createBadGame() {
        assertThrows(ClientException.class, () -> serverFacade.createGame("GAME!", "bad auth token"));
    }

    @Test
    @DisplayName("Join player")
    void joinPlayer() {
        RegisterResponse registerResponse = serverFacade.register(userData);
        CreateGameResponse createGameResponse = serverFacade.createGame("GAME!", registerResponse.authToken());

        serverFacade.joinPlayer(registerResponse.authToken(), "WHITE", createGameResponse.gameID());
        ListGamesResponse listGamesResponse = serverFacade.listGames(registerResponse.authToken());
        assertNotNull(listGamesResponse);//add a command that gets a game
    }

    @Test
    @DisplayName("Join player color already taken")
    void joinPlayerColorAlreadyTaken() {
        RegisterResponse registerResponse = serverFacade.register(userData);
        CreateGameResponse createGameResponse = serverFacade.createGame("GAME!", registerResponse.authToken());

        serverFacade.joinPlayer(registerResponse.authToken(), "WHITE", createGameResponse.gameID());
        assertThrows(ClientException.class, () -> serverFacade.joinPlayer(registerResponse.authToken(), "WHITE", createGameResponse.gameID()));
    }


    @Test
    void clear() {
        serverFacade.clear();
    }
}