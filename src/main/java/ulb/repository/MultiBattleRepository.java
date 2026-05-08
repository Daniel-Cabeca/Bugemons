package ulb.repository;

import ulb.model.Player;
import ulb.model.battle.MultiBattleSession;

import java.util.NoSuchElementException;

/**
 * Repository for multiplayer battle sessions.
 */
public interface MultiBattleRepository {
	/**
	 * Finds a MultiBattleSession instance corresponding to the given ids.
	 * Order of the ids does not matter.
	 *
	 * @param userId1 The id of a participant
	 * @param userId2 The id of the other participant
	 * @return A corresponding MultiBattleSession instance
	 * @throws NoSuchElementException If no corresponding MultiBattleSession was found
	 */
	public MultiBattleSession findByIds(int userId1, int userId2) throws NoSuchElementException;

	/**
	 * Creates a multiplayer battle session for the specific players.
	 * If a session already exists for those players, deletes it and replaces it with a new one.
	 *
	 * @param player1 One of the players
	 * @param player2 The other player
	 * @return The created battle session
	 */
	public MultiBattleSession create(Player player1, Player player2);
}
