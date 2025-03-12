package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryAuthDAO implements AuthDAO {
    //authData service will PROVIDE authToken
    Collection<AuthData> allAuthData = new ArrayList<AuthData>();

    public void createAuth(AuthData authData) {
        allAuthData.add(authData);
    }

    public AuthData getAuth(String authToken) throws UnauthorizedException {
        for (AuthData authData : allAuthData) {
            if (authData.authToken().equals(authToken)) {
                return authData;
            }
        }
        throw new UnauthorizedException();
    }

    public void deleteAuth(String authToken) throws UnauthorizedException {
        AuthData auth = getAuth(authToken);
        allAuthData.remove(auth);
    }

    public void clearAllAuth() {
        allAuthData.clear();
    }
}