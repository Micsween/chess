package dataaccess;

import model.AuthData;

public interface AuthDao {
    /**
     * creates a new authorization given AuthData
     */
    void createAuth(AuthData authData); //authData service will PROVIDE authToken
    /**
     * returns AuthData if there exists and authorization for the given authToken, null if otherwise
     */
    AuthData getAuth(String authToken); //returns AuthData; returns null if there is non matching the given authtoken
    /**
     * returns true if there exists and authorization for the given authToken
     */
    boolean verifiedAuth(String authToken);
    /**
     * deletes an authorization given an authToken
     */
    void deleteAuth(String authToken);

    /**
     * Deletes ALL current AuthData
     */
    void clearAllAuth();
}
