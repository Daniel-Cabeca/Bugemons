package ulb.repository.database;

import ulb.repository.AccountRepository;
import ulb.exceptions.LoadException;
import ulb.repository.database.sql.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	 * {@inheritDoc}
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
	 * {@inheritDoc}
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
	 * {@inheritDoc}
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

	/**
	 * {@inheritDoc}
	 */
	public List<String> getPendingFriendRequests(int receiverId) throws LoadException {
		List<String> senders = new ArrayList<>();
		String sql = "SELECT u.username FROM users u JOIN friend_requests r ON u.id = r.sender_id WHERE r.receiver_id = ?";
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
		String sql = "SELECT u.username FROM users u JOIN battle_requests r ON u.id = r.sender_id WHERE r.receiver_id = ?";
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


}
