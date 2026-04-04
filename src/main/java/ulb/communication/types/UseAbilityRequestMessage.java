package ulb.communication.types;

import ulb.communication.Message;
import ulb.controller.GameController;
import ulb.model.ability.Ability;

/**
 * Player chose to use an ability in manual battle
 */
public class UseAbilityRequestMessage implements Message {
	private final Ability ability;

	/**
	 * @param ability the attack to perform
	 */
	public UseAbilityRequestMessage(Ability ability) {
		this.ability = ability;
	}

	public Ability getAbility() { return ability; }

	@Override
	public MessageType getMessageType() {
		return MessageType.USE_ABILITY_REQUEST;
	}

	@Override
    public Message handle(GameController controller) {
        return controller.applyOn(this);
    }
}
