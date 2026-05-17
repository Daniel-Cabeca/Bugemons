package ulb.model.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import ulb.model.Player;
import ulb.model.action.Action;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;
import ulb.model.team.Team;
import ulb.server.ClientHandler;

public class BattleHandler {
	private static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());
	private Battle battle;

	public BattleHandler(Battle battle){
		this.battle = battle;
	}

	/**
	 * Decrements TTL of all active TOUR effects and reverts those that have expired.
	 */
	private void tickActiveEffects() {
		List<ActiveEffect> expired = new ArrayList<>();
		for (ActiveEffect ae : this.battle.getActiveEffects()) {
			ae.ttl--;
			if (ae.ttl <= 0) {
				Stats revert = new Stats(-ae.delta.getHp(), -ae.delta.getAttack(), -ae.delta.getDefense(), -ae.delta.getInitiative());
				ae.target.changeFightStats(revert);
				this.battle.getLogMsg().add("L'effet de " + ae.itemName + " sur " + ae.target.getName() + " s'est dissipé!");
				expired.add(ae);
			}
		}
		this.battle.getActiveEffects().removeAll(expired);
	}

	/**
	 * handle the round of one of the two players
	 * @param playerTeam the player who plays now
	 * @return a boolean depending on if the turn continues
	 */
	public boolean handlePlayerTurn(ParticipantLabel playerTeam){
		Action currentAction = this.battle.getAction(playerTeam);

		this.battle.applyAction(currentAction, playerTeam);

		if (this.battle.getGameFinished()) {
			handleBattleEnd();
			return false;
		}

		if (handleActionFinished(playerTeam)){
			tickActiveEffects();
			if (this.battle.getGameFinished()){
				handleBattleEnd();
			}
			return false;
		}
		return true;
	}

	public void handleRound(){

		BattleParticipant participantA = battle.getParticipantA();
		BattleParticipant participantB = battle.getParticipantB();

		// checks whose action should be executed first and applies it
		battle.setParticipantHpAfterFirstAction(participantA, -1);
		battle.setParticipantHpAfterFirstAction(participantB, -1);

		ParticipantLabel firstPlayer = this.battle.getFirstTeamToPlay();
		ParticipantLabel secondPlayer = this.battle.getOpponentTeamLabel(firstPlayer);
		
		if (! this.handlePlayerTurn(firstPlayer)){
			return;
		}

		battle.setParticipantHpAfterFirstAction(participantA, participantA.getActiveBugemon().getHp());
		battle.setParticipantHpAfterFirstAction(participantB, participantB.getActiveBugemon().getHp());

		this.battle.getLogMsg().add(null); // separator between first and second action messages 
		
		if (! this.handlePlayerTurn(secondPlayer)){
			return;
		}

		tickActiveEffects();
		this.battle.setState(BattleState.INGAME, ParticipantLabel.TEAM_A);
		this.battle.setState(BattleState.INGAME, ParticipantLabel.TEAM_B);
	}

	/**
	 * Check if the round is finished and set the states depending on that
	 * @param previousActiveTeam the team who just finished the round
	 * @return a boolean depending on if the round is finished
	 */
	public boolean handleActionFinished(ParticipantLabel previousActiveTeam){
		BattleParticipant participantA = battle.getParticipantA();
		BattleParticipant participantB = battle.getParticipantB();

		ParticipantLabel opponentTeam = this.battle.getOpponentTeamLabel(previousActiveTeam);
		if (this.battle.isTeamKO(opponentTeam)){
			this.battle.setState(BattleState.LOST, opponentTeam);
			this.battle.setState(BattleState.WON, previousActiveTeam);
			this.battle.setGameFinished(true);

		} else if (this.battle.isBugemonKO(opponentTeam)){
			this.battle.setState(BattleState.SWAPPING, opponentTeam);
			this.battle.setState(BattleState.WAITING, previousActiveTeam);
			if (previousActiveTeam == ParticipantLabel.TEAM_A){
				this.battle.getLogMsg().add(participantB.getActiveBugemon().getName() + " est KO!");
				if (participantA.getHpAfterFirstAction() == -1) {
					battle.setParticipantHpAfterFirstAction(participantA, participantA.getActiveBugemon().getHp());
					battle.setParticipantHpAfterFirstAction(participantB, 0);
				}
			}
			
		} else {
			return false;
		}
		return true;
	}

	/**
	 * Handles the end of the battle
	 */
	public void handleBattleEnd(){

		BattleParticipant winner = this.battle.getParticipantA();
		BattleParticipant loser = this.battle.getParticipantB();

		if (this.battle.getState(ParticipantLabel.TEAM_B) == BattleState.WON) {
			winner = this.battle.getParticipantB();
			loser = this.battle.getParticipantA();
		}

		List<Bugemon> bugemonsOfWinner = winner.getParticipatingBugemons();
		Team teamOfLoser = loser.getTeam();

		int gainedXP = this.battle.computeTotalXP(teamOfLoser);
		for (Bugemon b : bugemonsOfWinner){
			b.gainXp(gainedXP / bugemonsOfWinner.size());
		}

		if (this.battle.getMultiplayerBattle()) {
			Player winnerPlayer = winner.getPlayer();
			if (winnerPlayer.getUserId().isPresent()){
				try {
					this.battle.getaccountService().addPoints(winnerPlayer.getUserId().get(), 1);
				
				} catch (Exception e) {
					LOGGER.log(Level.WARNING, "Failed to add point at the end of the battle");
				}
			}
		}

	}
	
}
