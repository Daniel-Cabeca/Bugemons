package ulb.message.response.gameData;

import ulb.DTO.ability.AbilityDTO;
import ulb.message.response.Response;

public class RandomAbilityResponse extends Response {
	private final AbilityDTO ability;

	public RandomAbilityResponse(AbilityDTO ability){
		this.ability = ability;
	}

	public AbilityDTO getAbility(){return this.ability;}
}
