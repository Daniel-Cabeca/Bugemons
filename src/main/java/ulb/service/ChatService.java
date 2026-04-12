package ulb.service;

import ulb.model.chat.ChatMessage;
import ulb.repository.json.ChatDatabaseRepository;

import java.time.LocalDateTime;
import java.util.List;

public class ChatService {
    private final ChatDatabaseRepository repository;

    public ChatService(ChatDatabaseRepository repository) {
        this.repository = repository;
    }

    public void sendMessage(String sender, String receiver, String content) {
        repository.insert(new ChatMessage(sender, receiver, content, LocalDateTime.now()));
    }

    public List<ChatMessage> getMessages(String userA, String userB) {
        return repository.getMessages(userA, userB);
    }
}
