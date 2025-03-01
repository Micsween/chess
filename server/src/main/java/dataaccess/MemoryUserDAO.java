package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryUserDAO implements UserDAO {
    public Collection<UserData> allUsers = new ArrayList<>();

    public void createUser(UserData userData) throws DataAccessException {

        if(!allUsers.contains(userData)){
            allUsers.add(userData);
        }else {
            throw new DataAccessException("This user already exists");
        }
    }

    public UserData getUser(String username) throws DataAccessException{
        for(UserData userdata : allUsers) {
            if(userdata.username().equals(username)){
                return userdata;
            }
        }
        throw new DataAccessException("User not found");
    }

    public UserData verifyUser(String username, String password) throws DataAccessException{
        UserData user = getUser(username);
        if(user != null && user.username().equals(username) && user.password().equals(password)) {
            return user;
        }else{
            throw new DataAccessException("User not found or password not correct");
        }
    }

    public void clearAllUsers(){
        allUsers.clear();
    }

}
