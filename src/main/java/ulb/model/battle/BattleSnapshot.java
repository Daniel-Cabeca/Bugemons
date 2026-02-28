package ulb.model.battle;

import ulb.model.team.Team;

/**
 * View of the battle from the point of view of a player.
 */
public class BattleSnapshot {
	private Battle battle;
	private boolean isTeamA;

	/**
	 * Creates a battle snapshot as the point of view of either team A or team A.
	 *
	 * @param battle The battle
	 * @param isTeamA True if the battle is viewed from team A's poiint of view, false otherwise
	 */
	BattleSnapshot(Battle battle, boolean isTeamA){
		this.battle = battle;
		this.isTeamA = isTeamA;
	}

	/**
	 * Gives the team that the snapshot views itself as.
	 *
	 * @return The snapshot's own team
	 */
	public Team getTeamSelf(){
		if (this.isTeamA) {
			return battle.getTeamA();
		}
		else {
			return battle.getTeamB();
		}
	}

	/**
	 * Gives the team that the snapshot views its opponent as.
	 *
	 * @return The snapshot's opponent team
	 */
	public Team getTeamOpponent(){
		if (this.isTeamA) {
			return battle.getTeamB();
		}
		else {
			return battle.getTeamA();
		}
	}
}
