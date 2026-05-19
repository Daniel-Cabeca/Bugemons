package ulb.repository.inmemory;

import org.junit.jupiter.api.Test;
import ulb.model.Player;
import ulb.model.battle.MultiBattleSession;
import ulb.repository.MultiBattleRepository;

import static org.junit.jupiter.api.Assertions.assertSame;

public class MultiBattleInMemoryRepositoryTest {
	@Test
	public void battleIsFindableAfterHavingBeenCreated() {
		MultiBattleRepository repository = new MultiBattleInMemoryRepository();

		MultiBattleSession session = repository.create(new Player("Player 1", 1), new Player("Player 2", 2));
		assertSame(session, repository.findByIds(1, 2));
	}

	@Test
	public void battleIsOverwrittenWhenCreateIsCalledWithSameIds() {
		MultiBattleRepository repository = new MultiBattleInMemoryRepository();

		repository.create(new Player("Player 1", 1), new Player("Player 2", 2));
		MultiBattleSession session2 = repository.create(new Player("Player 1", 1), new Player("Player 2", 2));

		assertSame(session2, repository.findByIds(1, 2));
	}

	@Test
	public void battleIsOverwrittenWhenCreateIsCalledWithSameIdsInDifferentOrder() {
		MultiBattleRepository repository = new MultiBattleInMemoryRepository();

		repository.create(new Player("Player 1", 1), new Player("Player 2", 2));
		MultiBattleSession session2 = repository.create(new Player("Player 2", 2), new Player("Player 1", 1));

		assertSame(session2, repository.findByIds(1, 2));
	}

	@Test
	public void findByIdsWorksWithSameOrderOfIds() {
		MultiBattleRepository repository = new MultiBattleInMemoryRepository();

		MultiBattleSession session = repository.create(new Player("Player 1", 1), new Player("Player 2", 2));
		assertSame(session, repository.findByIds(1, 2));
	}

	@Test
	public void findByIdsWorksWithDifferentOrderOfIds() {
		MultiBattleRepository repository = new MultiBattleInMemoryRepository();

		MultiBattleSession session = repository.create(new Player("Player 1", 1), new Player("Player 2", 2));
		assertSame(session, repository.findByIds(2, 1));
	}
}
