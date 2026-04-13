package ulb.model.action;

import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;

public class Run implements Action {
    public Run() {}

    @Override
    public boolean executeAction(Battle battle, ParticipantLabel team) {
        battle.forfeit(team);
        return true;
    }
}
