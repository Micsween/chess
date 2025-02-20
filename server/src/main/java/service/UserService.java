package service;

import dataaccess.UserDAO;
import service.requests.LoginRequest;
import service.requests.LogoutRequest;
import service.requests.RegisterRequest;
import service.responses.RegisterResult;

public class UserService {
    private static UserDAO userDAO;
    public RegisterResult register(RegisterRequest registerRequest) {
        //service talks to DAO
        throw new RuntimeException("Not Implemented");}
    //public LoginResult login(LoginRequest loginRequest) {}
    //public void logout(LogoutRequest logoutRequest) {}
}
