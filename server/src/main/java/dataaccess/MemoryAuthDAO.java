package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryAuthDAO {
    //authData service will PROVIDE authToken
    Collection<AuthData> allAuthData = new ArrayList<AuthData>();
    public void createAuth(AuthData authData){
        if(!allAuthData.contains(authData)){
            allAuthData.add(authData);
        }else{
            throw new RuntimeException("Auth data already exists");
        }
    }

    public AuthData getAuth(String authToken) {
        for(AuthData authData : allAuthData){
            if(authData.authToken().equals(authToken)) {
                return authData;
            }
        }
        throw new RuntimeException("Auth token does not match");
    }

    public void deleteAuth(String authToken) {
        AuthData auth = getAuth(authToken);
        if(auth != null) {
            allAuthData.remove(auth);
        }else{
            throw new RuntimeException("Auth token does not exist");
        }
    }

    public void clearAllAuth(){
        allAuthData.clear();
    }
}