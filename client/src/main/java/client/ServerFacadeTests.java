package client;

import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import service.responses.LoginResponse;
import service.responses.RegisterResponse;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ServerFacadeTests {
    static ServerFacade serverFacade;
    private static Server server;
    String authToken;

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

    @AfterAll
    static void finish() {
        serverFacade.clear();
    }

    @Test
    @Order(1)
    @DisplayName("Register")
    void register() {
        UserData userData = new UserData("test-user", "test-pass", "test-email");
        RegisterResponse register = serverFacade.register(userData);
        this.authToken = register.authToken();

    }

//    @Test
//    @Order(2)
//    void registerDuplicate() {
//        UserData userData = new UserData("test-user", null, "test-email");
//        //assertThrows(Exception.class, () -> serverFacade.register(userData));
//    }

    @Test
    @Order(2)
    @DisplayName("Logout")
    void logout() {
        UserData userData = new UserData("test-user", "test-pass", null);
        assertDoesNotThrow(() -> {
            serverFacade.login(userData);
            LoginResponse login = serverFacade.login(userData);
            this.authToken = login.authToken();
        });
        assertDoesNotThrow(() -> serverFacade.logout(authToken));


    }

    @Test
    @Order(3)
    @DisplayName("Login")
    void login() {
        UserData userData = new UserData("test-user", "test-pass", null);
        assertDoesNotThrow(() -> serverFacade.login(userData));
    }


    @Test
    void listGames() {
    }

    @Test
    void createGame() {
    }

    @Test
    void joinPlayer() {
    }

    @Test
    void clear() {
        serverFacade.clear();
    }
}