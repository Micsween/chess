package dataaccess;

import model.AuthData;

import java.sql.PreparedStatement;

/*
String sql = "INSERT INTO users (name, age, email) VALUES (?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setString(3, email);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
 */
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
        //thats it
    }

    public AuthData getAuth(String authToken) throws UnauthorizedException {
        return null;
    }

    public void deleteAuth(String authToken) throws UnauthorizedException {

    }

    public void clearAllAuth() {
    }
}
