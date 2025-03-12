package passoff.server;

import dataaccess.DatabaseManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseSetupTests {

    @Test
    @Order(1)
    @DisplayName("Create Database works")
    public void create() {
        assertDoesNotThrow(DatabaseManager::createDatabase, "CreateDatabase should not throw an exception.");
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
