package dataaccess;

import model.AuthData;

public interface AuthDao {
    /**
     * creates a new authorization given AuthData
     */
    public void createAuth(AuthData authData); //authData service will PROVIDE authToken
    /**
     * returns AuthData if there exists and authorization for the given authToken, null if otherwise
     */
    public AuthData getAuth(String authToken); //returns AuthData; returns null if there is non matching the given authtoken
    /**
     * returns true if there exists and authorization for the given authToken
     */
    public boolean verifiedAuth(String authToken);
    /**
     * deletes an authorization given an authToken
     */
    public void deleteAuth(String authToken);
}
