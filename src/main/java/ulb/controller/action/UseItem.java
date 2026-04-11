package ulb.controller.action;

import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.item.Item;

public class UseItem implements Action {
    private Item item;

    public UseItem() {}

    public UseItem(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public boolean executeAction(Battle battle, ParticipantLabel team) {
        return battle.applyItem(this.item, team);
    }

}
