package ulb.message.serverToClient.gameInfo;

import ulb.model.battle.BattleState;
import java.io.Serializable;

public class BattleStateMessage implements Serializable {
    private final BattleState battleState;

    public BattleStateMessage(BattleState battleState){
        this.battleState = battleState;
    }

    public BattleState getBattleState(){return this.battleState;}
}
