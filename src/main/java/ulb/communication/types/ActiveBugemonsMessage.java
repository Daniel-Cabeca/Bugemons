package ulb.communication.types;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.communication.Message;
import ulb.controller.GameController;

public class ActiveBugemonsMessage implements Message {
    private BugemonDTO selfActiveBugemon;
    private BugemonDTO opponentActiveBugemon;

    public ActiveBugemonsMessage(BugemonDTO selfActiveBugemon, BugemonDTO opponentActiveBugemon){
        this.selfActiveBugemon = selfActiveBugemon;
        this.opponentActiveBugemon = opponentActiveBugemon;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.ACTIVE_BUGEMONS;
    }

    @Override
    public Message handle(GameController controller) {
        // TODO Auto-generated method stub
        return null;
    }

    public BugemonDTO getSelfActiveBugemon(){return this.selfActiveBugemon;}
    public BugemonDTO getOpponentActiveBugemon(){return this.opponentActiveBugemon;}
}
