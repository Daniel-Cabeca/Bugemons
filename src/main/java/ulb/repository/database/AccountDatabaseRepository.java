package ulb.repository.database;

import ulb.repository.LoadException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDatabaseRepository {
    private final Connection connection;

    public AccountDatabaseRepository(Connection connection) {
        this.connection = connection;
    }

    // Returns false if the username is already taken (UNIQUE constraint)
    public boolean register(String username, String password) throws LoadException {
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) return false;
            throw new LoadException("Failed to register user: " + e.getMessage());
        }
    }

    public String getPasswordHash(String username) throws LoadException {
        String sql = "SELECT password_hash FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("password_hash");
            return null;
        } catch (SQLException e) {
            throw new LoadException("Failed to fetch user: " + e.getMessage());
        }
    }

    public int getUserId(String username) throws LoadException {
        String sql = "SELECT id FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
            return -1;
        } catch (SQLException e) {
            throw new LoadException("Failed to fetch user id: " + e.getMessage());
        }
    }
}
