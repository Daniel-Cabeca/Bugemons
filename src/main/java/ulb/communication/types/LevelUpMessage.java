package ulb.communication.types;

import ulb.communication.Message;
import ulb.model.bugemon.Bugemon;
import ulb.model.reward.Reward;

import java.util.Vector;

public class LevelUpMessage implements Message {

    private Bugemon bugemon;
    private Vector<Reward> rewards;

    public LevelUpMessage(Bugemon bugemon, Vector<Reward> rewards) {
        this.bugemon = bugemon;
        this.rewards = rewards;
    }

    public Bugemon getBugemon() { return bugemon; }

    public Vector<Reward> getRewards() { return rewards; }

    @Override
    public MessageType getMessageType() {
        return MessageType.LEVEL_UP;
    }
}
