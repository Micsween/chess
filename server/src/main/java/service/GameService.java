package service;


import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import service.requests.*;
import service.responses.*;


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
        } catch (UnauthorizedException e) {
            throw new ServiceException(401, e.getMessage());
        }
        try {
            String gameID = createGameID();
            GameData game = new GameData(gameID, null, null, request.gameName(), new ChessGame());
            memoryGameDAO.createGame(game);
            return new CreateGameResponse(gameID);
        } catch (DataAccessException e) {
            throw new ServiceException(400, "Error: bad request");
        }
    }

    public JoinGameResponse joinGame(JoinGameRequest joinGameRequest) throws ServiceException {
        if (joinGameRequest.authToken() == null || joinGameRequest.playerColor() == null || (!joinGameRequest.playerColor().equals("WHITE") && !joinGameRequest.playerColor().equals("BLACK")) || joinGameRequest.gameID() == null) {
            throw new ServiceException(400, "Error: bad request");
        }
        try {
            AuthData authData = memoryAuthDAO.getAuth(joinGameRequest.authToken());
            memoryGameDAO.joinGame(authData.username(), joinGameRequest.playerColor(), joinGameRequest.gameID());
            return new JoinGameResponse();
        } catch (DataAccessException e) {
            throw new ServiceException(400, e.getMessage());
        } catch (AlreadyTakenException e) {
            throw new ServiceException(403, e.getMessage());
        } catch (UnauthorizedException e) {
            throw new ServiceException(401, e.getMessage());
        }

    }

    public ListGamesResponse list(String authKey) {
        try {
            AuthData authData = memoryAuthDAO.getAuth(authKey);
            return new ListGamesResponse(memoryGameDAO.listGames());
        } catch (UnauthorizedException e) {
            throw new ServiceException(401, e.getMessage());
        }

    }

    public ClearResponse clear() {
        memoryGameDAO.clearAllGames();
        return new ClearResponse();
    }

}


