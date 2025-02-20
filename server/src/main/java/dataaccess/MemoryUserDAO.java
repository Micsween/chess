package dataaccess;

import model.UserData;

public record MemoryUserDAO() implements UserDAO {

    public void createUser(UserData userData){throw new RuntimeException("Not Implemented");};
    public UserData getUser(String username) throws DataAccessException{throw new RuntimeException("Not Implemented"); }
    public UserData verifyUser(String username, String password) throws DataAccessException{throw new RuntimeException("Not Implemented");}
    public void clearAllUsers(){throw new RuntimeException("Not Implemented");}

}
