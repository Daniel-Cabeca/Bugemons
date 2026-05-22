package ulb.model.chat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Immutable chat message exchanged between two users.
 */
public class ChatMessage implements Serializable {
	private final long id;
	private final String senderUsername;
	private final String receiverUsername;
	private final String content;
	private final LocalDateTime sentAt;

	/**
	 * Creates a new chat message without persistent id.
	 *
	 * @param senderUsername Sender username
	 * @param receiverUsername Receiver username
	 * @param content Message content
	 * @param sentAt Message timestamp
	 */
	public ChatMessage(String senderUsername, String receiverUsername, String content, LocalDateTime sentAt) {
		this(-1, senderUsername, receiverUsername, content, sentAt);
	}

	/**
	 * Creates a persisted chat message.
	 *
	 * @param id Message id
	 * @param senderUsername Sender username
	 * @param receiverUsername Receiver username
	 * @param content Message content
	 * @param sentAt Message timestamp
	 */
	public ChatMessage(long id, String senderUsername, String receiverUsername, String content, LocalDateTime sentAt) {
		this.id = id;
		this.senderUsername = senderUsername;
		this.receiverUsername = receiverUsername;
		this.content = content;
		this.sentAt = sentAt;
	}

	public String getReceiverUsername() { return this.receiverUsername; }

	public LocalDateTime getSentAt() { return this.sentAt; }

	/** 
	 * {@inheritDoc} 
	 */
	@Override
	public String toString() {
		return "[" + this.sentAt + "] " + this.senderUsername + " → " + this.receiverUsername + ": " + this.content;
	}

	/**
	 * Formats the message for local display.
	 *
	 * @param me Current local username
	 * @return Formatted display line
	 */
	public String format(String me) {
		String prefix = getSenderUsername().equals(me) ? "Vous" : getSenderUsername();
		return prefix + ": " + getContent();
	}

	public String getSenderUsername() { return this.senderUsername; }

	public String getContent() { return this.content; }
}