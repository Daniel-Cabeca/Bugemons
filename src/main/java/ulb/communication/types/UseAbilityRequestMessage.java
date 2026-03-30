package ulb.communication.types;

import ulb.communication.Message;
import ulb.model.ability.Ability;

/**
 * Player chose to use an ability in manual battle. {@link Ability} is not serialized (transient).
 */
public class UseAbilityRequestMessage implements Message {
	private final transient Ability ability;

	/**
	 * @param ability the attack to perform (transient reference)
	 */
	public UseAbilityRequestMessage(Ability ability) {
		this.ability = ability;
	}

	/** @return the selected ability, or {@code null} */
	public Ability getAbility() {
		return ability;
	}
}
