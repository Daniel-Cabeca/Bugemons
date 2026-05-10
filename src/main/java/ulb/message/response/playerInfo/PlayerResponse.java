package ulb.message.response.playerInfo;

import ulb.DTO.player.PlayerDTO;
import ulb.message.response.Response;

public class PlayerResponse extends Response {
    private final PlayerDTO player;

    public PlayerResponse(PlayerDTO player){
        this.player = player;
    }

    public PlayerDTO getPlayer(){
        return this.player;
    }
}
