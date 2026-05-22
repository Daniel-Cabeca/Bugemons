package ulb.model.action;

import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.item.Item;

/**
 * Action representing item usage during battle.
 */
public class UseItemAction extends Action {
	private Item item;

	/**
	 * Creates an item action.
	 *
	 * @param item Item to use
	 */
	public UseItemAction(Item item) {
		this();
		this.item = item;
	}

	/** 
	 * Creates an empty item action. 
	 */
	public UseItemAction() {
		this.priority = 2;
	}

	/** 
	 * {@inheritDoc} 
	 */
	@Override
	public boolean executeAction(Battle battle, ParticipantLabel team) {
		return battle.applyItem(this.item, team);
	}
}
