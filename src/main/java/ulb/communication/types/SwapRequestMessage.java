package ulb.communication.types;

import ulb.communication.Message;
import ulb.model.bugemon.Bugemon;

/**
 * Player chose to swap to another Bugémon. {@link Bugemon} is not serialized (transient).
 */
public class SwapRequestMessage implements Message {
	private final transient Bugemon bugemon;

	/**
	 * @param bugemon the team member to switch to (transient reference)
	 */
	public SwapRequestMessage(Bugemon bugemon) {
		this.bugemon = bugemon;
	}

	/** @return the Bugémon chosen for swap, or {@code null} */
	public Bugemon getBugemon() {
		return bugemon;
	}
}
