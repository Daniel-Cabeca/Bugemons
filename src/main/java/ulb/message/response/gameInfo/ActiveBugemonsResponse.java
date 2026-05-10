package ulb.message.response.gameInfo;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.message.response.Response;

public class ActiveBugemonsResponse extends Response {
    private final BugemonDTO selfActiveBugemon;
    private final BugemonDTO opponentActiveBugemon;

    public ActiveBugemonsResponse(BugemonDTO selfActiveBugemon, BugemonDTO opponentActiveBugemon){
        this.selfActiveBugemon = selfActiveBugemon;
        this.opponentActiveBugemon = opponentActiveBugemon;
    }

    public BugemonDTO getSelfActiveBugemon(){return this.selfActiveBugemon;}
    public BugemonDTO getOpponentActiveBugemon(){return this.opponentActiveBugemon;}
}
