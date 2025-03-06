package dataaccess;

import model.AuthData;

public class DBAuthDAO implements AuthDao {
    public void createAuth(AuthData authData) {
        
    }

    public AuthData getAuth(String authToken) throws UnauthorizedException {
        return null;
    }

    public void deleteAuth(String authToken) throws DataAccessException {

    }

    public void clearAllAuth() {
    }
}
