package ulb.model.battle;

import ulb.model.team.Team;
import ulb.model.Player;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;
import ulb.model.item.Item;
import ulb.model.effect.EffectHeal;
import ulb.controller.action.*; 
import ulb.model.reward.Reward;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import ulb.model.reward.RewardType;


public class Battle {
	private final int XP_COEF = 30;

	private BattleParticipant participantA;
	private BattleParticipant participantB;

	private boolean isBossBattle = false;
	private int floorNumber = 1;

	private boolean gameFinished = false;

	private List<String> logMsg;
	private List<ActiveEffect> activeEffects;

	public enum ParticipantLabel {
		TEAM_A,
		TEAM_B
	}

	public Battle(Team teamA, Team teamB, Player playerA, Player playerB) {
		this.participantA = new BattleParticipant(playerA, teamA);
		this.participantB = new BattleParticipant(playerB, teamB);
		this.logMsg = new ArrayList<>();
		this.activeEffects = new ArrayList<>();
	}

	public BattleParticipant getParticipant(ParticipantLabel team) {
		if (team == ParticipantLabel.TEAM_A) {
			return this.participantA;
		} else {
			return this.participantB;
		}
	}

	public Team getTeam(ParticipantLabel team) { return this.getParticipant(team).getTeam(); }

	public Bugemon getActiveBugemon(ParticipantLabel team) { return this.getParticipant(team).getActiveBugemon(); }

	public Player getPlayer(ParticipantLabel team) { return this.getParticipant(team).getPlayer(); }

	public Action getAction(ParticipantLabel team) { return this.getParticipant(team).getAction(); }

	public List<ActiveEffect> getActiveEffects() { return this.activeEffects; }

	public BattleState getState(ParticipantLabel team) { return this.getParticipant(team).getState(); }

	public ParticipantLabel getOpponentTeamLabel(ParticipantLabel currentTeam) { 
		return currentTeam == ParticipantLabel.TEAM_B ? ParticipantLabel.TEAM_A : ParticipantLabel.TEAM_B; 
	}

	public int getHpAfterFirstActionSelf(ParticipantLabel team) { return this.getParticipant(team).getHpAfterFirstAction(); }

	public int getHpAfterFirstActionOpponent(ParticipantLabel team) { return this.getParticipant(getOpponentTeamLabel(team)).getHpAfterFirstAction(); }

	public void setAction(Action action, ParticipantLabel team) { this.getParticipant(team).setAction(action); }

	public void setActiveBugemon(Bugemon bugemon, ParticipantLabel team) { this.getParticipant(team).setActiveBugemon(bugemon); 
	}

	private void setState(BattleState state, ParticipantLabel team) { this.getParticipant(team).setState(state); }

	public boolean isBugemonKO(ParticipantLabel team){ return this.getActiveBugemon(team).isKO(); }

	public boolean isTeamKO(ParticipantLabel team){ return getTeam(team).checkTeamKO(); }

	public void setFloorNumber(int floorNumber){ this.floorNumber = floorNumber; }

	public void enableBossBattle() { this.isBossBattle = true; }

	/**
	 * get the team that has the initiative
	 * @return the TeamLabel of the first team to play
	 */
	public ParticipantLabel getFirstTeamToPlay(){
		if (getActiveBugemon(ParticipantLabel.TEAM_A).checkInitiative(getActiveBugemon(ParticipantLabel.TEAM_B))){
			return ParticipantLabel.TEAM_A;
		}
		return ParticipantLabel.TEAM_B;
	}

	/**
	 * Checks if an item can be used or not given the stats of the bugemon
	 *
	 * @param item the item that needs to be checked
	 * @param team the team for which the item is checked
	 * @return if the item can be used or not (boolean)
	 */
	public boolean checkItem(Item item, ParticipantLabel team) {
		if (item.getEffect() instanceof EffectHeal) {
			return this.getActiveBugemon(team).hasHPDecreased();
		}

		return true;
	}

