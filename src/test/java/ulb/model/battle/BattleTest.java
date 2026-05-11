package ulb.model.battle;

import org.junit.jupiter.api.Test;

import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.BugemonSpecies;
import ulb.model.bugemon.Stats;
import ulb.model.effect.*;
import ulb.model.item.Item;
import ulb.model.team.Team;
import ulb.model.type.Type;
import java.util.List;
import ulb.model.Player;
import ulb.model.ability.AbilitySet;
import ulb.model.battle.Battle.ParticipantLabel;

import static org.junit.jupiter.api.Assertions.*;

public class BattleTest {

	private Bugemon createBugemon(String name, Type type, int hp, int attack, int defense, int initiative){
		BugemonSpecies species = new BugemonSpecies(name, name, type, new Stats(hp, attack, defense, initiative), new AbilitySet(), "", false);
		return new Bugemon(species);
	}

	private Battle setupBattle() {
		Bugemon a = createBugemon("a", Type.PYRO, 100, 10, 10, 20);
		Bugemon b = createBugemon("b", Type.AQUA, 100, 10, 10, 5);

		Team teamA = new Team(List.of(a));
		Team teamB = new Team(List.of(b));
		Player p = new Player();
		return new Battle(teamA, teamB, p, new Player());
	}

	private Item createHealItem() {
		EffectHeal healEffect = new EffectHeal(EffectTarget.OWN_BUGEMON, 20);
		return new Item("heal", "Heal", "Heals the bugemon", "heal", healEffect, "sprite");
	}

	@Test
	public void initiaveReturnsAWhenAHasHigherInitiative() {
		Bugemon fast = createBugemon("fast", Type.PYRO, 100, 10, 10, 20);
		Bugemon slow = createBugemon("slow", Type.AQUA, 100, 10, 10, 5);

		Team teamA = new Team(List.of(fast));
		Team teamB = new Team(List.of(slow));
		Player p = new Player();
		Battle battle = new Battle(teamA, teamB, p, new Player());

		assertSame(ParticipantLabel.TEAM_A, battle.getFirstTeamToPlay());
	}

	@Test
	public void initiaveReturnsBWhenBHasHigherInitiative() {
		Bugemon slow = createBugemon("slow", Type.PYRO, 100, 10, 10, 5);
		Bugemon fast = createBugemon("fast", Type.AQUA, 100, 10, 10, 20);

		Team teamA = new Team(List.of(slow));
		Team teamB = new Team(List.of(fast));

		Player p = new Player();
		Battle battle = new Battle(teamA, teamB, p, new Player());

		assertSame(ParticipantLabel.TEAM_B, battle.getFirstTeamToPlay());
	}

