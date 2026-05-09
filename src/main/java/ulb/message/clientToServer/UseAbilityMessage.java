package ulb.message.clientToServer;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.DTO.ability.AbilityDTO;
import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class UseAbilityMessage implements ClientToServerMessage{
    private AbilityDTO ability;

    public UseAbilityMessage(AbilityDTO ability){
        this.ability = ability;
    }

    @Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.chooseUseAbilityAction(ability);
	}
}
