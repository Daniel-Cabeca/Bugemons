package ulb.message.serverToClient.playerInfo;

import ulb.DTO.bugemon.BugemonDTO;

import java.io.Serializable;
import java.util.List;

public class PlayerTeamMessage implements Serializable {
    private final List<BugemonDTO> bugemons;

    public PlayerTeamMessage(List<BugemonDTO> bugemons) {
        this.bugemons = bugemons;
    }

    public List<BugemonDTO> getBugemons() { return bugemons; }
}
