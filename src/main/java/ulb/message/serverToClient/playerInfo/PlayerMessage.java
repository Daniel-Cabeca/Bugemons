package ulb.message.serverToClient.playerInfo;

import java.io.Serializable;

import ulb.DTO.player.PlayerDTO;

public class PlayerMessage implements Serializable {
    private final PlayerDTO player;

    public PlayerMessage(PlayerDTO player){
        this.player = player;
    }

    public PlayerDTO getPlayer(){
        return this.player;
    }
}
