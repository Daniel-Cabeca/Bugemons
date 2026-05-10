package ulb.message.response.social;

import ulb.message.response.Response;

import java.util.List;

public class FriendRequestsResponse extends Response {
    private final List<String> requests;

    public FriendRequestsResponse(List<String> requests) { this.requests = requests; }

    public List<String> getRequests() { return requests; }
}
