package ulb.repository.inmemory;

import ulb.model.Player;
import ulb.model.battle.MultiBattleSession;
import ulb.repository.MultiBattleRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Non-persistent repository for multiplayer battle sessions.
 */
public class MultiBattleInMemoryRepository implements MultiBattleRepository {
	/**
	 * Pair of user ids.
	 * The ids must always be sorted before building the record.
	 *
	 * @param userId1 The lowest-value user id of the pair
	 * @param userId2 The highest-value user id of the pair
	 */
	private record SessionKey(int userId1, int userId2) {
	}

	/** Map associating session keys to their instance. */
	private final Map<SessionKey, MultiBattleSession> sessions = new HashMap<>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MultiBattleSession findByIds(int userId1, int userId2) throws NoSuchElementException {
		SessionKey key = makeSessionKey(userId1, userId2);
		MultiBattleSession session = this.sessions.get(key);

		if (session == null) {
			throw new NoSuchElementException("No matching MultiBattleSession found.");
		}

		return session;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MultiBattleSession create(Player player1, Player player2) {
		MultiBattleSession session = new MultiBattleSession(player1, player2);
		SessionKey key = makeSessionKey(session);

		this.sessions.put(key, session);
		return session;
	}

	/**
	 * Creates a key to be used in the internal map to refer to a multiplayer battle session.
	 *
	 * @param session The multiplayer battle session
	 * @return A record that holds the ordered pair of ids
	 */
	private static SessionKey makeSessionKey(MultiBattleSession session) {
		int userId1 = session.getParticipants().a().getUserId();
		int userId2 = session.getParticipants().b().getUserId();

		return makeSessionKey(userId1, userId2);
	}

	/**
	 * Creates a key to be used in the internal map to refer to a multiplayer battle session.
	 *
	 * @param userId1 The id of one of the players
	 * @param userId2 The id of the other player
	 * @return A record that holds the ordered pair of ids
	 */
	private static SessionKey makeSessionKey(int userId1, int userId2) {
		return new SessionKey(Math.min(userId1, userId2), Math.max(userId1, userId2));
	}
}
