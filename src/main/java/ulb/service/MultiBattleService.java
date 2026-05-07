package ulb.service;

import ulb.model.battle.MultiBattleSession;
import ulb.repository.MultiBattleRepository;

import java.util.NoSuchElementException;

/**
 * Service in charge of multiplayer battle sessions.
 */
public class MultiBattleService {
	private MultiBattleRepository repository;

	public MultiBattleService(MultiBattleRepository repository) {
		this.repository = repository;
	}

	/**
	 * Creates a new multiplayer battle session.
	 *
	 * @param userId1 The id of one of the players
	 * @param userId2 The id of the other player
	 * @return The created multiplayer battle session
	 */
	public MultiBattleSession createMultiBattle(int userId1, int userId2) {
		MultiBattleSession battle = this.repository.create(userId1, userId2);
		return battle;
	}

	/**
	 * Fetches the multiplayer battle session corresponding to the given player ids.
	 *
	 * @param userId1 The id of one of the players
	 * @param userId2 The id of the other player
	 * @return The corresponding battle session
	 * @throws NoSuchElementException If no session exists for the given players
	 */
	public MultiBattleSession getMultiBattle(int userId1, int userId2) throws NoSuchElementException {
		return this.repository.findByIds(userId1, userId2);
	}
}
