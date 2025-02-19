package service.requests;

public record CreateGameRequest(
        int authToken,
        String gameName) {
}
