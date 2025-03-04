package service;

import dataaccess.*;

public class AuthService {
    ServerDaos daos;
    MemoryAuthDAO memoryAuthDao;
    MemoryUserDAO memoryUserDao;

    public AuthService(ServerDaos serverDAOs) {
        this.daos = serverDAOs;
        this.memoryAuthDao = serverDAOs.memoryAuthDAO();
        this.memoryUserDao = serverDAOs.memoryUserDAO();
    }

    public void getAuth(String authKey) throws UnauthorizedException {
        memoryAuthDao.getAuth(authKey);
    }

    public void clear() {
        memoryAuthDao.clearAllAuth();
    }
}
