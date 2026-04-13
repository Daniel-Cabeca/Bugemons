package ulb.repository.database;

import ulb.repository.AccountRepository;
import ulb.repository.LoadException;
import ulb.repository.database.sql.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database-backed implementation of account operations.
 */
public class AccountDatabaseRepository implements AccountRepository {
	private final Database database;

	/**
	 * Creates an account repository using the provided database.
	 *
	 * @param database The database connection wrapper
	 */
	public AccountDatabaseRepository(Database database) {
		this.database = database;
	}

	/**
	 * Registers a new user account.
	 *
	 * @param username The account username
	 * @param password The password hash to store
	 * @return False if the username is already taken, true otherwise
	 * @throws LoadException If the user cannot be persisted
	 */
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

	/**
	 * Returns the password hash associated with a username.
	 *
	 * @param username The account username
	 * @return The stored password hash, or null if not found
	 * @throws LoadException If the lookup fails
	 */
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

	/**
	 * Returns the internal user identifier associated with a username.
	 *
	 * @param username The account username
	 * @return The user id, or -1 if not found
	 * @throws LoadException If the lookup fails
	 */
	public int getUserId(String username) throws LoadException {
		String sql = "SELECT id FROM users WHERE username = ?";
		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) return rs.getInt("id");
			return -1;
		} catch (SQLException e) {
			throw new LoadException("Failed to fetch user id: " + e.getMessage());
		}
	}
}
