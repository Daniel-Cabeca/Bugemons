package ulb.repository.database;

import ulb.repository.LoadException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FriendDatabaseRepository {
    private final Connection connection;

    public FriendDatabaseRepository(Connection connection) {
        this.connection = connection;
    }

    public void addFriend(int userId, int friendId) throws LoadException {
        String sql = "INSERT OR IGNORE INTO friends (user_id, friend_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, friendId);
            stmt.executeUpdate();
            stmt.setInt(1, friendId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new LoadException(e.getMessage());
        }
    }

    public List<String> getFriends(int userId) throws LoadException {
        String sql = "SELECT u.username FROM friends f JOIN users u ON f.friend_id = u.id WHERE f.user_id = ?";
        List<String> friends = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                friends.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            throw new LoadException(e.getMessage());
        }
        return friends;
    }
}
