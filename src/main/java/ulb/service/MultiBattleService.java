package ulb.service;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.EntityNotFoundException;
import ulb.model.Player;
import ulb.model.battle.MultiBattleSession;
import ulb.repository.MultiBattleRepository;

import java.util.NoSuchElementException;

/**
 * Service in charge of multiplayer battle sessions.
 */
public class MultiBattleService {
	/** Repository holding ongoing multiplayer battle sessions. */
	private final MultiBattleRepository repository;
	/** Repository holding user accounts. */
	private final AccountService accountService;

	public MultiBattleService(MultiBattleRepository repository, AccountService accountService) {
		this.repository = repository;
		this.accountService = accountService;
	}

	/**
	 * Creates a new multiplayer battle session.
	 *
	 * @param userId1 The id of one of the players
	 * @param userId2 The id of the other player
	 * @return The created multiplayer battle session
	 * @throws DataAccessException If some unexpected repository error occurs
	 */
	public MultiBattleSession createMultiBattle(int userId1, int userId2) throws EntityNotFoundException,
			DataAccessException {
		String username1 = this.accountService.getUsername(userId1);
		String username2 = this.accountService.getUsername(userId2);

		Player player1 = new Player(username1, userId1);
		Player player2 = new Player(username2, userId2);

		return this.createMultiBattle(player1, player2);
	}

	/**
	 * Creates a new multiplayer battle session.
	 *
	 * @param player1 One of the players
	 * @param player2 The other player
	 * @return The created multiplayer battle session
	 */
	public MultiBattleSession createMultiBattle(Player player1, Player player2) {
		MultiBattleSession battle = this.repository.create(player1, player2);
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
