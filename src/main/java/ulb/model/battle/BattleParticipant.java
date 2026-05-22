package ulb.model.battle;

import ulb.model.Player;
import ulb.model.action.Action;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;

import java.util.Vector;

/**
 * Runtime state for one battle participant.
 */
public class BattleParticipant {
	private final Player player;
	private final Team team;
	private Bugemon activeBugemon;
	private final Vector<Bugemon> participatingBugemons;
	private BattleState state;
	private Action action = null;
	private int hpAfterFirstAction = -1;

	private boolean readyToPlay = false;
	private boolean clearLogs = false;

	/**
	 * Creates a participant using the first team member as active bugemon.
	 *
	 * @param player Associated player
	 * @param team Participant team
	 */
	public BattleParticipant(Player player, Team team) {
		this(player, team, team.getMembers().get(0));
	}

	/**
	 * Creates a participant with an explicit active bugemon.
	 *
	 * @param player Associated player
	 * @param team Participant team
	 * @param activeBugemon Initial active bugemon
	 */
	public BattleParticipant(Player player, Team team, Bugemon activeBugemon) {
		this.player = player;
		this.team = team;
		this.activeBugemon = activeBugemon;
		this.state = BattleState.INGAME;
		this.participatingBugemons = new Vector<>() {
		};
		this.setActiveBugemon(activeBugemon);
	}

	/** 
	 * Returns participant player. 
	*/
	public Player getPlayer() { return player; }

	/**
	 * Returns participant team. 
	*/
	public Team getTeam() { return team; }

	/**
	 * check if the team has the initiative for the next move.
	 * It compares the priority of the two actions and then, if needed, the initiative of the two
	 * actives bugemons
	 *
	 * @param other the other participant to compare the initiative with
	 * @return true if the team has the initiative
	 */
	public boolean hasInitiative(BattleParticipant other) {
		int actionPriorityTeamA = this.getAction().hasHightPriority(other.getAction());

		if (actionPriorityTeamA != 0) { // if the priority isn't equal to 0, one action has higher priority than the
			// other
			return actionPriorityTeamA == 1;
		}

		return getActiveBugemon().checkInitiative(other.getActiveBugemon());
	}

	public Action getAction() { return action; }

	/** 
	 * Returns active bugemon. 
	*/
	public Bugemon getActiveBugemon() { return activeBugemon; }

	/**
	 * Sets active bugemon and tracks participation.
	 *
	 * @param activeBugemon New active bugemon
	 */
	public void setActiveBugemon(Bugemon activeBugemon) {
		this.activeBugemon = activeBugemon;
		if (!this.participatingBugemons.contains(activeBugemon)) {
			participatingBugemons.add(activeBugemon);
		}
	}

	public void setAction(Action action) { this.action = action; }

	public BattleState getState() { return state; }

	public void setState(BattleState state) { this.state = state; }

	public Vector<Bugemon> getParticipatingBugemons() { return participatingBugemons; }

	public int getHpAfterFirstAction() { return hpAfterFirstAction; }

	public void setHpAfterFirstAction(int hpAfterFirstAction) { this.hpAfterFirstAction = hpAfterFirstAction; }

	public boolean isReadyToPlay() { return this.readyToPlay; }

	public void setReadyToPlay(boolean readyToPlay) { this.readyToPlay = readyToPlay; }

	public boolean isClearLogs() { return this.clearLogs; }
	
	public void setClearLogs(boolean clearLogs) { this.clearLogs = clearLogs; }
}
