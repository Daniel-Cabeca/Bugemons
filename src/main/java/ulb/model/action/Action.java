package ulb.model.action;

import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;

public interface Action {
    boolean executeAction(Battle battle, ParticipantLabel team);
}
