package ulb.message.response.social;

import ulb.message.response.Response;

import java.util.List;

public class BattleRequestsResponse extends Response {
	private final List<String> requests;

	public BattleRequestsResponse(List<String> requests) { this.requests = requests; }

	public List<String> getRequests() { return requests; }
}

