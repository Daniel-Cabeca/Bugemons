package ulb.communication.types;

import javafx.event.ActionEvent;
import ulb.communication.Message;
import ulb.controller.GameController;
import ulb.model.battle.BattleState;

/**
 * Sent from the battle view when the battle may have ended (win/loss), so the controller
 * can run level-up flow and/or switch windows. {@link ActionEvent} is not serialized (transient).
 */
public class BattleEndCheckMessage implements Message {
	private final BattleState battleState;
	private final transient ActionEvent actionEvent;

	/**
	 * @param battleState outcome to evaluate (e.g. won or lost)
	 * @param actionEvent JavaFX event used to navigate scenes; not serialized
	 */
	public BattleEndCheckMessage(BattleState battleState, ActionEvent actionEvent) {
		this.battleState = battleState;
		this.actionEvent = actionEvent;
	}

	/** @return the battle outcome sent from the view */
	public BattleState getBattleState() {
		return battleState;
	}

	/** @return the UI event for opening the next window, or {@code null} */
	public ActionEvent getActionEvent() {
		return actionEvent;
	}

	public MessageType getMessageType() {
		return MessageType.BATTLE_END_CHECK;
	}

	@Override
    public Message handle(GameController controller) {
        return controller.applyOn(this);
    }
}
