package ulb.communication.types;

import ulb.communication.Message;
import ulb.controller.GameController;

// Carries the battle result info needed by BattleEndWindow
public class BattleEndInfoMessage implements Message {
    private final boolean victory;
    private final int totalXP;

    public BattleEndInfoMessage(boolean victory, int totalXP) {
        this.victory = victory;
        this.totalXP = totalXP;
    }

    public boolean isVictory() { return victory; }
    public int getTotalXP() { return totalXP; }

    
    public MessageType getMessageType() {
        return MessageType.BATTLE_END_INFO;
    }

    @Override
    public Message handle(GameController controller) {
        return controller.applyOn(this);
    }
    
}
