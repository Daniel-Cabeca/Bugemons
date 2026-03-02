package ulb.controller.action;

import ulb.model.ability.Ability;

public class UseAbility implements Action {
	private Ability ability;

	public UseAbility(){}
	public UseAbility(Ability ability){
		this.ability = ability;
	}

	public Ability getAbility(){return ability;}

	public void setAbility(Ability ability){this.ability = ability;}
}

