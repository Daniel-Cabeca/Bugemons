package ulb.repository.database;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;
import ulb.exceptions.UserAlreadyExistsException;
import ulb.repository.AccountRepository;
import ulb.repository.database.sql.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Database-backed implementation of account operations.
 */
public class AccountDatabaseRepository implements AccountRepository {
	private static final Logger LOGGER = Logger.getLogger(AccountDatabaseRepository.class.getName());

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
	 * {@inheritDoc}
	 */
	public void register(String username, String password) throws LoadException, UserAlreadyExistsException {
		String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
			stmt.setString(1, username);
			stmt.setString(2, password);
			stmt.executeUpdate();
		} catch (SQLException e) {
			if (e.getMessage().contains("UNIQUE")) {
				throw new UserAlreadyExistsException(username);
			} else {
				throw new LoadException("Failed to register user: " + e.getMessage());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String getPasswordHash(String username) throws LoadException, EntityNotFoundException {
		String sql = "SELECT password_hash FROM users WHERE username = ?";
		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) return rs.getString("password_hash");
			throw new EntityNotFoundException("Password", username);
		} catch (SQLException e) {
			throw new LoadException("Failed to fetch user: " + e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getUserId(String username) throws EntityNotFoundException {
		String sql = "SELECT id FROM users WHERE username = ?";
		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) return rs.getInt("id");
			throw new EntityNotFoundException("Password", username);
		} catch (SQLException e) {
			// LOGGER
			throw new EntityNotFoundException("Password", username);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String getUsername(int userId) throws EntityNotFoundException, DataAccessException {
		String sql = "SELECT username FROM  users WHERE id = ?";

		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getString("username");
			}
		} catch (SQLException e) {
			throw new DataAccessException("Failed to parse SQL query");
		}

		throw new EntityNotFoundException("Player", userId);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getFriendsList(int userId) throws LoadException {
		List<String> friends = new ArrayList<>();

		String sql = """
				    SELECT u.username
				    FROM users u
				    JOIN friends f ON u.id = f.friend_id
				    WHERE f.user_id = ?
				""";

		try (PreparedStatement pstmt = this.database.prepareStatement(sql)) {
			pstmt.setInt(1, userId);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				friends.add(rs.getString("username"));
			}
		} catch (SQLException e) {
			throw new LoadException("Failed to fetch friends list: " + e.getMessage());
		}

		return friends;
	}

	/**
	 * {@inheritDoc}
	 */
	public void addFriend(int userId, int friendId) throws LoadException {
		String sql = "INSERT OR IGNORE INTO friends (user_id, friend_id) VALUES (?, ?)";
		try (PreparedStatement pstmt = this.database.prepareStatement(sql)) {
			// On insère les deux sens pour simplifier la lecture plus tard
			pstmt.setInt(1, userId);
			pstmt.setInt(2, friendId);
			pstmt.executeUpdate();

			pstmt.setInt(1, friendId);
			pstmt.setInt(2, userId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new LoadException("Failed to add friend: " + e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void sendFriendRequest(int senderId, int receiverId) throws LoadException {
		String sql = "INSERT OR IGNORE INTO friend_requests (sender_id, receiver_id) VALUES (?, ?)";
		try (PreparedStatement stmt = database.prepareStatement(sql)) {
			stmt.setInt(1, senderId);
			stmt.setInt(2, receiverId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new LoadException("Failed to send friend request: " + e.getMessage());
		}
	}

	public void sendBattleRequest(int senderId, int receiverId) throws LoadException {
		String sql = "INSERT OR IGNORE INTO battle_requests (sender_id, receiver_id) VALUES (?, ?)";
		try (PreparedStatement stmt = database.prepareStatement(sql)) {
			stmt.setInt(1, senderId);
			stmt.setInt(2, receiverId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new LoadException("Failed to send battle request: " + e.getMessage());
		}
	}

	public boolean hasPendingBattleRequestBetween(int userIdA, int userIdB) throws LoadException {
		String sql = """
					SELECT 1
					FROM battle_requests
					WHERE (sender_id = ? AND receiver_id = ?)
					   OR (sender_id = ? AND receiver_id = ?)
					LIMIT 1
				""";
		try (PreparedStatement stmt = database.prepareStatement(sql)) {
			stmt.setInt(1, userIdA);
			stmt.setInt(2, userIdB);
			stmt.setInt(3, userIdB);
			stmt.setInt(4, userIdA);
			ResultSet rs = stmt.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			throw new LoadException("Failed to check pending battle request: " + e.getMessage());
		}
	}

	public List<String> getPendingBattleRequests(int receiverId) throws LoadException {
		List<String> senders = new ArrayList<>();
		String sql = "SELECT u.username FROM users u JOIN battle_requests r ON u.id = r.sender_id WHERE r.receiver_id " +
				"= ?";
		try (PreparedStatement stmt = database.prepareStatement(sql)) {
			stmt.setInt(1, receiverId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) senders.add(rs.getString("username"));
		} catch (SQLException e) {
			throw new LoadException("Failed to fetch requests: " + e.getMessage());
		}
		return senders;
	}

	public void acceptBattleRequest(int senderId, int receiverId) throws LoadException {
		String del = "DELETE FROM battle_requests WHERE sender_id = ? AND receiver_id = ?";
		try (PreparedStatement stmt = database.prepareStatement(del)) {
			stmt.setInt(1, senderId);
			stmt.setInt(2, receiverId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new LoadException("Failed to accept request: " + e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getPendingFriendRequests(int receiverId) throws LoadException {
		List<String> senders = new ArrayList<>();
		String sql = "SELECT u.username FROM users u JOIN friend_requests r ON u.id = r.sender_id WHERE r.receiver_id " +
				"= ?";
		try (PreparedStatement stmt = database.prepareStatement(sql)) {
			stmt.setInt(1, receiverId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) senders.add(rs.getString("username"));
		} catch (SQLException e) {
			throw new LoadException("Failed to fetch requests: " + e.getMessage());
		}
		return senders;
	}

	/**
	 * {@inheritDoc}
	 */
	public void acceptFriendRequest(int senderId, int receiverId) throws LoadException {
		String del = "DELETE FROM friend_requests WHERE sender_id = ? AND receiver_id = ?";
		try (PreparedStatement stmt = database.prepareStatement(del)) {
			stmt.setInt(1, senderId);
			stmt.setInt(2, receiverId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new LoadException("Failed to accept request: " + e.getMessage());
		}
		addFriend(senderId, receiverId);
	}

	/**
	 * {@inheritDoc}
	 */
	public void declineFriendRequest(int senderId, int receiverId) throws LoadException {
		String del = "DELETE FROM friend_requests WHERE sender_id = ? AND receiver_id = ?";
		try (PreparedStatement stmt = database.prepareStatement(del)) {
			stmt.setInt(1, senderId);
			stmt.setInt(2, receiverId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new LoadException("Failed to decline request: " + e.getMessage());
		}
	}

	public void declineBattleRequest(int senderId, int receiverId) throws LoadException {
		String del = "DELETE FROM battle_requests WHERE sender_id = ? AND receiver_id = ?";
		try (PreparedStatement stmt = database.prepareStatement(del)) {
			stmt.setInt(1, senderId);
			stmt.setInt(2, receiverId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new LoadException("Failed to decline request: " + e.getMessage());
		}
	}

	public void addPoints(int userId, int pointsToAdd) throws LoadException {
		String sql = "UPDATE users SET points = points + ? WHERE id = ?";

		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
			stmt.setInt(1, pointsToAdd);
			stmt.setInt(2, userId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, "Failed to add leaderboard points for user id: " + userId);
			throw new LoadException("Failed to fetch request: " + e.getMessage());
		}
	}

	public Map<String, Integer> getLeaderboard() throws LoadException {
		Map<String, Integer> leaderboard = new LinkedHashMap<>();
		String sql = "SELECT username, points FROM users ORDER BY points DESC LIMIT 10";

		try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				leaderboard.put(rs.getString("username"), rs.getInt("points"));
			}
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, "Failed to load leaderboard from database.");
			throw new LoadException("Failed to fetch request: " + e.getMessage());
		}
		return leaderboard;
	}

}
