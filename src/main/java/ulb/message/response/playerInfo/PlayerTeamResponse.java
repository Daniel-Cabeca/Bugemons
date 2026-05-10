package ulb.message.response.playerInfo;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.message.response.Response;

import java.util.List;

public class PlayerTeamResponse extends Response {
    private final List<BugemonDTO> bugemons;

    public PlayerTeamResponse(List<BugemonDTO> bugemons) {
        this.bugemons = bugemons;
    }

    public List<BugemonDTO> getBugemons() { return bugemons; }
}
