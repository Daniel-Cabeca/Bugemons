package ulb.model.battle;

import ulb.exceptions.GameException;
import ulb.model.Player;
import ulb.model.team.Team;
import ulb.service.AccountService;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Holds data pertaining to a multiplayer battle between two players.
 */
public class MultiBattleSession {
	public record ParticipantPair(MultiBattleParticipant a, MultiBattleParticipant b) {}

	private final ParticipantPair participants;
	private Battle battle = null;

	public MultiBattleSession(Player player1, Player player2) {
		this.participants = new ParticipantPair(
				new MultiBattleParticipant(player1, Battle.ParticipantLabel.TEAM_A),
				new MultiBattleParticipant(player2, Battle.ParticipantLabel.TEAM_B)
		);
	}

	public Battle getBattle() { return this.battle; }
	public ParticipantPair getParticipants() { return this.participants; }

	/**
	 * Gets the MultiBattleParticipant instance corresponding to a given id.
	 *
	 * @param userId The user id
	 * @return The corresponding MultiBattleParticipant instance
	 * @throws NoSuchElementException If the id does not correspond to any of the participants' id
	 */
	public MultiBattleParticipant getParticipant(int userId) throws NoSuchElementException {
		if (userId == this.participants.a().getUserId()) {
			return this.participants.a();
		}
		else if (userId == this.participants.b().getUserId()) {
			return this.participants.b();
		}

		throw new NoSuchElementException("No corresponding participant was found in the multiplayer battle session for this id: "+ userId);
	}

	/**
	 * Whether the battle has been accepted by both players.
	 *
	 * @return True if it has been accepted by both, false otherwise
	 */
	public boolean isAccepted() {
		return this.participants.a().hasAccepted() && this.participants.b().hasAccepted();
	}

	/**
	 * Whether a participant has declined the battle.
	 *
	 * @return True if at least one participant has declined the battle, false otherwise
	 */
	public boolean isDeclined() {
		return this.participants.a().hasDeclined() || this.participants.b().hasDeclined();
	}

	/**
	 * Whether both players have picked their team and are ready for the battle.
	 *
	 * @return True if both players have picked a team, false otherwise
	 */
	public boolean isReady() {
		return this.participants.a().hasTeam() && this.participants.b().hasTeam();
	}

	/**
	 * Creates a Battle instance for the session. To be called once both players are ready.
	 */
	public void start(AccountService accountService) throws GameException{
		Optional<Team> teamA = this.participants.a().getTeam();
		Optional<Team> teamB = this.participants.b().getTeam();
		if (teamA.isEmpty() || teamB.isEmpty()){
			throw new GameException("all the players must have a team");
		}
		this.battle = new Battle(
			teamA.get(),
			teamB.get(),

			this.participants.a().getPlayer(),
			this.participants.b().getPlayer(),

			true,
			accountService
		);
	}
}
