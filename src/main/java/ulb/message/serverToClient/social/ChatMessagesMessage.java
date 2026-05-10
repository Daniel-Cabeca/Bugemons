package ulb.message.serverToClient.social;

import ulb.model.chat.ChatMessage;

import java.io.Serializable;
import java.util.List;

public class ChatMessagesMessage implements Serializable {
    private final List<ChatMessage> messages;

    public ChatMessagesMessage(List<ChatMessage> messages) { this.messages = messages; }

    public List<ChatMessage> getMessages() { return messages; }
}
