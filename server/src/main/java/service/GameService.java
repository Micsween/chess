package service;


import dataaccess.MemoryGameDAO;
import service.responses.ClearResponse;

public class GameService {
    MemoryGameDAO memoryGameDAO = new MemoryGameDAO();

    public ClearResponse clear() {
        memoryGameDAO.clearAllGames();
        return new ClearResponse();
    }

}

//Game Service
//create
//jooin
//list
