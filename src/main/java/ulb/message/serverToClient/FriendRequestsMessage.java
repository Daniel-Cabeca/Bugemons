package ulb.message.serverToClient;

import java.io.Serializable;
import java.util.List;

public class FriendRequestsMessage implements Serializable {
    private final List<String> requests;

    public FriendRequestsMessage(List<String> requests) { this.requests = requests; }

    public List<String> getRequests() { return requests; }
}
