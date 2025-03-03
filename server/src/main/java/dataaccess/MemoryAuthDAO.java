package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryAuthDAO {
    //authData service will PROVIDE authToken
    Collection<AuthData> allAuthData = new ArrayList<AuthData>();

    public void createAuth(AuthData authData) throws DataAccessException {
        if (allAuthData.contains(authData)) {
            throw new DataAccessException("Auth data already exists");
        } else {
            allAuthData.add(authData);
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        for (AuthData authData : allAuthData) {
            if (authData.authToken().equals(authToken)) {
                return authData;
            }
        }
        throw new DataAccessException("Auth token does not match");
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        AuthData auth = getAuth(authToken);
        if (auth != null) {
            allAuthData.remove(auth);
        } else {
            throw new RuntimeException("Auth token does not exist");
        }
    }

    public void clearAllAuth() {
        allAuthData.clear();
    }
}