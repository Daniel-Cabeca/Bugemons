package ulb.message.serverToClient;

import java.io.Serializable;
import java.util.List;

public class BattleRequestsMessage implements Serializable {
    private final List<String> requests;

    public BattleRequestsMessage(List<String> requests) { this.requests = requests; }

    public List<String> getRequests() { return requests; }
}

