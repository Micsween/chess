package service;

import dataaccess.*;

public class AuthService {
    ServerDaos daos;
    AuthDAO memoryAuthDao;
    UserDAO memoryUserDao;

    public AuthService(ServerDaos serverDAOs) {
        this.daos = serverDAOs;
        this.memoryAuthDao = serverDAOs.authDao();
        this.memoryUserDao = serverDAOs.userDAO();
    }

    public void getAuth(String authKey) throws UnauthorizedException {
        memoryAuthDao.getAuth(authKey);
    }

    public void clear() {
        memoryAuthDao.clearAllAuth();
    }
}
