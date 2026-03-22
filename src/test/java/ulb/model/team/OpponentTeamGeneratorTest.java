package ulb.model.team;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ulb.model.bugemon.Bugemon;
import ulb.model.type.Type;

import ulb.model.sample.SamplesLoader;

public class OpponentTeamGeneratorTest {

	@BeforeAll
	public static void load() throws Exception {
		SamplesLoader.load();
	}

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
		Team playerTeam = new Team();
		playerTeam.add(makeBugemon());
		playerTeam.add(makeBugemon());
		playerTeam.add(makeBugemon());

		Team opponentTeam = OpponentTeamGenerator.generateRandomOpponentTeam(playerTeam);

		assertEquals(playerTeam.size(), opponentTeam.size());
		assertTrue(opponentTeam.isValid());
	}

	@Test
	public void cannotGenerateOpponentForEmptyTeam() throws Exception {
		Team emptyTeam = new Team();
		assertThrows(IllegalArgumentException.class,
			() -> OpponentTeamGenerator.generateRandomOpponentTeam(emptyTeam));
	}
}

