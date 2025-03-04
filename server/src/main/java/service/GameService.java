package service;


import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import service.requests.*;
import service.responses.*;

public class GameService {
    ServerDaos serverDaos;
    MemoryAuthDAO memoryAuthDao;
    MemoryGameDAO memoryGameDao;
    static Integer gameId = 1;

    public GameService(ServerDaos serverDaos) {
        this.serverDaos = serverDaos;
        this.memoryAuthDao = serverDaos.memoryAuthDAO();
        this.memoryGameDao = serverDaos.memoryGameDAO();
    }

    private Integer createGameID() {
        return gameId++;
    }

    public CreateGameResponse createGame(CreateGameRequest request) throws ServiceException {
        if (request.gameName() == null) {
            throw new ServiceException(400, "Error: bad request");
        }
        try {
            memoryAuthDao.getAuth(request.authToken());
        } catch (UnauthorizedException e) {
            throw new ServiceException(401, e.getMessage());
        }
        try {
            Integer gameID = createGameID();
            GameData game = new GameData(gameID, null, null, request.gameName(), new ChessGame());
            memoryGameDao.createGame(game);
            return new CreateGameResponse(gameID);
        } catch (DataAccessException e) {
            throw new ServiceException(400, "Error: bad request");
        }
    }

    public JoinGameResponse joinGame(JoinGameRequest joinGameRequest) throws ServiceException {
        if (joinGameRequest.authToken() == null || joinGameRequest.playerColor() == null
                || (!joinGameRequest.playerColor().equals("WHITE") && !joinGameRequest.playerColor().equals("BLACK"))
                || joinGameRequest.gameID() == null) {
            throw new ServiceException(400, "Error: bad request");
        }
        try {
            AuthData authData = memoryAuthDao.getAuth(joinGameRequest.authToken());
            memoryGameDao.joinGame(authData.username(), joinGameRequest.playerColor(), joinGameRequest.gameID());
            return new JoinGameResponse();
        } catch (DataAccessException e) {
            throw new ServiceException(400, e.getMessage());
        } catch (AlreadyTakenException e) {
            throw new ServiceException(403, e.getMessage());
        } catch (UnauthorizedException e) {
            throw new ServiceException(401, e.getMessage());
        }

    }

    public ClearResponse clear() {
        memoryGameDao.clearAllGames();
        return new ClearResponse();
    }

    public ListGamesResponse list(String authKey) {
        try {
            memoryAuthDao.getAuth(authKey);
            return new ListGamesResponse(memoryGameDao.listGames());
        } catch (UnauthorizedException e) {
            throw new ServiceException(401, e.getMessage());
        }

    }

}


