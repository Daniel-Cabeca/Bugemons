package ulb.repository.inmemory;

import ulb.model.battle.MultiBattleSession;
import ulb.repository.MultiBattleRepository;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Non-persistent repository for multiplayer battle sessions.
 */
public class MultiBattleInMemoryRepository implements MultiBattleRepository {
	private final Set<MultiBattleSession> battles = new HashSet<>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MultiBattleSession findByIds(int userId1, int userId2) throws NoSuchElementException {
		MultiBattleSession.IdPair findIds = MultiBattleSession.makeSortedIdPair(userId1, userId2);

		for (MultiBattleSession battle: this.battles) {
			MultiBattleSession.IdPair battleIds = battle.getUserIds();

			if (findIds.a() == battleIds.a() && findIds.b() == battleIds.b())
				return battle;
		}

		throw new NoSuchElementException("No matching MultiBattleSession found.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MultiBattleSession create(int userId1, int userId2) {
		MultiBattleSession battle = new MultiBattleSession(userId1, userId2);

		this.battles.remove(battle); // remove if a battle with the same players exists
		this.battles.add(battle);

		return battle;
	}
}
