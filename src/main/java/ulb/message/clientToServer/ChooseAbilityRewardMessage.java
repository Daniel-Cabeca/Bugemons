package ulb.message.clientToServer;

import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class ChooseAbilityRewardMessage implements ClientToServerMessage{
	private BugemonDTO bugemon;
	private AbilityDTO oldAbility;
	private AbilityDTO newAbility;

	public ChooseAbilityRewardMessage(BugemonDTO bugemon, AbilityDTO oldAbility, AbilityDTO newAbility){
		this.bugemon = bugemon;
		this.oldAbility = oldAbility;
		this.newAbility = newAbility;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.chooseAbilityReward(bugemon, oldAbility, newAbility);
	}
}
