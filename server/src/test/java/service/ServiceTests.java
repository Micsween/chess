package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import dataaccess.*;
import model.UserData;


import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {

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
        assertThrows(DataAccessException.class, () -> userDAO.createUser(duplicateUser));
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
    public void getInvalidUser() throws DataAccessException {
        //create a user using userData
        MemoryUserDAO userDAO = new MemoryUserDAO();
        assertThrows(DataAccessException.class, () -> userDAO.getUser(new UserData("admin", "admin", "admin").username()));
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