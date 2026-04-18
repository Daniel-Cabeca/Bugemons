package ulb.service;

import ulb.model.chat.ChatMessage;
import ulb.repository.ChatRepository;
import ulb.service.chat.InappropriateWordFilter;

import java.time.LocalDateTime;
import java.util.List;

public class ChatService {
    private final ChatRepository repository;
    private final InappropriateWordFilter inappropriateWordFilter;

    public ChatService(ChatRepository repository) {
        this(repository, new InappropriateWordFilter());
    }

    public ChatService(ChatRepository repository, InappropriateWordFilter inappropriateWordFilter) {
        this.repository = repository;
        this.inappropriateWordFilter = inappropriateWordFilter;
    }

    public void sendMessage(String sender, String receiver, String content) {
        String censoredContent = this.inappropriateWordFilter.censor(content);
        repository.insert(new ChatMessage(sender, receiver, censoredContent, LocalDateTime.now()));
    }

    public List<ChatMessage> getMessages(String userA, String userB) {
        return repository.getMessages(userA, userB);
    }
}
