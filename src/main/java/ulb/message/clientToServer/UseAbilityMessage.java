package ulb.message.clientToServer;

import ulb.DTO.ability.AbilityDTO;

import ulb.Server.ServerMessageHandler;
import ulb.message.ClientToServerMessage;

public class UseAbilityMessage implements ClientToServerMessage{
    private AbilityDTO ability;

    public UseAbilityMessage(AbilityDTO ability){
        this.ability = ability;
    }

    @Override
	public void dispatch(ServerMessageHandler handler) {
		handler.handle(this);
	}

    public AbilityDTO getAbility(){return this.ability;}
}
