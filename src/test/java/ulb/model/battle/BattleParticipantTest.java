package ulb.model.battle;

import org.junit.jupiter.api.Test;
import ulb.model.Player;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;
import ulb.repository.mock.BugemonSpeciesMockRepository;
import ulb.service.BugemonService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BattleParticipantTest {

	@Test
	public void setActiveBugemonAddsToParticipatingBugemons() throws Exception {
		BugemonSpeciesMockRepository repository = new BugemonSpeciesMockRepository();
		BugemonService service = new BugemonService(repository);

		Bugemon a = service.spawnBugemon("florachu");
		Bugemon b = service.spawnBugemon("pyricore");

		Team team = new Team(List.of(a, b));

		BattleParticipant participantTest = new BattleParticipant(new Player(), team);

		participantTest.setActiveBugemon(a);
		assertFalse(participantTest.getParticipatingBugemons().contains(b));
		assertNotSame(participantTest.getActiveBugemon(), b);

		participantTest.setActiveBugemon(b);
		assertTrue(participantTest.getParticipatingBugemons().contains(b));
		assertSame(participantTest.getActiveBugemon(), b);
	}

	@Test
	public void setActiveBugemonDoesntAddSameBugemonTwice() throws Exception {
		BugemonSpeciesMockRepository repository = new BugemonSpeciesMockRepository();
		BugemonService service = new BugemonService(repository);

		Bugemon a = service.spawnBugemon("florachu");
		Bugemon b = service.spawnBugemon("pyricore");

		Team team = new Team(List.of(a, b));

		BattleParticipant participantTest = new BattleParticipant(new Player(), team);

		participantTest.setActiveBugemon(a);
		participantTest.setActiveBugemon(b);
		participantTest.setActiveBugemon(a);

		int numberOfBugemonA = 0;
		for (Bugemon bugemon : participantTest.getParticipatingBugemons()) {
			if (bugemon == a) {
				++numberOfBugemonA;
			}
		}

		assertEquals(1, numberOfBugemonA);
	}
}
