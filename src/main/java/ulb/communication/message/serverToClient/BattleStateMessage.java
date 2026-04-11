package ulb.communication.message.serverToClient;

import ulb.model.battle.BattleState;
import java.io.Serializable;

public class BattleStateMessage implements Serializable {
    private BattleState battleState;

    public BattleStateMessage(BattleState battleState){
        this.battleState = battleState;
    }

    public BattleState getBattleState(){return this.battleState;}
}
