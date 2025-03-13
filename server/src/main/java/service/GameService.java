package service;


import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import service.requests.*;
import service.responses.*;

public class GameService {
    ServerDaos serverDaos;
    AuthDAO authDao;
    GameDAO gameDao;

    public GameService(ServerDaos serverDaos) {
        this.serverDaos = serverDaos;
        this.authDao = serverDaos.authDao();
        this.gameDao = serverDaos.gameDao();
    }


    public CreateGameResponse createGame(CreateGameRequest request) throws ServiceException {
        if (request.gameName() == null) {
            throw new ServiceException(400, "Error: bad request");
        }
        try {
            authDao.getAuth(request.authToken());
        } catch (UnauthorizedException e) {
            throw new ServiceException(401, e.getMessage());
        }
        try {
            GameData game = new GameData(null, null, null, request.gameName(), new ChessGame());
            GameData createdGame = gameDao.createGame(game);
            return new CreateGameResponse(createdGame.gameID());
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
            AuthData authData = authDao.getAuth(joinGameRequest.authToken());
            gameDao.joinGame(authData.username(), joinGameRequest.playerColor(), joinGameRequest.gameID());
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
        gameDao.clearAllGames();
        return new ClearResponse();
    }

    public ListGamesResponse list(String authKey) {
        try {
            authDao.getAuth(authKey);
            return new ListGamesResponse(gameDao.listGames());
        } catch (UnauthorizedException e) {
            throw new ServiceException(401, e.getMessage());
        }

    }

}


