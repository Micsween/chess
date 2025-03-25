package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import model.responses.*;
import model.requests.LoginRequest;
import model.requests.LogoutRequest;
import model.requests.RegisterRequest;

import java.util.UUID;

public class UserService {
    ServerDaos daos;
    AuthDAO authDAO;
    UserDAO userDAO;

    public UserService(ServerDaos serverDAOs) {
        this.daos = serverDAOs;
        this.authDAO = serverDAOs.authDao();
        this.userDAO = serverDAOs.userDAO();
    }

    public RegisterResponse register(RegisterRequest registerRequest) throws ServiceException {
        if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null) {
            throw new ServiceException(400, "Error: bad request");
        }
        try {
            userDAO.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(authToken, registerRequest.username());
            authDAO.createAuth(authData);
            return new RegisterResponse(authData.authToken(), authData.username());
        } catch (AlreadyTakenException e) {
            throw new ServiceException(403, e.getMessage());
        } catch (DataAccessException e) {
            throw new ServiceException(401, e.getMessage());
        }
    }

    public LoginResponse login(LoginRequest loginRequest) throws ServiceException {
        if (loginRequest.username() == null || loginRequest.password() == null) {
            throw new ServiceException(400, "Error: bad request");
        }
        try {
            UserData user = userDAO.verifyUser(loginRequest.username(), loginRequest.password());
            String authKey = UUID.randomUUID().toString();
            authDAO.createAuth(new AuthData(authKey, loginRequest.username()));
            return new LoginResponse(user.username(), authKey);
        } catch (DataAccessException e) {
            throw new ServiceException(401, "Error: unauthorized");
        }

    }

    public LogoutResponse logout(LogoutRequest logoutRequest) throws ServiceException {
        try {
            authDAO.deleteAuth(logoutRequest.authToken());
        } catch (UnauthorizedException e) {
            throw new ServiceException(401, e.getMessage());
        }
        return new LogoutResponse();
    }

    public ClearResponse clear() {
        userDAO.clearAllUsers();
        return new ClearResponse();
    }

}
