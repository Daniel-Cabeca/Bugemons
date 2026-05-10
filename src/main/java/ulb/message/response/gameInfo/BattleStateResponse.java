package ulb.message.response.gameInfo;

import ulb.message.response.Response;
import ulb.model.battle.BattleState;

public class BattleStateResponse extends Response {
    private final BattleState battleState;

    public BattleStateResponse(BattleState battleState){
        this.battleState = battleState;
    }

    public BattleState getBattleState(){return this.battleState;}
}
