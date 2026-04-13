package ulb.repository.database;

import ulb.model.chat.ChatMessage;
import ulb.repository.ChatRepository;
import ulb.repository.LoadException;
import ulb.repository.database.sql.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatDatabaseRepository implements ChatRepository {
    public static final int MAX_MESSAGES = 20;

    private final Database database;

    public ChatDatabaseRepository(Database database) {
        this.database = database;
    }

	@Override
    public void insert(ChatMessage message) throws LoadException {
        String sql = "INSERT INTO chat_messages (sender_username, receiver_username, content, sent_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
            stmt.setString(1, message.getSenderUsername());
            stmt.setString(2, message.getReceiverUsername());
            stmt.setString(3, message.getContent());
            stmt.setString(4, message.getSentAt().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new LoadException(e.getMessage());
        }
        this.pruneMessages(message.getSenderUsername(), message.getReceiverUsername());
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public List<ChatMessage> getMessages(String usernameA, String usernameB) throws LoadException {
        String sql = """
                SELECT id, sender_username, receiver_username, content, sent_at
                FROM chat_messages
                WHERE (sender_username = ? AND receiver_username = ?)
                   OR (sender_username = ? AND receiver_username = ?)
                ORDER BY sent_at DESC, id DESC
                LIMIT ?
                """;
        try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
            this.setMessagesParameters(stmt, usernameA, usernameB, 1);
            stmt.setInt(5, MAX_MESSAGES);
            ResultSet rs = stmt.executeQuery();
            List<ChatMessage> messages = new ArrayList<>();
            while (rs.next()) {
                messages.add(this.mapRow(rs));
            }
            Collections.reverse(messages);
            return messages;
        } catch (SQLException e) {
            throw new LoadException("Failed to load conversation: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public int countMessages(String usernameA, String usernameB) throws LoadException {
        String sql = """
                SELECT COUNT(*) FROM chat_messages
                WHERE (sender_username = ? AND receiver_username = ?)
                   OR (sender_username = ? AND receiver_username = ?)
                """;
        try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
            this.setMessagesParameters(stmt, usernameA, usernameB, 1);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
            return 0;
        } catch (SQLException e) {
            throw new LoadException(e.getMessage());
        }
    }

    private void pruneMessages(String usernameA, String usernameB) throws LoadException {
        String sql = """
                DELETE FROM chat_messages
                WHERE id NOT IN (
                    SELECT id FROM chat_messages
                    WHERE (sender_username = ? AND receiver_username = ?)
                       OR (sender_username = ? AND receiver_username = ?)
                    ORDER BY sent_at DESC, id DESC
                    LIMIT ?
                )
                AND (
                    (sender_username = ? AND receiver_username = ?)
                 OR (sender_username = ? AND receiver_username = ?)
                )
                """;
        try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
            this.setMessagesParameters(stmt, usernameA, usernameB, 1);
            stmt.setInt(5, MAX_MESSAGES);
            this.setMessagesParameters(stmt, usernameA, usernameB, 6);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new LoadException(e.getMessage());
        }
    }

    private void setMessagesParameters(PreparedStatement stmt, String usernameA, String usernameB, int startIndex) throws SQLException {
        stmt.setString(startIndex, usernameA);
        stmt.setString(startIndex + 1, usernameB);
        stmt.setString(startIndex + 2, usernameB);
        stmt.setString(startIndex + 3, usernameA);
    }

    private ChatMessage mapRow(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        String senderUsername = resultSet.getString("sender_username");
        String receiverUsername = resultSet.getString("receiver_username");
        String content = resultSet.getString("content");
        LocalDateTime sentAt = LocalDateTime.parse(resultSet.getString("sent_at"));
        return new ChatMessage(id, senderUsername, receiverUsername, content, sentAt);
    }
}