	/**
	 * Removes an item that has been used from the inventory of the team
	 * @param team the team from which the item is removed
	 * @param item the item which is removed from the inventory
	 */
	private void removeUsedItemFromInventory(ParticipantLabel team, Item item) {
		getPlayer(team).getInventory().removeItem(item);
	}

	/**
	 * Uses an item and manages its removal from the inventory
	 * @param item the item which is used
	 * @param team the team that uses the item
	 */
	public boolean applyItem(Item item, ParticipantLabel team){
		if (item == null) {
			return false;
		}

		item.use(this, team);
		removeUsedItemFromInventory(team, item);
		logMsg.add("L'objet " + item.getName() + " a été utilisé.");
		return true;
	}

	/**
	 * Swaps the active Bugemon to another one
	 * @param target the Bugemon which will replace the current active Bugemon
	 * @param team the team on which the active Bugemon will be replaced
	 */
	public boolean performSwap(Bugemon target, ParticipantLabel team){
		if (!checkSwappableBugemon(target, team)){
			return false;
		}

		setActiveBugemon(target, team);
		if (team == ParticipantLabel.TEAM_A){
			logMsg.add("Tu as envoyé " + target.getName() + "!");
		} else {
			logMsg.add("L'adversaire a envoyé " + target.getName() + "!");
		}
		return true;
	}

	/**
	 * Returns the next available non-active Bugemon when switching.
	 * @param team the team whose Bugemons are being considered
	 * @return the next available non-active Bugemon or null if none available
	 */
	public Bugemon getNextBugemon(ParticipantLabel team){
		return this.getTeam(team).getNextBugemon(this.getActiveBugemon(team));
	}

	/**
	 * Returns all available actions based on current game state
	 * @param team the team whose available actions are returned
	 * @return the currently available actions
	 */
	public Vector<Action> getAvailableActions(ParticipantLabel team) {
		Vector<Action> actions = new Vector<Action>();

		if (gameFinished){
			return actions;
		}

		switch (this.getState(team)) {
			case INGAME:
				actions.add(new UseAbility());
				actions.add(new Run());
				actions.add(new Swap());
				actions.add(new UseItem());
				break;

			case SWAPPING:
				actions.add(new Swap());
				break;

			case LOST:
				break;

			case WON:
				break;

			case WAITING:
				break;

			default:
				break;
		}
		return actions;
	}

	/**
	 * Applies an action on a team
	 * @param action the action being applied
	 * @param team the team on which the action is applied
	 * @return boolean indicating if the action succeeded
	 */
	private boolean applyAction(Action action, ParticipantLabel team){
		return action != null && action.executeAction(this, team);
	}

	/**
	 * Registers an action done by a specific team and calls the handling of a round when both teams are waiting
	 * @param action the action to be registered
	 * @param ownTeam the team which registers the action
	 * @param oppositeTeam the opposite team, whose state changes if ownTeam swaps active Bugemons
	 * @param ownState the current state of the team registering the action
	 */
	private void registerAction(Action action, ParticipantLabel ownTeam, ParticipantLabel oppositeTeam, BattleState ownState) {
		switch (ownState) {
			case INGAME:
				setAction(action, ownTeam);
				setState(BattleState.WAITING, ownTeam);
				break;

			case SWAPPING:
				setAction(action, ownTeam);
				if (applyAction(action, ownTeam)){
					setState(BattleState.INGAME, ownTeam);
					setState(BattleState.INGAME, oppositeTeam);
				}
				break;

			default:
				break;
		}
		if (this.getState(ownTeam) == BattleState.WAITING && this.getState(oppositeTeam) == BattleState.WAITING){
			handleRound();
		}
	}

	/**
	 * Registers an action of a specific team
	 * @param action the action to be registered
	 * @param team team registering the action
	 */
	public synchronized void chooseAction(Action action, ParticipantLabel team){
		registerAction(action, team, getOpponentTeamLabel(team), getState(team));
	}

