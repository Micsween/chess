package service.responses;

public record RegisterResult(
        String username,
        int authToken) {
}
