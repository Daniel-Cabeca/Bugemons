package ulb.message.response.social;

import ulb.message.response.Response;

import java.util.Map;

public class LeaderboardResponse extends Response {
	private final Map<String, Integer> leaderboard;

	public LeaderboardResponse(Map<String, Integer> leaderboard) { this.leaderboard = leaderboard; }

	public  Map<String, Integer> getLeaderboard() { return leaderboard; }
}

