package ulb.communication.message.serverToClient;

import ulb.DTO.bugemon.BugemonDTO;
import java.io.Serializable;

public class ActiveBugemonsMessage implements Serializable {
    private BugemonDTO selfActiveBugemon;
    private BugemonDTO opponentActiveBugemon;

    public ActiveBugemonsMessage(BugemonDTO selfActiveBugemon, BugemonDTO opponentActiveBugemon){
        this.selfActiveBugemon = selfActiveBugemon;
        this.opponentActiveBugemon = opponentActiveBugemon;
    }

    public BugemonDTO getSelfActiveBugemon(){return this.selfActiveBugemon;}
    public BugemonDTO getOpponentActiveBugemon(){return this.opponentActiveBugemon;}
}
