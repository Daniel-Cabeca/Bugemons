package ulb.communication.types;

import ulb.communication.Message;
import ulb.model.battle.BattleState;

/**
 * Answer from the controller after a battle turn
 */
public class AutoTurnResponseMessage implements Message {
	private final BattleState battleState;

	/**
	 * @param battleState state of the battle after the controller handled the request
	 */
	public AutoTurnResponseMessage(BattleState battleState) {
		this.battleState = battleState;
	}

	public BattleState getBattleState() {
		return battleState;
	}

	@Override
	public MessageType getMessageType() {
		return MessageType.AUTO_TURN_RESPONSE;
	}
}
