package dataaccess;

public record ServerDaos(AuthDAO authDao, UserDAO userDAO, GameDAO gameDao) {
}
