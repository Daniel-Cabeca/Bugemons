package ulb.message.serverToClient;

import java.io.Serializable;

import ulb.DTO.player.PlayerDTO;

public class PlayerMessage implements Serializable {
    private PlayerDTO player;

    public PlayerMessage(PlayerDTO player){
        this.player = player;
    }

    public PlayerDTO getPlayer(){
        return this.player;
    }
}
