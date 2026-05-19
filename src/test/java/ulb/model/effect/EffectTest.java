package ulb.model.effect;

import org.junit.jupiter.api.Test;
import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;
import ulb.model.Player;
import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;
import ulb.repository.mock.BugemonSpeciesMockRepository;
import ulb.service.BugemonService;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EffectTest {
	@Test
	public void getTargetsIsOwnBugemonWhenTargetIsOwnBugemon() throws Exception {
		Battle battle = getTestBattle();
		Effect effect = new EffectStatModifier(EffectTarget.OWN_BUGEMON, EffectStatDuration.PERMANENT, Map.of());

		ParticipantLabel team = ParticipantLabel.TEAM_A;
		List<Bugemon> targets = effect.getTargets(battle, team);

		assertEquals(1, targets.size());
		assertSame(battle.getActiveBugemon(team), targets.get(0));
	}

	private static Battle getTestBattle() throws LoadException, EntityNotFoundException {
		BugemonSpeciesMockRepository repository = new BugemonSpeciesMockRepository();
		BugemonService service = new BugemonService(repository);

		Team teamA = new Team(List.of(service.spawnBugemon("florachu"), service.spawnBugemon("pyricore")));
		Team teamB = new Team(List.of(service.spawnBugemon("rockachu"), service.spawnBugemon("obsidian")));

		Player player = new Player();
		Battle battle = new Battle(teamA, teamB, player, new Player());

		return battle;
	}

	@Test
	public void getTargetsIsActiveOpposingBugemonWhenTargetIsOpposingBugemon() throws Exception {
		Battle battle = getTestBattle();
		Effect effect = new EffectStatModifier(EffectTarget.OPPOSITE_BUGEMON, EffectStatDuration.PERMANENT, Map.of());

		ParticipantLabel team = ParticipantLabel.TEAM_A;
		List<Bugemon> targets = effect.getTargets(battle, team);

		assertEquals(1, targets.size());
		assertSame(battle.getActiveBugemon(battle.getOpponentTeamLabel(team)), targets.get(0));
	}

	@Test
	public void getTargetsIsOwnTeamWhenTargetIsOwnTeam() throws Exception {
		Battle battle = getTestBattle();
		Effect effect = new EffectStatModifier(EffectTarget.OWN_TEAM, EffectStatDuration.PERMANENT, Map.of());

		ParticipantLabel team = ParticipantLabel.TEAM_A;
		List<Bugemon> targets = effect.getTargets(battle, team);

		boolean foundFlorachu = false;
		boolean foundPyricore = false;

		for (Bugemon bugemon : targets) {
			if (!foundFlorachu && bugemon.getSpecies().getId().equals("florachu")) {
				foundFlorachu = true;
			} else if (!foundPyricore && bugemon.getSpecies().getId().equals("pyricore")) {
				foundPyricore = true;
			} else {
				assertTrue(false, "Found an unexpected Bugemon in the targets list.");
			}
		}

		assertTrue(foundFlorachu);
		assertTrue(foundPyricore);
	}
}
