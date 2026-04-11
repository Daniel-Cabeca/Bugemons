package ulb.message.clientToServer;

import java.util.List;

import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;

import ulb.Server.ServerMessageHandler;
import ulb.message.ClientToServerMessage;

public class GetAbilityEffectivenessMessage implements ClientToServerMessage {
    private List<AbilityDTO> abilities;
    private BugemonDTO bugemonTarget;

    public GetAbilityEffectivenessMessage(List<AbilityDTO> abilities, BugemonDTO bugemonTarget){
        this.abilities = abilities;
        this.bugemonTarget = bugemonTarget;
    }

    @Override
	public void dispatch(ServerMessageHandler handler) {
		handler.handle(this);
	}

    public List<AbilityDTO> getAbilities(){return this.abilities;}
    public BugemonDTO getBugemonTarget(){return this.bugemonTarget;}
}
