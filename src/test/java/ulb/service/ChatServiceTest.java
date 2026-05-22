package ulb.service;

import org.junit.jupiter.api.Test;
import ulb.exceptions.LoadException;
import ulb.model.chat.ChatMessage;
import ulb.repository.ChatRepository;
import ulb.service.chat.InappropriateWordFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChatServiceTest {
	@Test
	void sendMessageStoresCensoredMessage() throws LoadException {
		InMemoryChatRepository repository = new InMemoryChatRepository();
		ChatService chatService = new ChatService(repository, new InappropriateWordFilter(Set.of("idiot")));

		chatService.sendMessage("alice", "bob", "Tu es idiot");

		assertEquals(1, repository.insertedMessages.size());
		assertEquals("Tu es i***t", repository.insertedMessages.get(0).getContent());
	}

	private static class InMemoryChatRepository implements ChatRepository {
		private final List<ChatMessage> insertedMessages = new ArrayList<>();

		@Override
		public void insert(ChatMessage message) {
			this.insertedMessages.add(message);
		}

		@Override
		public List<ChatMessage> getMessages(String usernameA, String usernameB) {
			return List.of();
		}

	}
}
