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
    ServerDaos Daos;
    MemoryAuthDAO memoryAuthDao;
    MemoryUserDAO memoryUserDao;

    public UserService(ServerDaos serverDAOs) {
        this.Daos = serverDAOs;
        this.memoryAuthDao = serverDAOs.memoryAuthDAO();
        this.memoryUserDao = serverDAOs.memoryUserDAO();
    }

    public RegisterResponse register(RegisterRequest registerRequest) throws ServiceException {
        if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null) {
            throw new ServiceException(400, "Error: bad request");
        }
        try {
            memoryUserDao.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(authToken, registerRequest.username());
            memoryAuthDao.createAuth(authData);
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
            UserData user = memoryUserDao.verifyUser(loginRequest.username(), loginRequest.password());
            String authKey = UUID.randomUUID().toString();
            memoryAuthDao.createAuth(new AuthData(authKey, loginRequest.username()));
            return new LoginResponse(user.username(), authKey);
        } catch (DataAccessException e) {
            throw new ServiceException(401, "Error: unauthorized");
        }

    }

    public LogoutResponse logout(LogoutRequest logoutRequest) throws ServiceException {
        try {
            memoryAuthDao.deleteAuth(logoutRequest.authToken());
        } catch (UnauthorizedException e) {
            throw new ServiceException(401, e.getMessage());
        }
        return new LogoutResponse();
    }

    public ClearResponse clear() {
        memoryUserDao.clearAllUsers();
        return new ClearResponse();
    }

}
