package service;


import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import service.requests.CreateGameRequest;
import service.responses.ClearResponse;
import service.responses.CreateGameResponse;
import service.responses.ServiceException;

public class GameService {
    MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
    MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();

    public CreateGameResponse createGame(CreateGameRequest request) throws ServiceException {
        if (request.gameName() == null) {
            throw new ServiceException(400, "Error: bad request");
        }
        try {
            memoryAuthDAO.getAuth(request.authToken());
        } catch (DataAccessException e) {
            throw new ServiceException(400, "Error: unauthorized");
        }
        try {
            memoryGameDAO.createGame(request.gameName());
            return new CreateGameResponse(request.gameName());
        } catch (DataAccessException e) {
            throw new ServiceException(400, "Error: bad request");
        }
    }

    public ClearResponse clear() {
        memoryGameDAO.clearAllGames();
        return new ClearResponse();
    }

}

//Game Service
//create
//jooin
//list