	@Test
	public void initiaveIsRandomWhenInitiativesAreEqual() {
		Bugemon sameA = createBugemon("sameA", Type.PYRO, 100, 10, 10, 10);
		Bugemon sameB = createBugemon("sameB", Type.AQUA, 100, 10, 10, 10);

		Team teamA = new Team(List.of(sameA));
		Team teamB = new Team(List.of(sameB));

		Player p = new Player();
		Battle battle = new Battle(teamA, teamB, p, new Player());

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

	@Test
	public void forfeitSetsGameFinished() {
		Battle battle = setupBattle();
		battle.forfeit(ParticipantLabel.TEAM_A);
		assertTrue(battle.isGameFinished());
	}

	@Test
	public void forfeitSetsGameStateLost() {
		Battle battle = setupBattle();

		battle.forfeit(ParticipantLabel.TEAM_A);
		assertSame(BattleState.LOST, battle.getState(ParticipantLabel.TEAM_A));
	}

	@Test
	public void forfeitSetsGameStateWon() {
		Battle battle = setupBattle();

		battle.forfeit(ParticipantLabel.TEAM_A);
		assertSame(BattleState.WON, battle.getState(ParticipantLabel.TEAM_B));
	}

	@Test
	public void computeTotalXPIsCorrectForNormalBattle() {
		Battle battle = setupBattle();
		Team teamB = battle.getTeam(ParticipantLabel.TEAM_B);
		// XP_COEF * floor number * boss multiplicator * losing team size
		int expected = 30 * 1 * 1 * teamB.size();
		assertEquals(expected, battle.computeTotalXP(teamB));
	}

	@Test
	public void computeTotalXPIsDoubledForBossBattle() {
		Battle battle = setupBattle();
		Team teamB = battle.getTeam(ParticipantLabel.TEAM_B);
		battle.enableBossBattle();
		int expected = 30 * 1 * 2 * teamB.size();
		assertEquals(expected, battle.computeTotalXP(teamB));
	}

	@Test
	public void computeTotalXPScalesWithFloorNumber() {
		Battle battle = setupBattle();
		Team teamB = battle.getTeam(ParticipantLabel.TEAM_B);
		battle.setFloorNumber(3);
		int expected = 30 * 3 * 1 * teamB.size();
		assertEquals(expected, battle.computeTotalXP(teamB));
	}

	@Test
	public void checkSwappableBugemonReturnsTrueForAliveBugemon() {
		Bugemon bugemonA = createBugemon("A", Type.PYRO, 100, 10, 10, 10);
		Bugemon alive = createBugemon("alive", Type.PYRO, 100, 10, 10, 10);
		Team teamA = new Team(List.of(bugemonA, alive));
		Team teamB = new Team(List.of(bugemonA, alive));
		Battle battle = new Battle(teamA, teamB, new Player(), new Player());
		assertTrue(battle.checkSwappableBugemon(alive, ParticipantLabel.TEAM_A));
	}

	@Test
	public void checkSwappableBugemonReturnsFalseForKOBugemon() {
		Bugemon bugemonA = createBugemon("A", Type.PYRO, 100, 10, 10, 10);
		Bugemon ko = createBugemon("ko", Type.PYRO, 0, 10, 10, 10);
		Team teamA = new Team(List.of(bugemonA, ko));
		Team teamB = new Team(List.of(bugemonA, ko));
		Battle battle = new Battle(teamA, teamB, new Player(), new Player());
		assertFalse(battle.checkSwappableBugemon(ko, ParticipantLabel.TEAM_A));
	}

	@Test
	public void performSwapSetsCorrectBugemonAsActive() {
		Bugemon bugemonA = createBugemon("A", Type.PYRO, 100, 10, 10, 10);
		Bugemon alive = createBugemon("alive", Type.PYRO, 100, 10, 10, 10);
		Team teamA = new Team(List.of(bugemonA, alive));
		Team teamB = new Team(List.of(bugemonA, alive));
		Battle battle = new Battle(teamA, teamB, new Player(), new Player());
		battle.performSwap(alive, ParticipantLabel.TEAM_A);
		assertSame(alive, battle.getActiveBugemon(ParticipantLabel.TEAM_A));
	}

	@Test
	public void checkItemReturnsFalseForHealItemWhenBugemonIsAtFullHp() {
		Battle battle = setupBattle();
		Item healItem = createHealItem();
		assertFalse(battle.checkItem(healItem, ParticipantLabel.TEAM_A));
	}

	@Test
	public void checkItemReturnsTrueForHealItemWhenBugemonHasLostHp() {
		Battle battle = setupBattle();
		battle.getActiveBugemon(ParticipantLabel.TEAM_A).changeFightStats(new Stats(-30, 0, 0, 0));
		Item healItem = createHealItem();
		assertTrue(battle.checkItem(healItem, ParticipantLabel.TEAM_A));
	}

	@Test
	public void checkItemReturnsTrueForNonHealItem() {
		Battle battle = setupBattle();
		battle.getActiveBugemon(ParticipantLabel.TEAM_A).changeFightStats(new Stats(-30, 0, 0, 0));
		Effect nonHealEffect = new EffectResetMalus(EffectTarget.OWN_BUGEMON);
		Item nonHealItem = new Item("other", "Other", "Some other item", "other", nonHealEffect, "sprite");
		assertTrue(battle.checkItem(nonHealItem, ParticipantLabel.TEAM_A));
	}

	@Test
	public void addedLogMessageAppearsInGetLogMsg() {
		Battle battle = setupBattle();
		battle.addLogMsg("hello");
		assertTrue(battle.getLogMsg().contains("hello"));
	}

	@Test
	public void multipleLogMessagesAreInTheCorrectOrder() {
		Battle battle = setupBattle();

		battle.addLogMsg("first");
		battle.addLogMsg("second");
		battle.addLogMsg("third");

		List<String> logs = battle.getLogMsg();
		assertEquals("first", logs.get(0));
		assertEquals("second", logs.get(1));
		assertEquals("third", logs.get(2));
	}
}
