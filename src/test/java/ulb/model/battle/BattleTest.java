package ulb.model.battle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.BugemonSpecies;
import ulb.model.bugemon.Stats;
import ulb.model.team.Team;
import ulb.model.type.Type;
import java.util.List;
import ulb.model.Player;
import ulb.model.ability.AbilitySet;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.repository.mock.StartingInventoryMockRepository;
import ulb.repository.mock.ItemMockRepository;
import ulb.service.ItemService;

public class BattleTest {

	private Bugemon createBugemon(String name, Type type, int hp, int attack, int defense, int initiative){
		BugemonSpecies species = new BugemonSpecies(name, name, type, new Stats(hp, attack, defense, initiative), new AbilitySet(), "", false);
		return new Bugemon(species);
	}

	@Test
	public void initiaveReturnsAWhenAHasHigherInitiative() {
		ItemService itemService = new ItemService(new ItemMockRepository(), new StartingInventoryMockRepository());

		Bugemon fast = createBugemon("fast", Type.PYRO, 100, 10, 10, 20);
		Bugemon slow = createBugemon("slow", Type.AQUA, 100, 10, 10, 5);

		Team teamA = new Team(List.of(fast));
		Team teamB = new Team(List.of(slow));
		Player p = new Player(itemService);
		Battle battle = new Battle(teamA, teamB, p, new Player(itemService));

		assertSame(ParticipantLabel.TEAM_A, battle.getFirstTeamToPlay());
	}

	@Test
	public void initiaveReturnsBWhenBHasHigherInitiative() {
		ItemService itemService = new ItemService(new ItemMockRepository(), new StartingInventoryMockRepository());

		Bugemon slow = createBugemon("slow", Type.PYRO, 100, 10, 10, 5);
		Bugemon fast = createBugemon("fast", Type.AQUA, 100, 10, 10, 20);

		Team teamA = new Team(List.of(slow));
		Team teamB = new Team(List.of(fast));

		Player p = new Player(itemService);
		Battle battle = new Battle(teamA, teamB, p, new Player(itemService));

		assertSame(ParticipantLabel.TEAM_B, battle.getFirstTeamToPlay());
	}

	@Test
	public void initiaveIsRandomWhenInitiativesAreEqual() {
		ItemService itemService = new ItemService(new ItemMockRepository(), new StartingInventoryMockRepository());

		Bugemon sameA = createBugemon("sameA", Type.PYRO, 100, 10, 10, 10);
		Bugemon sameB = createBugemon("sameB", Type.AQUA, 100, 10, 10, 10);

		Team teamA = new Team(List.of(sameA));
		Team teamB = new Team(List.of(sameB));

		Player p = new Player(itemService);
		Battle battle = new Battle(teamA, teamB, p, new Player(itemService));

		boolean sawA = false;
		boolean sawB = false;

		for (int i = 0; i < 200; i++) {
			Battle.ParticipantLabel result = battle.getFirstTeamToPlay();
			if (result == ParticipantLabel.TEAM_A) {
				sawA = true;
			} else if (result == ParticipantLabel.TEAM_B) {
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
