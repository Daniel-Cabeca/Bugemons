package ulb.message.clientToServer;

import ulb.DTO.reward.RewardDTO;
import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class ChooseLevelUpRewardMessage implements ClientToServerMessage {
    private final RewardDTO reward;

    public ChooseLevelUpRewardMessage(RewardDTO reward) {
        this.reward = reward;
    }

    public RewardDTO getReward() {
        return reward;
    }

    @Override
    public void dispatch(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
