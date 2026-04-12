package ulb.message.serverToClient;

import java.io.Serializable;
import java.util.List;

public class FriendsListMessage implements Serializable {
    private final List<String> friends;

    public FriendsListMessage(List<String> friends) { this.friends = friends; }

    public List<String> getFriends() { return friends; }
}
