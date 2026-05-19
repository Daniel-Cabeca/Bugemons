package ulb.message.request.gameActions;

import ulb.DTO.reward.RewardDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;
import ulb.server.ServerMessageHandler;

public class ChooseLevelUpRewardRequest implements Request {
	private final RewardDTO reward;

	public ChooseLevelUpRewardRequest(RewardDTO reward) {
		this.reward = reward;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.chooseLevelUpReward(reward);
	}
}
