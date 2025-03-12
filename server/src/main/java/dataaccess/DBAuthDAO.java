package dataaccess;

import model.AuthData;

import java.sql.PreparedStatement;

public class DBAuthDAO implements AuthDAO {

    public void createAuth(AuthData authData) {
        String sql = "INSERT INTO authdata (authToken, username) VALUES (?, ?)";

        try (var conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, authData.authToken());
            pstmt.setString(2, authData.username());
            pstmt.executeUpdate();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public AuthData getAuth(String authToken) throws UnauthorizedException {
        try (var conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("""
                      SELECT * FROM authdata
                      WHERE authToken=(?);
                     """)) {
            pstmt.setString(1, authToken);
            var resultSet = pstmt.executeQuery();
            resultSet.next();
            String username = resultSet.getString("username");
            return new AuthData(authToken, username);
        } catch (Exception e) {
            throw new UnauthorizedException();
        }
    }

    public void deleteAuth(String authToken) throws UnauthorizedException {
        try (var conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("""
                      DELETE FROM authdata
                         WHERE authToken=(?);
                     """)) {
            getAuth(authToken); //throws an error if the authToken does not exist.
            pstmt.setString(1, authToken);
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new UnauthorizedException();
        }
    }

    public void clearAllAuth() {
        try (var conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("TRUNCATE TABLE testing.authdata")) {
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
