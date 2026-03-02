package ulb.model.battle;

import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import ulb.model.Bugemon;
import ulb.model.team.Team;
import ulb.model.type.Type;
import java.util.List;

public class BattleTest {
	@Test
	public void checkInitiaveReturnsAWhenAHasHigherInitiative() {
		Bugemon fast = new Bugemon("fast", Type.PYRO, 100, 10, 10, 20, 1);
		Bugemon slow = new Bugemon("slow", Type.AQUA, 100, 10, 10, 5, 1);

		Team teamA = new Team(List.of(fast));
		Team teamB = new Team(List.of(slow));

		Battle battle = new Battle(teamA, teamB);

		assertSame(fast, battle.CheckInitiave());
	}

	@Test
	public void checkInitiaveReturnsBWhenBHasHigherInitiative() {
		Bugemon slow = new Bugemon("slow", Type.PYRO, 100, 10, 10, 5, 1);
		Bugemon fast = new Bugemon("fast", Type.AQUA, 100, 10, 10, 20, 1);

		Team teamA = new Team(List.of(slow));
		Team teamB = new Team(List.of(fast));

		Battle battle = new Battle(teamA, teamB);

		assertSame(fast, battle.CheckInitiave());
	}

	@Test
	public void checkInitiaveIsRandomWhenInitiativeEqual() {
		Bugemon sameA = new Bugemon("sameA", Type.PYRO, 100, 10, 10, 10, 1);
		Bugemon sameB = new Bugemon("sameB", Type.AQUA, 100, 10, 10, 10, 1);

		Team teamA = new Team(List.of(sameA));
		Team teamB = new Team(List.of(sameB));

		Battle battle = new Battle(teamA, teamB);

		boolean sawA = false;
		boolean sawB = false;

		for (int i = 0; i < 200; i++) {
			Bugemon result = battle.CheckInitiave();
			if (result == sameA) {
				sawA = true;
			} else if (result == sameB) {
				sawB = true;
			}
			if (sawA && sawB) {
				break;
			}
		}

		assertTrue("Should sometimes pick A on tie", sawA);
		assertTrue("Should sometimes pick B on tie", sawB);
	}
}
