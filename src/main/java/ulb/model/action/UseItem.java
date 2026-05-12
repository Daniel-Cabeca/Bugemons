package ulb.model.action;

import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.item.Item;

/**
 * Action representing item usage during battle.
 */
public class UseItem implements Action {
    private Item item;

    /** Creates an empty item action. */
    public UseItem() {}

    /**
     * Creates an item action.
     *
     * @param item Item to use
     */
    public UseItem(Item item) {
        this.item = item;
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeAction(Battle battle, ParticipantLabel team) {
        return battle.applyItem(this.item, team);
    }

}
