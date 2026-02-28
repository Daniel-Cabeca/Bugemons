package ulb.model.battle;

import ulb.model.ability.Ability;
import ulb.model.team.Team;

public class BattleSnapshot {
	private Battle battle;
	private boolean isTeamA;

	BattleSnapshot(Battle battle){
		this.battle = battle;
	}

	public Team getTeamSelf(){
		if (isTeamA) {
			return battle.getTeamA();
		}
		else {
			return battle.getTeamB();
		}
	}

	public Team getTeamOpponent(){
		if (! isTeamA) {
			return battle.getTeamB();
		}
		else {
			return battle.getTeamA();
		}
	}
}
