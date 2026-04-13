package ulb.model.chat;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ChatMessage implements Serializable {
    private final long id;
    private final String senderUsername;
    private final String receiverUsername;
    private final String content;
    private final LocalDateTime sentAt;

    public ChatMessage(long id, String senderUsername, String receiverUsername, String content, LocalDateTime sentAt) {
        this.id = id;
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.content = content;
        this.sentAt = sentAt;
    }

    public ChatMessage(String senderUsername, String receiverUsername, String content, LocalDateTime sentAt) {
        this(-1, senderUsername, receiverUsername, content, sentAt);
    }

    public long getId() { return this.id; }
    public String getSenderUsername() { return this.senderUsername; }
    public String getReceiverUsername() { return this.receiverUsername; }
    public String getContent() { return this.content; }
    public LocalDateTime getSentAt() { return this.sentAt; }

    @Override
    public String toString() {
        return "[" + this.sentAt + "] " + this.senderUsername + " → " + this.receiverUsername + ": " + this.content;
    }

    public String format(String me) {
        String prefix = getSenderUsername().equals(me) ? "Vous" : getSenderUsername();
        return prefix + ": " + getContent();
    }
}