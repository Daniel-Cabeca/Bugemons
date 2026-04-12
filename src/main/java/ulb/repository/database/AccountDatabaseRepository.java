package ulb.repository.database;

import ulb.repository.AccountRepository;
import ulb.repository.LoadException;
import ulb.repository.database.sql.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
			throw new LoadException("Failed to fetch user id: " + e.getMessage());
		}
	}

	public void addFriend(int userId, int friendId) {
		String sql = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
		try (PreparedStatement pstmt = this.database.prepareStatement(sql)) {
			// On insère les deux sens pour simplifier la lecture plus tard
			pstmt.setInt(1, userId);
			pstmt.setInt(2, friendId);
			pstmt.executeUpdate();

			pstmt.setInt(1, friendId);
			pstmt.setInt(2, userId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<String> getFriendsList(int userId) {
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
			System.err.println("Erreur lors de la récupération des amis : " + e.getMessage());
		}

		return friends;
	}

	private boolean areFriends(int userId, int friendId) throws LoadException {
		String sql = "SELECT 1 FROM friends WHERE user_id = ? AND friend_id = ?";
		try (PreparedStatement pstmt = this.database.prepareStatement(sql)) {
			pstmt.setInt(1, userId);
			pstmt.setInt(2, friendId);
			ResultSet rs = pstmt.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			throw new LoadException("Failed to check friendship: " + e.getMessage());
		}
	}

	private void deleteFriendRequestPair(int userA, int userB) throws LoadException {
		String sql = """
			DELETE FROM friend_requests
			WHERE (requester_id = ? AND addressee_id = ?)
			   OR (requester_id = ? AND addressee_id = ?)
			""";
		try (PreparedStatement pstmt = this.database.prepareStatement(sql)) {
			pstmt.setInt(1, userA);
			pstmt.setInt(2, userB);
			pstmt.setInt(3, userB);
			pstmt.setInt(4, userA);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new LoadException("Failed to remove friend request rows: " + e.getMessage());
		}
	}

	@Override
	public void sendFriendRequest(int requesterId, int addresseeId) throws LoadException {
		if (requesterId == addresseeId || requesterId <= 0 || addresseeId <= 0) {
			return;
		}
		if (areFriends(requesterId, addresseeId)) {
			return;
		}
		String sql = "INSERT INTO friend_requests (requester_id, addressee_id) VALUES (?, ?)";
		try (PreparedStatement pstmt = this.database.prepareStatement(sql)) {
			pstmt.setInt(1, requesterId);
			pstmt.setInt(2, addresseeId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			if (e.getMessage() != null && e.getMessage().contains("UNIQUE")) {
				return;
			}
			throw new LoadException("Failed to send friend request: " + e.getMessage());
		}
	}

	@Override
	public List<PendingFriendRequest> getPendingFriendRequestsForAddressee(int addresseeUserId) throws LoadException {
		List<PendingFriendRequest> list = new ArrayList<>();
		String sql = """
			SELECT fr.requester_id, u.username
			FROM friend_requests fr
			JOIN users u ON u.id = fr.requester_id
			WHERE fr.addressee_id = ?
			ORDER BY u.username
			""";
		try (PreparedStatement pstmt = this.database.prepareStatement(sql)) {
			pstmt.setInt(1, addresseeUserId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(new PendingFriendRequest(rs.getInt("requester_id"), rs.getString("username")));
			}
		} catch (SQLException e) {
			throw new LoadException("Failed to fetch friend requests: " + e.getMessage());
		}
		return list;
	}

	@Override
	public void acceptFriendRequest(int addresseeId, int requesterId) throws LoadException {
		String check = "SELECT 1 FROM friend_requests WHERE requester_id = ? AND addressee_id = ?";
		try (PreparedStatement pstmt = this.database.prepareStatement(check)) {
			pstmt.setInt(1, requesterId);
			pstmt.setInt(2, addresseeId);
			ResultSet rs = pstmt.executeQuery();
			if (!rs.next()) {
				return;
			}
		} catch (SQLException e) {
			throw new LoadException("Failed to accept friend request: " + e.getMessage());
		}
		addFriend(requesterId, addresseeId);
		deleteFriendRequestPair(requesterId, addresseeId);
	}

	@Override
	public void declineFriendRequest(int addresseeId, int requesterId) throws LoadException {
		String sql = "DELETE FROM friend_requests WHERE requester_id = ? AND addressee_id = ?";
		try (PreparedStatement pstmt = this.database.prepareStatement(sql)) {
			pstmt.setInt(1, requesterId);
			pstmt.setInt(2, addresseeId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new LoadException("Failed to decline friend request: " + e.getMessage());
		}
	}

}
