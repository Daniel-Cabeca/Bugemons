package ulb.model.battle;

import ulb.model.Player;
import ulb.model.action.*;
import ulb.model.bugemon.Bugemon;
import ulb.model.effect.EffectHeal;
import ulb.model.item.Item;
import ulb.model.reward.Reward;
import ulb.model.reward.RewardType;
import ulb.model.team.Team;
import ulb.service.AccountService;

import java.util.*;


public class Battle {
	private final int XP_COEF = 30;
	private final BattleHandler battleHandler;
	private AccountService accountService;
	private boolean multiplayerBattle;
	private final BattleParticipant participantA;
	private final BattleParticipant participantB;

	private boolean isBossBattle = false;
	private int floorNumber = 1;

	private boolean gameFinished = false;

	private final List<String> logMsg;
	private boolean clearLogs = false;
	private final List<ActiveEffect> activeEffects;

	public Battle(Team teamA, Team teamB, Player playerA, Player playerB, boolean multiplayerBattle,
				  AccountService accountService) {
		this(teamA, teamB, playerA, playerB);
		this.accountService = accountService;
		this.multiplayerBattle = multiplayerBattle;
	}

	public Battle(Team teamA, Team teamB, Player playerA, Player playerB) {
		this.participantA = new BattleParticipant(playerA, teamA);
		this.participantB = new BattleParticipant(playerB, teamB);
		this.logMsg = new ArrayList<>();
		this.activeEffects = new ArrayList<>();
		this.multiplayerBattle = false;
		this.battleHandler = new BattleHandler(this);
	}

	public Action getAction(ParticipantLabel team) { return this.getParticipant(team).getAction(); }

	public BattleParticipant getParticipant(ParticipantLabel team) {
		if (team == ParticipantLabel.TEAM_A) {
			return this.participantA;
		} else {
			return this.participantB;
		}
	}

	public List<ActiveEffect> getActiveEffects() { return this.activeEffects; }

	public boolean getGameFinished() { return this.gameFinished; }

	public boolean getMultiplayerBattle() { return this.multiplayerBattle; }

	public AccountService getaccountService() { return this.accountService; }

	public int getHpAfterFirstActionSelf(ParticipantLabel team) { return this.getParticipant(team).getHpAfterFirstAction(); }

	public int getHpAfterFirstActionOpponent(ParticipantLabel team) { return this.getParticipant(getOpponentTeamLabel(team)).getHpAfterFirstAction(); }

	public ParticipantLabel getOpponentTeamLabel(ParticipantLabel currentTeam) {
		return currentTeam == ParticipantLabel.TEAM_B ? ParticipantLabel.TEAM_A : ParticipantLabel.TEAM_B;
	}

	public void setAction(Action action, ParticipantLabel team) { this.getParticipant(team).setAction(action); }

	public boolean isBugemonKO(ParticipantLabel team) { return this.getActiveBugemon(team).isKO(); }

	public Bugemon getActiveBugemon(ParticipantLabel team) { return this.getParticipant(team).getActiveBugemon(); }

	public boolean isTeamKO(ParticipantLabel team) { return getTeam(team).checkTeamKO(); }

	public Team getTeam(ParticipantLabel team) { return this.getParticipant(team).getTeam(); }

	public boolean isBossBattle() { return this.isBossBattle; }

	public void setFloorNumber(int floorNumber) { this.floorNumber = floorNumber; }

	public void setParticipantHpAfterFirstAction(BattleParticipant participant, int hp) { participant.setHpAfterFirstAction(hp); }

	public void enableBossBattle() { this.isBossBattle = true; }

	/**
	 * get the team that has the initiative
	 *
	 * @return the TeamLabel of the first team to play
	 */
	public ParticipantLabel getFirstTeamToPlay() {
		if (getParticipantA().hasInitiative(getParticipantB())) {
			return ParticipantLabel.TEAM_A;
		}
		return ParticipantLabel.TEAM_B;
	}

	public BattleParticipant getParticipantA() { return this.participantA; }

	public BattleParticipant getParticipantB() { return this.participantB; }

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
	 * Uses an item and manages its removal from the inventory
	 *
	 * @param item the item which is used
	 * @param team the team that uses the item
	 */
	public boolean applyItem(Item item, ParticipantLabel team) {
		if (item == null) {
			return false;
		}
		item.use(this, team);
		removeUsedItemFromInventory(team, item);
		logMsg.add("L'objet " + item.getName() + " a été utilisé.");
		return true;
	}

	/**
	 * Removes an item that has been used from the inventory of the team
	 *
	 * @param team the team from which the item is removed
	 * @param item the item which is removed from the inventory
	 */
	private void removeUsedItemFromInventory(ParticipantLabel team, Item item) {
		getPlayer(team).getInventory().removeItem(item);
	}

	public Player getPlayer(ParticipantLabel team) { return this.getParticipant(team).getPlayer(); }

	/**
	 * Swaps the active Bugemon to another one
	 *
	 * @param target the Bugemon which will replace the current active Bugemon
	 * @param team the team on which the active Bugemon will be replaced
	 */
	public boolean performSwap(Bugemon target, ParticipantLabel team) {
		if (!checkSwappableBugemon(target, team)) {
			return false;
		}

		setActiveBugemon(target, team);
		logMsg.add(target.getName() + " a été envoyé !");
		return true;
	}

