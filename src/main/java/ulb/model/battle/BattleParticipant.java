package ulb.model.battle;

import java.util.Vector;

import ulb.model.Player;
import ulb.model.action.Action;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;

/**
 * Runtime state for one battle participant.
 */
public class BattleParticipant {
    private Player player;
	private Team team;
	private Bugemon activeBugemon;
	private Vector<Bugemon> participatingBugemons;
	private BattleState state;
	private Action action = null;
	private int hpAfterFirstAction = -1;

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
		this.participatingBugemons = new Vector<>() {};
		this.setActiveBugemon(activeBugemon);
    }

	/** Returns participant player. */
    public Player getPlayer() { return player; }
	/** Returns participant team. */
    public Team getTeam() { return team; }
	/** Returns active bugemon. */
    public Bugemon getActiveBugemon() { return activeBugemon; }

	/**
	 * Sets active bugemon and tracks participation.
	 *
	 * @param activeBugemon New active bugemon
	 */
    public void setActiveBugemon(Bugemon activeBugemon) { 
        this.activeBugemon = activeBugemon; 
        if (!this.participatingBugemons.contains(activeBugemon)){
            participatingBugemons.add(activeBugemon);
        }
    }


    public BattleState getState() { return state; }

    public void setState(BattleState state) { this.state = state; }

    public Vector<Bugemon> getParticipatingBugemons() { return participatingBugemons; }

    public Action getAction() { return action; }

    public void setAction(Action action) { this.action = action; }

    public int getHpAfterFirstAction() { return hpAfterFirstAction; }

    public void setHpAfterFirstAction(int hpAfterFirstAction) { this.hpAfterFirstAction = hpAfterFirstAction; }

}
