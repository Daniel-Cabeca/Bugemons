package ulb.repository;

import ulb.exceptions.LoadException;
import ulb.model.chat.ChatMessage;

import java.util.List;

/**
 * Repository holding chat messages.
 */
public interface ChatRepository {
	/**
	 * Adds a message in the story.
	 *
	 * @param message The message to add
	 * @throws LoadException If the message could not be added
	 */
	void insert(ChatMessage message) throws LoadException;

	/**
	 * Returns the last messages exchanged between two users,
	 * ordered from oldest to newest.
	 *
	 * @param usernameA One participant of the conversation
	 * @param usernameB The other participant
	 * @return All messages
	 * @throws LoadException If the conversation could not be loaded
	 */
	List<ChatMessage> getMessages(String usernameA, String usernameB) throws LoadException;

}
