package ulb.communication.types;

import ulb.communication.Message;
import ulb.communication.MessageType;
import ulb.controller.GameController;
import ulb.model.battle.BattleState;

public class BattleStateMessage implements Message{
    private BattleState battleState;

    public BattleStateMessage(BattleState battleState){
        this.battleState = battleState;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.BATTLE_STATE;
    }

    @Override
    public Message handle(GameController controller) {
        // TODO Auto-generated method stub
        return null;
    }

    public BattleState getBattleState(){return this.battleState;}
}
