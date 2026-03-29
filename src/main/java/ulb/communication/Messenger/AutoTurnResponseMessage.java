package ulb.communication.Messenger;

import ulb.communication.Message;
import ulb.model.battle.BattleState;

/**
 * Answer from the controller after a battle step that yields an updated {@link BattleState},
 * e.g. auto-turn or manual actions ({@code UseItemRequestMessage}, swap, ability).
 */
public class AutoTurnResponseMessage implements Message {
	private final BattleState battleState;

	/**
	 * @param battleState state of the battle after the controller handled the request
	 */
	public AutoTurnResponseMessage(BattleState battleState) {
		this.battleState = battleState;
	}

	/** @return the battle state returned by the controller */
	public BattleState getBattleState() {
		return battleState;
	}
}
