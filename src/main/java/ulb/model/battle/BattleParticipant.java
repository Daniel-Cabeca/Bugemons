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

	/** Returns participant battle state. */
    public BattleState getState() { return state; }
	/** Sets participant battle state. */
    public void setState(BattleState state) { this.state = state; }
	/** Returns bugemons that participated in battle. */
    public Vector<Bugemon> getParticipatingBugemons() { return participatingBugemons; }
	/** Returns selected action for current turn. */
    public Action getAction() { return action; }
	/** Sets selected action for current turn. */
    public void setAction(Action action) { this.action = action; }
	/** Returns HP after first resolved action in turn. */
    public int getHpAfterFirstAction() { return hpAfterFirstAction; }
	/** Stores HP after first resolved action in turn. */
    public void setHpAfterFirstAction(int hpAfterFirstAction) { this.hpAfterFirstAction = hpAfterFirstAction; }

}
