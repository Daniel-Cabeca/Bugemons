package ulb.model.team;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ulb.model.bugemon.Bugemon;
import ulb.model.type.Type;

import ulb.repository.BugemonSpeciesRepository;
import ulb.repository.mock.BugemonSpeciesMockRepository;
import ulb.service.BugemonService;

public class OpponentTeamGeneratorTest {

	private Bugemon makeBugemon() {
		return new Bugemon(
			100,
			100,
			100,
			100
		);
	}

	@Test
	public void generateOpponentTeamSameSizeAsPlayerTeam() throws Exception {
		BugemonSpeciesRepository repository = new BugemonSpeciesMockRepository();
		BugemonService service = new BugemonService(repository);

		Team playerTeam = new Team();
		playerTeam.add(makeBugemon());
		playerTeam.add(makeBugemon());
		playerTeam.add(makeBugemon());

		Team opponentTeam = OpponentTeamGenerator.generateRandomOpponentTeam(playerTeam, service);

		assertEquals(playerTeam.size(), opponentTeam.size());
		assertTrue(opponentTeam.isValid());
	}

	@Test
	public void cannotGenerateOpponentForEmptyTeam() throws Exception {
		BugemonSpeciesRepository repository = new BugemonSpeciesMockRepository();
		BugemonService service = new BugemonService(repository);

		Team emptyTeam = new Team();
		assertThrows(IllegalArgumentException.class,
			() -> OpponentTeamGenerator.generateRandomOpponentTeam(emptyTeam, service));
	}
}

