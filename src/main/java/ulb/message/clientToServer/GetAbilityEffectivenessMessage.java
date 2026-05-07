package ulb.message.clientToServer;

import java.util.List;

import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class GetAbilityEffectivenessMessage implements ClientToServerMessage {
    private List<AbilityDTO> abilities;
    private BugemonDTO bugemonTarget;

    public GetAbilityEffectivenessMessage(List<AbilityDTO> abilities, BugemonDTO bugemonTarget){
        this.abilities = abilities;
        this.bugemonTarget = bugemonTarget;
    }

    @Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.handle(this);
	}

    public List<AbilityDTO> getAbilities(){return this.abilities;}
    public BugemonDTO getBugemonTarget(){return this.bugemonTarget;}
}
