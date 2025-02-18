package dataaccess;

import model.UserData;

public interface UserDAO {
    /**
     * Creates a new user
     * @param userData New user's user data
     */
    void createUser(UserData userData);
    /**
     *
     * @param username given username
     * @return UserData
     * @throws DataAccessException if the given username does not exist.
     */
    UserData getUser(String username) throws DataAccessException;

    /**
     * Retrieves UserData given a username and password
     * @param username given username
     * @param password given password
     * @return UserData, verifying the user exists and the password is correct
     * @throws DataAccessException if either the username or password does not match
     */
    UserData verifyUser(String username, String password) throws DataAccessException;
    void clearAllUsers();
}
