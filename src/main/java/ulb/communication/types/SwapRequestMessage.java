package ulb.communication.types;

import ulb.communication.Message;
import ulb.controller.GameController;
import ulb.model.bugemon.Bugemon;

/**
 * Player chose to swap to another Bugémon.
 */
public class SwapRequestMessage implements Message {
	private final Bugemon bugemon;

	/**
	 * @param bugemon the team member to switch to
	 */
	public SwapRequestMessage(Bugemon bugemon) {
		this.bugemon = bugemon;
	}

	public Bugemon getBugemon() {
		return bugemon;
	}

	@Override
	public MessageType getMessageType() {
		return MessageType.SWAP_REQUEST;
	}

	@Override
    public Message handle(GameController controller) {
        return controller.applyOn(this);
    }
}