	public void resetFightStats() {
		this.resetAllFightStats();
	}

	/**
	 * handle the round of one of the two players
	 * @param playerTeam the player who plays now
	 * @return a boolean depending on if the turn continues
	 */
	private boolean handlePlayerTurn(ParticipantLabel playerTeam){
		Action currentAction = getAction(playerTeam);

		this.applyAction(currentAction, playerTeam);

		if (gameFinished) {
			handleBattleEnd();
			return false;
		}

		if (handleActionFinished(playerTeam)){
			tickActiveEffects();
			if (gameFinished){
				handleBattleEnd();
			}
			return false;
		}
		return true;
	}

	/**
	 * Handles one round of the battle
	 */
	private void handleRound(){
		// checks whose action should be executed first and applies it
		this.participantA.setHpAfterFirstAction(-1);
		this.participantB.setHpAfterFirstAction(-1);

		ParticipantLabel firstPlayer = this.getFirstTeamToPlay();
		ParticipantLabel secondPlayer = getOpponentTeamLabel(firstPlayer);
		
		if (! this.handlePlayerTurn(firstPlayer)){
			return;
		}

		this.participantA.setHpAfterFirstAction(this.participantA.getActiveBugemon().getHp());
		this.participantB.setHpAfterFirstAction(this.participantB.getActiveBugemon().getHp());

		logMsg.add(null); // separator between first and second action messages 
		
		if (! this.handlePlayerTurn(secondPlayer)){
			return;
		}

		tickActiveEffects();
		setState(BattleState.INGAME, ParticipantLabel.TEAM_A);
		setState(BattleState.INGAME, ParticipantLabel.TEAM_B);
	}

	/**
	 * Decrements TTL of all active TOUR effects and reverts those that have expired.
	 */
	private void tickActiveEffects() {
		List<ActiveEffect> expired = new ArrayList<>();
		for (ActiveEffect ae : activeEffects) {
			ae.ttl--;
			if (ae.ttl <= 0) {
				Stats revert = new Stats(-ae.delta.getHp(), -ae.delta.getAttack(), -ae.delta.getDefense(), -ae.delta.getInitiative());
				ae.target.changeFightStats(revert);
				logMsg.add("L'effet de " + ae.itemName + " sur " + ae.target.getName() + " s'est dissipé!");
				expired.add(ae);
			}
		}
		activeEffects.removeAll(expired);
	}


	/**
	 * Check if the round is finished and set the states depending on that
	 * @param previousActiveTeam the team who just finished the round
	 * @return a boolean depending on if the round is finished
	 */
	private boolean handleActionFinished(ParticipantLabel previousActiveTeam){
		ParticipantLabel opponentTeam = getOpponentTeamLabel(previousActiveTeam);
		if (this.isTeamKO(opponentTeam)){
			setState(BattleState.LOST, opponentTeam);
			setState(BattleState.WON, previousActiveTeam);
			this.gameFinished = true;

		} else if (this.isBugemonKO(opponentTeam)){
			setState(BattleState.SWAPPING, opponentTeam);
			setState(BattleState.WAITING, previousActiveTeam);
			if (previousActiveTeam == ParticipantLabel.TEAM_A){
				logMsg.add(this.participantB.getActiveBugemon().getName() + " est KO!");
				if (this.participantA.getHpAfterFirstAction() == -1) {
					this.participantA.setHpAfterFirstAction(this.participantA.getActiveBugemon().getHp());
					this.participantB.setHpAfterFirstAction(0);

				}

				logMsg.add(null);
				Bugemon nextB = getTeam(ParticipantLabel.TEAM_B).getNextBugemon(this.participantB.getActiveBugemon());

				if (nextB != null) {
					setActiveBugemon(nextB, ParticipantLabel.TEAM_B);
					logMsg.add("L'adversaire a envoyé " + nextB.getName() + "!");
				} // TO REFACTOR : le changement de bugemon ne doit pas se faire dans battle mais dans strategy, refactor des logs en fonction de ça.
				setState(BattleState.INGAME, ParticipantLabel.TEAM_A);
				setState(BattleState.INGAME, ParticipantLabel.TEAM_B); // TO REFACTOR TOO
			}
			
		} else {
			return false;
		}
		return true;
	}

