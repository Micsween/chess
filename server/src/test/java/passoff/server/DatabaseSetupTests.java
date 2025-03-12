package passoff.server;

import dataaccess.DBAuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseSetupTests {
    DBAuthDAO dbAuthDAO = new DBAuthDAO();

    @BeforeEach
    public void setUp() {
        //serverFacade.clear();
    }

    @AfterAll
    static void stopServer() {

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
