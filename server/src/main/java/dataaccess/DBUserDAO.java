package dataaccess;

import model.UserData;

import java.sql.PreparedStatement;

public class DBUserDAO implements UserDAO {
    //    if (!allUsers.contains(userData)) {
    //            allUsers.add(userData);
    //        } else {
    //            throw new AlreadyTakenException();
    //        }
    @Override
    public void createUser(UserData userData) throws AlreadyTakenException {
        String sql = "INSERT INTO userdata (username, password, email) VALUES (?, ?, ?)";

        try (var conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userData.username());
            pstmt.setString(2, userData.password());
            pstmt.setString(3, userData.email());
            pstmt.executeUpdate();

        } catch (Exception e) {
            //for now just catch the error
            //later we're going to throw it as an AlreadyTakenException.
            System.err.println("Error: " + e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public UserData verifyUser(String username, String password) throws DataAccessException {
        return null;
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
