package ulb.controller;

import java.util.List;
import java.util.Vector;

import ulb.model.ability.Ability;
import ulb.model.battle.Battle;
import ulb.model.battle.BattleState;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.team.Team;
import ulb.model.bugemon.Bugemon;
import ulb.controller.action.*;
import ulb.model.Player;
import ulb.model.item.Item;
import ulb.model.reward.Reward;
import ulb.service.ItemService;

/**
 * Coordinates battle actions and data for one participant.
 */
public class BattleController {
	private Player player;
	private Battle battle;
	private ParticipantLabel participantLabel;
	private final ItemService itemService;

	private Vector<Reward> rewards;

	/**
	 * Creates a battle controller for a participant.
	 *
	 * @param player The owning player
	 * @param battle The active battle
	 * @param participantLabel Participant side label
	 * @param itemService Item service used during battle
	 */
	public BattleController(Player player, Battle battle, ParticipantLabel participantLabel, ItemService itemService) {
		this.player = player;
		this.battle = battle;
		this.participantLabel = participantLabel;
		this.itemService = itemService;
	}

	/**
	 * Returns the item service used by this controller.
	 *
	 * @return The item service
	 */
	public ItemService getItemService() { return this.itemService; }

	/**
	 * Checks if an item can be used or not given the stats of the bugemon
	 *
	 * @param item the item that needs to be checked
	 * @return if the item can be used or not (boolean)
	 */
	public boolean checkItem(Item item) {
		return this.battle.checkItem(item, this.participantLabel);
	}

	/**
	 * Get the possible random rewards from Battle and create a copy to avoid cheating
	 * @param bugemonTarget the bugemon targeted by the reward
	 * @return a vector of the copied rewards
	 */
	public Vector<Reward> getRewards(Bugemon bugemonTarget){
		this.rewards = battle.computeRewards(bugemonTarget);
		Vector<Reward> vector = new Vector<>();
		for (Reward r : this.rewards){
			vector.add(new Reward(r));
		}
		return vector;
	}

	/**
	 * Applies the chosen level up reward to the bugemon
	 *
	 * @param bugemonTarget the bugemon who receives the reward
	 * @param reward the chosen reward
	 * @return true if the reward was applied, false otherwise
	 */
	public boolean applyReward(Bugemon bugemonTarget, Reward reward){
		for (Reward r : this.rewards) {
			if (r.getStats().equals(reward.getStats()) && r.getBugemon().equals(bugemonTarget)){
				r.applyReward();
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the effectiveness factor of the current ability
	 *
	 * @param ability the ability whose type effectiveness is evaluated
	 * @return the effectiveness message (or null if the effectiveness is normal)
	 */
	public String getEffectiveness(Ability ability) {
		Bugemon oppositeBugemon = getActiveBugemonOpponent();
		return ability.getEffectivenessMessage(oppositeBugemon);
	}

	/** Returns the active bugemon of the controlled participant. */
	public Bugemon getActiveBugemonSelf() { return this.battle.getActiveBugemon(this.participantLabel); }

	/** Returns the active bugemon of the opponent participant. */
	public Bugemon getActiveBugemonOpponent() { return this.battle.getActiveBugemon(battle.getOpponentTeamLabel(this.participantLabel)); }

	/** Returns the owning player. */
	public Player getPlayer() { return this.player; }

	/** Returns the team controlled by this participant. */
	public Team getTeam() { return this.battle.getTeam(this.participantLabel); }

	/**
	 * Applies a battle action for this participant.
	 *
	 * @param action The chosen action
	 */
	public void useAction(Action action) { this.battle.chooseAction(action, this.participantLabel); }

	/** Resets fighter temporary stats for a new battle. */
	public void resetFighter() {this.battle.resetFightStats();}

	/** Returns currently available actions. */
	public Vector<Action> getAvailableAction() { return this.battle.getAvailableActions(this.participantLabel); }

	/** Returns currently available bugemons. */
	public List<Bugemon> getAvailableBugemons() { return this.battle.getAvailableBugemons(this.participantLabel); }

	/** Indicates whether the battle has ended. */
	public boolean isGameFinished() { return this.battle.isGameFinished(); }

	/** Returns current battle state for this participant. */
	public BattleState getState() { return this.battle.getState(this.participantLabel); }

	/** Returns self HP after first resolved action. */
	public int getHpAfterFirstActionSelf() { return battle.getHpAfterFirstActionSelf(this.participantLabel); }

	/** Returns opponent HP after first resolved action. */
	public int getHpAfterFirstActionOpponent() { return battle.getHpAfterFirstActionOpponent(this.participantLabel); }

	/** Returns current battle log messages. */
	public List<String> getLogMsg() { return this.battle.getLogMsg(); }

	/** Clears battle log messages. */
	public void clearLogMsg() { this.battle.clearLogMsg(); }

	/** Returns total XP from defeated opponent team. */
	public int getTotalXP() { return this.battle.computeTotalXP(battle.getTeam(battle.getOpponentTeamLabel(participantLabel))); }

	/** Returns the underlying battle model. */
	public Battle getBattle() { return this.battle; }
}
