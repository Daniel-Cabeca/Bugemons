package ulb.repository.database;

import ulb.repository.AccountRepository;
import ulb.repository.LoadException;
import ulb.repository.database.sql.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDatabaseRepository implements AccountRepository {
	private final Database database;

	public AccountDatabaseRepository(Database database) {
		this.database = database;
	}

	// Returns false if the username is already taken (UNIQUE constraint)
	public boolean register(String username, String password) throws LoadException {
		String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
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
		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
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
        try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
            return -1;
        } catch (SQLException e) {
            throw new LoadException(e.getMessage());
        }
    }

    public boolean isFirstLogin(String username) throws LoadException {
        String sql = "SELECT first_login FROM users WHERE username = ?";
        try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("first_login") == 1;
            return false;
        } catch (SQLException e) {
            throw new LoadException(e.getMessage());
        }
    }

    public String[] getPlayerProfile(String username) throws LoadException {
        String sql = "SELECT player_name, gender FROM users WHERE username = ?";
        try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return new String[]{rs.getString("player_name"), rs.getString("gender")};
            return new String[]{null, null};
        } catch (SQLException e) {
            throw new LoadException(e.getMessage());
        }
    }

    public void savePlayerProfile(String username, String playerName, String gender) throws LoadException {
        String sql = "UPDATE users SET first_login = 0, player_name = ?, gender = ? WHERE username = ?";
        try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
            stmt.setString(1, playerName);
            stmt.setString(2, gender);
            stmt.setString(3, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new LoadException(e.getMessage());
        }
    }
}
