package ulb.message.serverToClient.gameInfo;

import java.io.Serializable;
import java.util.List;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.reward.RewardDTO;

public class LevelUpInfoMessage implements Serializable {
    private final BugemonDTO bugemon;
    private final List<RewardDTO> rewards;

    public LevelUpInfoMessage(BugemonDTO bugemon, List<RewardDTO> rewards) {
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
