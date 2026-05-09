package ulb.message.clientToServer.gameActions;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.clientToServer.ClientToServerMessage;
import ulb.DTO.ability.AbilityDTO;
import ulb.server.ServerMessageHandler;

public class UseAbilityMessage implements ClientToServerMessage{
    private final AbilityDTO ability;

    public UseAbilityMessage(AbilityDTO ability){
        this.ability = ability;
    }

    @Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.chooseUseAbilityAction(ability);
	}
}
