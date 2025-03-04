package dataaccess;

public record ServerDaos(MemoryAuthDAO memoryAuthDAO, MemoryUserDAO memoryUserDAO, MemoryGameDAO memoryGameDAO) {
}