	/**
	 * Handles the end of the battle
	 */
	private void handleBattleEnd(){
		List<Bugemon> winners = this.participantA.getParticipatingBugemons();
		Team losers = this.getTeam(ParticipantLabel.TEAM_B);
		if (this.getState(ParticipantLabel.TEAM_B) == BattleState.WON){
			winners = this.participantB.getParticipatingBugemons();
			losers = this.getTeam(ParticipantLabel.TEAM_A);
		}

		int gainedXP = computeTotalXP(losers);
		for (Bugemon b : winners){
			b.gainXp(gainedXP / winners.size());
		}

	}

	/**
	 * Computes total XP
	 * @param losers the vanquished team
	 * @return the gained XP
	 */
	public int computeTotalXP(Team losers){
		int boss_multiplicator = 1;
		if (isBossBattle){
			boss_multiplicator = 2;
		}
		return XP_COEF * floorNumber * boss_multiplicator * losers.size();
	}

	/**
	 * Returns the available Bugemons
	 * @param team the team whose available Bugemons are returned
	 * @return the available Bugemons
	 */
	public List<Bugemon> getAvailableBugemons(ParticipantLabel teamLabel){
		return getTeam(teamLabel).getBugemonsAlive();
	}

	/**
	 * Computes rewards for Bugemons which have to receive them
	 * @param bugemonTarget the Bugemons receiving a reward
	 * @return the rewards computed
	 */
	public Vector<Reward> computeRewards(Bugemon bugemonTarget){
		List<RewardType> types = new ArrayList<>(List.of(RewardType.values()));
		Collections.shuffle(types);
		Vector<Reward> rewards = new Vector<>();
		for (int i = 0; i < 3; i++) {
			Reward r = new Reward(bugemonTarget);
			r.configureReward(types.get(i));
			rewards.add(r);
		}
		return rewards;
	}

	public boolean isGameFinished() {
		return this.gameFinished;
	}

	/**
	 * Resets all fight stats
	 */
	private void resetAllFightStats(){
		for (Bugemon b : this.getTeam(ParticipantLabel.TEAM_A).getMembers()){
			b.removeStatsDebuffs();
		}

		for (Bugemon b : this.getTeam(ParticipantLabel.TEAM_B).getMembers()){
			b.removeStatsDebuffs();
		}
	}

	/**
	 * Checks if the Bugemon trying to be swapped can be used
	 * @param bugemon the Bugemon trying to be swapped
	 * @param team the team on which the Bugemon should be
	 * @return true if the Bugemon is available, false otherwise
	 */
	public boolean checkSwappableBugemon(Bugemon bugemon, ParticipantLabel team){
		return this.getTeam(team).isBugemonOK(bugemon);
	}


	public void forfeit(ParticipantLabel team) {
		ParticipantLabel opponentTeam = getOpponentTeamLabel(team);
		setState(BattleState.LOST, team);
		setState(BattleState.WON, opponentTeam);
		this.gameFinished = true;
	}

	public int getHpAfterFirstActionA() { return this.participantA.getHpAfterFirstAction(); }
	public int getHpAfterFirstActionB() { return this.participantB.getHpAfterFirstAction(); }

	public List<String> getLogMsg() { return logMsg; } 
	public void addLogMsg(String log){this.logMsg.add(log);}

	/** 
	 * Clears the current log message
	 */
	public void clearLogMsg() { 
		logMsg.clear();
	}
}