	/**
	 * Checks if the Bugemon trying to be swapped can be used
	 *
	 * @param bugemon the Bugemon trying to be swapped
	 * @param team the team on which the Bugemon should be
	 * @return true if the Bugemon is available, false otherwise
	 */
	public boolean checkSwappableBugemon(Bugemon bugemon, ParticipantLabel team) {
		return this.getTeam(team).isBugemonOK(bugemon);
	}

	public void setActiveBugemon(Bugemon bugemon, ParticipantLabel team) { this.getParticipant(team).setActiveBugemon(bugemon); }

	/**
	 * Returns the next available non-active Bugemon when switching.
	 *
	 * @param team the team whose Bugemons are being considered
	 * @return the next available non-active Bugemon as an Optional object.
	 * The Optional is empty if no Bugemon is available
	 */
	public Optional<Bugemon> getNextBugemon(ParticipantLabel team) {
		return this.getTeam(team).getNextBugemon(this.getActiveBugemon(team));
	}

	/**
	 * Returns all available actions based on current game state
	 *
	 * @param team the team whose available actions are returned
	 * @return the currently available actions
	 */
	public Vector<Action> getAvailableActions(ParticipantLabel team) {
		Vector<Action> actions = new Vector<Action>();

		if (gameFinished) {
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

	public BattleState getState(ParticipantLabel team) { return this.getParticipant(team).getState(); }

	/**
	 * Applies an action on a team
	 *
	 * @param action the action being applied
	 * @param team the team on which the action is applied
	 * @return boolean indicating if the action succeeded
	 */
	boolean applyAction(Action action, ParticipantLabel team) {
		return action != null && action.executeAction(this, team);
	}

	/**
	 * Registers an action done by a specific team and calls the handling of a round when both teams are waiting
	 *
	 * @param action the action to be registered
	 * @param ownTeam the team which registers the action
	 * @param oppositeTeam the opposite team, whose state changes if ownTeam swaps active Bugemons
	 * @param ownState the current state of the team registering the action
	 */
	private void registerAction(Action action, ParticipantLabel ownTeam, ParticipantLabel oppositeTeam,
								BattleState ownState) {
		switch (ownState) {
			case INGAME:
				setAction(action, ownTeam);
				setState(BattleState.WAITING, ownTeam);
				break;

			case SWAPPING:
				setAction(action, ownTeam);
				if (applyAction(action, ownTeam)) {
					setState(BattleState.INGAME, ownTeam);
					setState(BattleState.INGAME, oppositeTeam);
				}
				break;

			default:
				break;
		}
		if (this.getState(ownTeam) == BattleState.WAITING && this.getState(oppositeTeam) == BattleState.WAITING) {
			handleRound();
		}
	}

	/**
	 * Registers an action of a specific team
	 *
	 * @param action the action to be registered
	 * @param team team registering the action
	 */
	public synchronized void chooseAction(Action action, ParticipantLabel team) {
		registerAction(action, team, getOpponentTeamLabel(team), getState(team));
	}

	public void resetFightStats() {
		this.resetAllFightStats();
	}

	/**
	 * Resets all fight stats
	 */
	private void resetAllFightStats() {
		for (Bugemon b : this.getTeam(ParticipantLabel.TEAM_A).getMembers()) {
			b.removeStatsDebuffs();
		}

		for (Bugemon b : this.getTeam(ParticipantLabel.TEAM_B).getMembers()) {
			b.removeStatsDebuffs();
		}
	}

	/**
	 * Handles one round of the battle
	 */
	private void handleRound() {
		this.battleHandler.handleRound();
	}

	/**
	 * Computes total XP
	 *
	 * @param losers the vanquished team
	 * @return the gained XP
	 */
	public int computeTotalXP(Team losers) {
		int boss_multiplicator = 1;
		if (isBossBattle) {
			boss_multiplicator = 2;
		}
		return XP_COEF * floorNumber * boss_multiplicator * losers.size();
	}

	/**
	 * Returns the available Bugemons
	 *
	 * @param teamLabel the team whose available Bugemons are returned
	 * @return the available Bugemons
	 */
	public List<Bugemon> getAvailableBugemons(ParticipantLabel teamLabel) {
		return getTeam(teamLabel).getBugemonsAlive();
	}

	/**
	 * Computes rewards for Bugemons which have to receive them
	 *
	 * @param bugemonTarget the Bugemons receiving a reward
	 * @return the rewards computed
	 */
	public Vector<Reward> computeRewards(Bugemon bugemonTarget) {
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

	public void setGameFinished(boolean finished) { this.gameFinished = finished; }

	public void forfeit(ParticipantLabel team) {
		ParticipantLabel opponentTeam = getOpponentTeamLabel(team);
		setState(BattleState.LOST, team);
		setState(BattleState.WON, opponentTeam);
		this.gameFinished = true;
	}

	void setState(BattleState state, ParticipantLabel team) { this.getParticipant(team).setState(state); }

	public List<String> getLogMsg() { return logMsg; }

	public void addLogMsg(String log) { this.logMsg.add(log); }

	/**
	 * Clears the current log message
	 */
	synchronized public void clearLogMsg() {
		if (!multiplayerBattle) { // if solo battle, always clears the logs
			logMsg.clear();
			return;
		} // else, clears the logs only when the two player asked for it
		if (clearLogs) {
			logMsg.clear();
		}
		clearLogs = !clearLogs; // switch the flag so that it clears the logs only when the two players asked for
	}

	public enum ParticipantLabel {
		TEAM_A, TEAM_B
	}
}
