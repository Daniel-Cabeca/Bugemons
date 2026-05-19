package ulb.message.response.gameInfo;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.reward.RewardDTO;
import ulb.message.response.Response;

import java.util.List;

public class LevelUpInfoResponse extends Response {
	private final BugemonDTO bugemon;
	private final List<RewardDTO> rewards;

	public LevelUpInfoResponse(BugemonDTO bugemon, List<RewardDTO> rewards) {
		this.bugemon = bugemon;
		this.rewards = rewards;
	}

	public BugemonDTO getBugemon() {
		return bugemon;
	}

	public List<RewardDTO> getRewards() {
		return rewards;
	}
}
