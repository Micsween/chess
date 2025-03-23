package client;

import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import service.responses.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ServerFacadeTests {
    static ServerFacade serverFacade;
    private static Server server;
    UserData userData = new UserData("test-user", "test-pass", "test-email");

    @BeforeAll
    public static void init() {
        server = new Server();
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

//    @Test
//    @Order(2)
//    void registerDuplicate() {
//        UserData userData = new UserData("test-user", null, "test-email");
//        //assertThrows(Exception.class, () -> serverFacade.register(userData));
//    }


    @Test
    @DisplayName("Login")
    void login() {
        serverFacade.register(userData);
        assertDoesNotThrow(() -> serverFacade.login(userData));
    }


    @Test
    @Order(2)
    @DisplayName("Logout")
    void logout() {
        RegisterResponse registerResponse = serverFacade.register(userData);
        String authToken = registerResponse.authToken();
        assertDoesNotThrow(() -> serverFacade.logout(authToken));
    }


    @Test
    void listGames() {
        RegisterResponse registerResponse = serverFacade.register(userData);
        String authToken = registerResponse.authToken();
        ListGamesResponse listGamesResponse = serverFacade.listGames(authToken);
        assertNotNull(listGamesResponse);
    }


    @Test
    void createGame() {
        RegisterResponse registerResponse = serverFacade.register(userData);
        CreateGameResponse createGameResponse = serverFacade.createGame("GAME!", registerResponse.authToken());
        assertNotNull(createGameResponse);
    }

    @Test
    void joinPlayer() {
        RegisterResponse registerResponse = serverFacade.register(userData);
        CreateGameResponse createGameResponse = serverFacade.createGame("GAME!", registerResponse.authToken());

        serverFacade.joinPlayer(registerResponse.authToken(), "WHITE", createGameResponse.gameID());
        ListGamesResponse listGamesResponse = serverFacade.listGames(registerResponse.authToken());
        assertNotNull(listGamesResponse);//add a command that gets a game
    }

    @Test
    void clear() {
        serverFacade.clear();
    }
}