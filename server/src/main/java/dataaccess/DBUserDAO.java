package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.SQLIntegrityConstraintViolationException;

public class DBUserDAO implements UserDAO {

    @Override
    public void createUser(UserData userData) throws AlreadyTakenException {
        String sql = "INSERT INTO userdata (username, password, email) VALUES (?, ?, ?)";
        //hash the password
        try (var conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userData.username());
            pstmt.setString(2, BCrypt.hashpw(userData.password(), BCrypt.gensalt()));
            pstmt.setString(3, userData.email());
            pstmt.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new AlreadyTakenException();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("""
                      SELECT * FROM userdata
                      WHERE username=(?);
                     """)) {
            pstmt.setString(1, username);
            var resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                return new UserData(username, password, email);
            } else {
                throw new DataAccessException("User not found");
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public UserData verifyUser(String username, String password) throws DataAccessException {
        UserData userData = getUser(username);
        return (BCrypt.checkpw(password, userData.password())) ? userData : null;

    }

    @Override
    public void clearAllUsers() {
        try (var conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("""
                      DELETE FROM userdata;
                     """)) {
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
