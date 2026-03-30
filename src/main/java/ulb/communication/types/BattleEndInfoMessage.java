package ulb.communication.types;

import ulb.communication.Message;

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

    @Override
    public MessageType getMessageType() {
        return MessageType.BATTLE_END_INFO;
    }
}
