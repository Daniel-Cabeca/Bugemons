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

public class BattleController {
	private Player player;
	private Battle battle;
	private ParticipantLabel participantLabel;

	private Vector<Reward> rewards;

	public BattleController(Player player, Battle battle, ParticipantLabel participantLabel) {
		this.player = player;
		this.battle = battle;
		this.participantLabel = participantLabel;
	}

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

	public Bugemon getActiveBugemonSelf() { return this.battle.getActiveBugemon(this.participantLabel); }

	public Bugemon getActiveBugemonOpponent() { return this.battle.getActiveBugemon(battle.getOpponentTeamLabel(this.participantLabel)); }

	public Player getPlayer() { return this.player; }

	public Team getTeam() { return this.battle.getTeam(this.participantLabel); }

	public void useAction(Action action) { this.battle.chooseAction(action, this.participantLabel); }

	public Vector<Action> getAvailableAction() { return this.battle.getAvailableActions(this.participantLabel); }

	public List<Bugemon> getAvailableBugemons() { return this.battle.getAvailableBugemons(this.participantLabel); }

	public boolean isGameFinished() { return this.battle.isGameFinished(); }

	public BattleState getState() { return this.battle.getState(this.participantLabel); }

	public int getHpAfterFirstActionSelf() { return battle.getHpAfterFirstActionSelf(this.participantLabel); }

	public int getHpAfterFirstActionOpponent() { return battle.getHpAfterFirstActionOpponent(this.participantLabel); }

	public List<String> getLogMsg() { return this.battle.getLogMsg(); }

	public void clearLogMsg() { this.battle.clearLogMsg(); }

	public int getTotalXP() { return this.battle.computeTotalXP(battle.getTeam(battle.getOpponentTeamLabel(participantLabel))); }

	public Battle getBattle() { return this.battle; }
}
