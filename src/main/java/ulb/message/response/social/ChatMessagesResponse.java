package ulb.message.response.social;

import ulb.message.response.Response;
import ulb.model.chat.ChatMessage;

import java.util.List;

public class ChatMessagesResponse extends Response {
    private final List<ChatMessage> messages;

    public ChatMessagesResponse(List<ChatMessage> messages) { this.messages = messages; }

    public List<ChatMessage> getMessages() { return messages; }
}
