package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import service.requests.LoginRequest;
import service.requests.LogoutRequest;
import service.requests.RegisterRequest;
import service.responses.*;

import java.util.UUID;

public class UserService {
    MemoryUserDAO memoryUserDAO = new MemoryUserDAO();
    MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();

    public RegisterResponse register(RegisterRequest registerRequest) throws ServiceException {
        if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null) {
            throw new ServiceException(400, "Error: bad request");
        }
        try {
            memoryUserDAO.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
        } catch (DataAccessException e) {
            throw new ServiceException(403, "Error: already taken");
        }
        return new RegisterResponse(registerRequest.username(), UUID.randomUUID().toString());
    }

    public LoginResponse login(LoginRequest loginRequest) throws ServiceException {
        if (loginRequest.username() == null || loginRequest.password() == null) {
            throw new ServiceException(401, "Error: unauthorized");
        }
        //if something is missing throw an error
        //if the data doesn't match anything in the memory user dao, verifyUser will also throw an error
        try {
            UserData user = memoryUserDAO.verifyUser(loginRequest.username(), loginRequest.password());
            memoryAuthDAO.createAuth(new AuthData(UUID.randomUUID().toString(), loginRequest.username()));
            return new LoginResponse(user.username(), user.password());
        } catch (DataAccessException e) {
            throw new ServiceException(401, "Error: unauthorized");
        }

    }

    public LogoutResponse logout(LogoutRequest logoutRequest) throws ServiceException {
        if (logoutRequest.authToken() == null) {
            throw new ServiceException(401, "Error: unauthorized");
        }
        memoryAuthDAO.deleteAuth(logoutRequest.authToken());
        return new LogoutResponse();
    }

    public ClearResponse clear() {
        memoryUserDAO.clearAllUsers();
        return new ClearResponse();
    }

}
