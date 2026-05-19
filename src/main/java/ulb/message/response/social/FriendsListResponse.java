package ulb.message.response.social;

import ulb.message.response.Response;

import java.util.List;

public class FriendsListResponse extends Response {
	private final List<String> friends;

	public FriendsListResponse(List<String> friends) { this.friends = friends; }

	public List<String> getFriends() { return friends; }
}
