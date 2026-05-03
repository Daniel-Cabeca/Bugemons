package ulb.repository.inmemory;

import ulb.model.battle.MultiBattle;
import ulb.repository.MultiBattleRepository;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Non-persistent repository for multiplayer battle sessions.
 */
public class MultiBattleInMemoryRepository implements MultiBattleRepository {
	private final Set<MultiBattle> battles = new HashSet<>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MultiBattle findByIds(int userId1, int userId2) throws NoSuchElementException {
		MultiBattle.IdPair findIds = MultiBattle.makeSortedIdPair(userId1, userId2);

		for (MultiBattle battle: this.battles) {
			MultiBattle.IdPair battleIds = battle.getUserIds();

			if (findIds.a() == battleIds.a() && findIds.b() == battleIds.b())
				return battle;
		}

		throw new NoSuchElementException("No matching MultiBattle found.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MultiBattle create(int userId1, int userId2) {
		MultiBattle battle = new MultiBattle(userId1, userId2);

		this.battles.remove(battle); // remove if a battle with the same players exists
		this.battles.add(battle);

		return battle;
	}
}
