package ulb.model.team;

import org.junit.jupiter.api.Test;
import ulb.model.ability.AbilitySet;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.BugemonSpecies;
import ulb.model.bugemon.Stats;
import ulb.model.type.Type;
import ulb.repository.BugemonSpeciesRepository;
import ulb.repository.mock.BugemonSpeciesMockRepository;
import ulb.service.BugemonService;

import static org.junit.jupiter.api.Assertions.*;

public class OpponentTeamGeneratorTest {

	@Test
	public void generateOpponentTeamSameSizeAsPlayerTeam() throws Exception {
		BugemonSpeciesRepository repository = new BugemonSpeciesMockRepository();
		BugemonService service = new BugemonService(repository);

		Team playerTeam = new Team();
		playerTeam.add(createBugemon());
		playerTeam.add(createBugemon());
		playerTeam.add(createBugemon());

		Team opponentTeam = OpponentTeamGenerator.generateRandomOpponentTeam(playerTeam, service);

		assertEquals(playerTeam.size(), opponentTeam.size());
		assertTrue(opponentTeam.isValid());
	}

	private Bugemon createBugemon() {
		BugemonSpecies species = new BugemonSpecies("", "", Type.AQUA, new Stats(100, 100, 100, 100), new AbilitySet()
				, "", false);
		return new Bugemon(species);
	}

	@Test
	public void cannotGenerateOpponentForEmptyTeam() throws Exception {
		BugemonSpeciesRepository repository = new BugemonSpeciesMockRepository();
		BugemonService service = new BugemonService(repository);

		Team emptyTeam = new Team();
		assertThrows(IllegalArgumentException.class, () -> OpponentTeamGenerator.generateRandomOpponentTeam(emptyTeam,
				service));
	}
}

