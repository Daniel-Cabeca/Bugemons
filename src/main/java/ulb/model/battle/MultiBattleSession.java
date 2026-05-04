package ulb.model.battle;

import ulb.model.Player;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Holds data pertaining to a multiplayer battle between two players.
 */
public class MultiBattleSession {
	public record IdPair(int a, int b) {}
	private record ParticipantPair(MultiBattleParticipant a, MultiBattleParticipant b) {}

	private final ParticipantPair participants;
	private Battle battle = null;

	public MultiBattleSession(int userId1, int userId2) {
		IdPair ids = makeSortedIdPair(userId1, userId2);

		this.participants = new ParticipantPair(
				new MultiBattleParticipant(ids.a(), Battle.ParticipantLabel.TEAM_A),
				new MultiBattleParticipant(ids.b(), Battle.ParticipantLabel.TEAM_B)
		);
	}

	public Battle getBattle() { return this.battle; }

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
	public void start() {
		this.battle = new Battle(
			this.participants.a().getTeam(),
			this.participants.b().getTeam(),
			new Player("Player A", this.participants.a().getUserId()),
			new Player("Player B", this.participants.b().getUserId())
		);
	}

	/**
	 * Creates an ordered pair of ids.
	 *
	 * @param id1 An id
	 * @param id2 An id
	 * @return A sorted pair of ids
	*/
	public static IdPair makeSortedIdPair(int id1, int id2) {
		return new IdPair(Math.min(id1, id2), Math.max(id1, id2));
	}

	public IdPair getUserIds() { return new IdPair(this.participants.a().getUserId(), this.participants.b().getUserId()); }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o instanceof MultiBattleSession other) {
			return this.participants.a().getUserId() == other.participants.a().getUserId()
				&& this.participants.b().getUserId() == other.participants.b().getUserId();
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.participants.a().getUserId(), this.participants.b().getUserId());
	}
}
