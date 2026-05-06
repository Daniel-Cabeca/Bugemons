package ulb.message.clientToServer;

import ulb.message.ClientToServerMessage;
import ulb.model.battle.BattleState;
import ulb.server.ServerMessageHandler;

public class AddBattleEndResuts implements ClientToServerMessage {
    private BattleState battleState;

    public AddBattleEndResuts(BattleState battleState) {
        this.battleState = battleState;
    }

    public BattleState getBattleState() {
        return this.battleState;
    }
    
    @Override
    public void dispatch(ServerMessageHandler handler) { handler.handle(this); }
}
