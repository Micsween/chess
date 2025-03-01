package dataaccess;

import model.UserData;

import java.util.Collection;

public class MemoryUserDAO implements UserDAO {
    Collection<UserData> allUsers;

    public void createUser(UserData userData){
        allUsers.add(userData);
    }

    public UserData getUser(String username) throws DataAccessException{
        for(UserData userdata : allUsers) {
            if(userdata.username().equals(username)){
                return userdata;
            }
        }
        return null;
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
