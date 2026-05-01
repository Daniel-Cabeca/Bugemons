package ulb.message.serverToClient;

import java.io.Serializable;
import java.util.Map;

public class LeaderboardMessage implements Serializable {
	private final Map<String, Integer> leaderboard;

	public LeaderboardMessage( Map<String, Integer> leaderboard) { this.leaderboard = leaderboard; }

	public  Map<String, Integer> getLeaderboard() { return leaderboard; }
}

