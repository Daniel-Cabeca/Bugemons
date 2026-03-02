package ulb.model.team;

import org.junit.Test;
import org.junit.BeforeClass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import ulb.model.Loader;
import ulb.model.bugemon.Bugemon;
import ulb.model.type.Type;

public class OpponentTeamGeneratorTest {

	@BeforeClass
	public static void load() throws Exception {
		Loader.load();
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
	public void generatedOpponentTeamSameSizeAsPlayerTeam() throws Exception {
		Team playerTeam = new Team();
		playerTeam.add(makeBugemon());
		playerTeam.add(makeBugemon());
		playerTeam.add(makeBugemon());

		Team opponentTeam = OpponentTeamGenerator.generateRandomOpponentTeam(playerTeam);

		assertEquals(playerTeam.size(), opponentTeam.size());
		assertTrue(opponentTeam.isValid());
	}

	@Test(expected = IllegalArgumentException.class)
	public void cannotGenerateOpponentForEmptyTeam() throws Exception {
		Team emptyTeam = new Team();
		OpponentTeamGenerator.generateRandomOpponentTeam(emptyTeam);
	}
}

