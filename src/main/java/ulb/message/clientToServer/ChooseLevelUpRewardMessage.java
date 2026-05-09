package ulb.message.clientToServer;

import ulb.DTO.reward.RewardDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class ChooseLevelUpRewardMessage implements ClientToServerMessage {
    private final RewardDTO reward;

    public ChooseLevelUpRewardMessage(RewardDTO reward) {
        this.reward = reward;
    }

    @Override
    public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
        handler.chooseLevelUpReward(reward);
    }
}
