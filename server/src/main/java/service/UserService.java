package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import service.requests.LoginRequest;
import service.requests.LogoutRequest;
import service.requests.RegisterRequest;
import service.responses.*;

import java.util.UUID;

public class UserService {
    ServerDAOs DAOs;
    MemoryAuthDAO memoryAuthDAO;
    MemoryUserDAO memoryUserDAO;

    public UserService(dataaccess.ServerDAOs serverDAOs) {
        this.DAOs = serverDAOs;
        this.memoryAuthDAO = serverDAOs.memoryAuthDAO();
        this.memoryUserDAO = serverDAOs.memoryUserDAO();
    }

    public RegisterResponse register(RegisterRequest registerRequest) throws ServiceException {
        if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null) {
            throw new ServiceException(400, "Error: bad request");
        }
        try {
            memoryUserDAO.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(authToken, registerRequest.username());
            memoryAuthDAO.createAuth(authData);
            return new RegisterResponse(authData.authToken(), authData.username());
        } catch (AlreadyTakenException e) {
            throw new ServiceException(403, e.getMessage());
        }
    }

    public LoginResponse login(LoginRequest loginRequest) throws ServiceException {
        if (loginRequest.username() == null || loginRequest.password() == null) {
            throw new ServiceException(400, "Error: bad request");
        }
        try {
            UserData user = memoryUserDAO.verifyUser(loginRequest.username(), loginRequest.password());
            String authKey = UUID.randomUUID().toString();
            memoryAuthDAO.createAuth(new AuthData(authKey, loginRequest.username()));
            return new LoginResponse(user.username(), authKey);
        } catch (DataAccessException e) {
            throw new ServiceException(401, "Error: unauthorized");
        }

    }

    public LogoutResponse logout(LogoutRequest logoutRequest) throws ServiceException {
        try {
            memoryAuthDAO.deleteAuth(logoutRequest.authToken());
        } catch (UnauthorizedException e) {
            throw new ServiceException(401, e.getMessage());
        }
        return new LogoutResponse();
    }

    public ClearResponse clear() {
        memoryUserDAO.clearAllUsers();
        return new ClearResponse();
    }

}
