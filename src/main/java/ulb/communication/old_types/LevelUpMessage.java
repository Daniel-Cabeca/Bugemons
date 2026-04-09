package ulb.communication.old_types;

import javafx.event.ActionEvent;
import ulb.communication.Message;
import ulb.communication.MessageType;
import ulb.controller.GameController;
import ulb.model.bugemon.Bugemon;
import ulb.model.reward.Reward;

import java.util.Vector;

public class LevelUpMessage implements Message {

    private Bugemon bugemon;
    private Vector<Reward> rewards;
    private Reward reward;
    private ActionEvent event;

    public LevelUpMessage(Bugemon bugemon, Vector<Reward> rewards) {
        this.bugemon = bugemon;
        this.rewards = rewards;
    }

    public LevelUpMessage(Reward reward, ActionEvent event) {
        this.reward = reward;
        this.event = event;
    }

    public Bugemon getBugemon() { return bugemon; }

    public Vector<Reward> getRewards() { return rewards; }

    public Reward getReward() { return reward; }

    public ActionEvent getEvent() { return event; }

    @Override
    public MessageType getMessageType() {
        return MessageType.LEVEL_UP;
    }

    @Override
    public Message handle(GameController controller) {
        return controller.applyOn(this);
    }
}
