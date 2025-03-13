package dataaccess;

import model.AuthData;

public interface AuthDAO {
    /**
     * creates a new authorization given AuthData
     */
    void createAuth(AuthData authData) throws DataAccessException; //authData service will PROVIDE authToken

    /**
     * @return AuthData if there exists an authorization for the given authToken.
     * @throws DataAccessException if the given authToken does not exist
     */
    AuthData getAuth(String authToken) throws UnauthorizedException;

    /**
     * Deletes an authorization given an authToken
     *
     * @throws UnauthorizedException if the given authToken does not exist
     */
    public void deleteAuth(String authToken) throws UnauthorizedException;

    /**
     * Deletes ALL current AuthData
     */
    void clearAllAuth();
        /*
     * returns true if there exists and authorization for the given authToken
        not sure if im going to add this or not, since its very similar to getAuth
    boolean verifiedAuth(String authToken);
     */
}
