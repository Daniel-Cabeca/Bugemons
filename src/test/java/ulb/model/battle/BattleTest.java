package ulb.model.battle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;
import ulb.model.type.Type;
import java.util.List;
import ulb.model.Player;
import ulb.model.battle.Battle.TeamLabel;

public class BattleTest {
	@Test
	public void initiaveReturnsAWhenAHasHigherInitiative() {
		Bugemon fast = new Bugemon("fast", Type.PYRO, 100, 10, 10, 20);
		Bugemon slow = new Bugemon("slow", Type.AQUA, 100, 10, 10, 5);

		Team teamA = new Team(List.of(fast));
		Team teamB = new Team(List.of(slow));
		Player p = new Player();
		Battle battle = new Battle(teamA, teamB, p);

		assertSame(TeamLabel.TEAM_A, battle.getFirstTeamToPlay());
	}

	@Test
	public void initiaveReturnsBWhenBHasHigherInitiative() {
		Bugemon slow = new Bugemon("slow", Type.PYRO, 100, 10, 10, 5);
		Bugemon fast = new Bugemon("fast", Type.AQUA, 100, 10, 10, 20);

		Team teamA = new Team(List.of(slow));
		Team teamB = new Team(List.of(fast));

		Player p = new Player();
		Battle battle = new Battle(teamA, teamB, p);

		assertSame(TeamLabel.TEAM_B, battle.getFirstTeamToPlay());
	}

	@Test
	public void initiaveIsRandomWhenInitiativesAreEqual() {
		Bugemon sameA = new Bugemon("sameA", Type.PYRO, 100, 10, 10, 10);
		Bugemon sameB = new Bugemon("sameB", Type.AQUA, 100, 10, 10, 10);

		Team teamA = new Team(List.of(sameA));
		Team teamB = new Team(List.of(sameB));

		Player p = new Player();
		Battle battle = new Battle(teamA, teamB, p);

		boolean sawA = false;
		boolean sawB = false;

		for (int i = 0; i < 200; i++) {
			Battle.TeamLabel result = battle.getFirstTeamToPlay();
			if (result == TeamLabel.TEAM_A) {
				sawA = true;
			} else if (result == TeamLabel.TEAM_B) {
				sawB = true;
			}
			if (sawA && sawB) {
				break;
			}
		}

		assertTrue(sawA, "Should sometimes pick A on tie");
		assertTrue(sawB, "Should sometimes pick B on tie");
	}
}
