package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.ServerDAOs;

public class AuthService {
    ServerDAOs DAOs;
    MemoryAuthDAO memoryAuthDAO;
    MemoryUserDAO memoryUserDAO;

    public AuthService(dataaccess.ServerDAOs serverDAOs) {
        this.DAOs = serverDAOs;
        this.memoryAuthDAO = serverDAOs.memoryAuthDAO();
        this.memoryUserDAO = serverDAOs.memoryUserDAO();
    }

    public void clear() {
        memoryAuthDAO.clearAllAuth();
    }
}
