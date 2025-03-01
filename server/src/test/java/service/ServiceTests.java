package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import dataaccess.*;
import model.UserData;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {
    @Test
    @DisplayName("Create user Positive Test")
    public void createUserTest() throws DataAccessException {
        //create a user using userData
        UserData testUser = new UserData("admin", "admin", "admin");
        MemoryUserDAO userDAO = new MemoryUserDAO();
        userDAO.createUser(testUser);
        assertTrue(userDAO.allUsers.contains(testUser));
    }
    @Test
    @DisplayName("Create user Negative Test Case")
    public void createInvalidUser() throws DataAccessException {
        //create a user using userData
        UserData testUser = new UserData("admin", "admin", "admin");
        UserData duplicateUser = new UserData("admin", "admin", "admin");
        MemoryUserDAO userDAO = new MemoryUserDAO();
        userDAO.createUser(testUser);
        assertThrows(DataAccessException.class, () -> userDAO.createUser(duplicateUser));
    }
}

/*
   void createUser(UserData userData);

UserData getUser(String username) throws DataAccessException;

UserData verifyUser(String username, String password) throws DataAccessException;
void clearAllUsers();
*   @Test
    @Order(1)
    @DisplayName("Static Files")
    public void staticFiles() {
        String htmlFromServer = serverFacade.file("/").replaceAll("\r", "");
        Assertions.assertEquals(HttpURLConnection.HTTP_OK, serverFacade.getStatusCode(),
                "Server response code was not 200 OK");
        Assertions.assertNotNull(htmlFromServer, "Server returned an empty file");
        Assertions.assertTrue(htmlFromServer.contains("CS 240 Chess Server Web API"),
                "file returned did not contain an exact match of text from provided index.html");
    }*/