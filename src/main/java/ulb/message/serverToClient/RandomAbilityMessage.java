package ulb.message.serverToClient;

import java.io.Serializable;

import ulb.DTO.ability.AbilityDTO;

public class RandomAbilityMessage implements Serializable{
	private AbilityDTO ability;

	public RandomAbilityMessage(AbilityDTO ability){
		this.ability = ability;
	}

	public AbilityDTO getAbility(){return this.ability;}
}
