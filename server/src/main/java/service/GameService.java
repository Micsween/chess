package service;


import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.ServerDAOs;
import model.GameData;
import service.requests.CreateGameRequest;
import service.responses.ClearResponse;
import service.responses.CreateGameResponse;
import service.responses.ServiceException;

import java.util.Random;

public class GameService {
    ServerDAOs serverDAOs;
    MemoryAuthDAO memoryAuthDAO;
    MemoryGameDAO memoryGameDAO;

    public GameService(ServerDAOs serverDAOs) {
        this.serverDAOs = serverDAOs;
        this.memoryAuthDAO = serverDAOs.memoryAuthDAO();
        this.memoryGameDAO = serverDAOs.memoryGameDAO();
    }

    public static String createGameID() {
        Random random = new Random();
        StringBuilder ID = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int randomDigit = random.nextInt(10);
            ID.append(randomDigit);
        }
        return ID.toString();
    }

    public CreateGameResponse createGame(CreateGameRequest request) throws ServiceException {
        if (request.gameName() == null) {
            throw new ServiceException(400, "Error: bad request");
        }
        try {
            memoryAuthDAO.getAuth(request.authToken());
        } catch (DataAccessException e) {
            throw new ServiceException(401, "Error: unauthorized");
        }
        try {
            String gameID = createGameID();
            GameData game = new GameData(gameID, "", "", request.gameName(), new ChessGame());
            memoryGameDAO.createGame(game);
            return new CreateGameResponse(gameID);
        } catch (DataAccessException e) {
            throw new ServiceException(400, "Error: bad request");
        }
    }

    public ClearResponse clear() {
        memoryGameDAO.clearAllGames();
        return new ClearResponse();
    }

}


