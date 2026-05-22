package ulb.service;

import ulb.exceptions.LoadException;

import ulb.model.chat.ChatMessage;
import ulb.repository.ChatRepository;
import ulb.service.chat.InappropriateWordFilter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer for chat messaging between users.
 */
public class ChatService {
	/** Repository holding chat messages. */
	private final ChatRepository repository;
	/** Filter for inappropriate words. */
	private final InappropriateWordFilter inappropriateWordFilter;

	public ChatService(ChatRepository repository) {
		this(repository, new InappropriateWordFilter());
	}

	public ChatService(ChatRepository repository, InappropriateWordFilter inappropriateWordFilter) {
		this.repository = repository;
		this.inappropriateWordFilter = inappropriateWordFilter;
	}

	/**
	 * Creates a chat message.
	 *
	 * @param sender The username of the sender
	 * @param receiver The username of the receiver
	 * @param content The contents of the message
	 * @throws LoadException If the operation fails
	 */
	public void sendMessage(String sender, String receiver, String content) throws LoadException {
		String censoredContent = this.inappropriateWordFilter.censor(content);
		repository.insert(new ChatMessage(sender, receiver, censoredContent, LocalDateTime.now()));
	}

	/**
	 * Fetches the list of messages between two users.
	 *
	 * @param userA The username of one of the users
	 * @param userB The username of the other user
	 * @return The list of chat messages between the two users
	 * @throws LoadException If the operation fails
	 */
	public List<ChatMessage> getMessages(String userA, String userB) throws LoadException {
		return repository.getMessages(userA, userB);
	}
}
