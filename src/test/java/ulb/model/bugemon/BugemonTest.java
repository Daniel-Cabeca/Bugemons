package ulb.model.bugemon;

import org.junit.jupiter.api.Test;
import ulb.model.ability.Ability;
import ulb.model.ability.AbilitySet;
import ulb.model.type.Type;
import ulb.repository.BugemonSpeciesRepository;
import ulb.repository.mock.BugemonSpeciesMockRepository;
import ulb.service.BugemonService;

import static org.junit.jupiter.api.Assertions.*;

public class BugemonTest {

	@Test
	public void swapWrongTypeAbility() {
		Bugemon B = createBugemon("A", Type.AQUA, 10, 29, 35, 16);
		Ability A = new Ability("test", "Test", Type.PYRO, "Test Description", 10);
		B.swapAbility(A, null);

		boolean contains = false;
		for (Ability ab : B.getAbilities()) {
			contains |= A.equals(ab);
		}
		assertFalse(contains);
	}

	private Bugemon createBugemon(String name, Type type, int hp, int attack, int defense, int initiative) {
		BugemonSpecies species = new BugemonSpecies(name, name, type, new Stats(hp, attack, defense, initiative),
				new AbilitySet(), "", false);
		return new Bugemon(species);
	}

	@Test
	public void xpGainsDoesNotAllowALevelUp() {
		Bugemon B = createBugemon("A", Type.AQUA, 10, 29, 35, 16);
		int levelGained = B.gainXp(49);
		assertEquals(1, B.getLevel());
		assertEquals(49, B.getXp());
		assertEquals(0, levelGained);
	}

	@Test
	public void xpGainsAllowALevelUp() {
		Bugemon B = createBugemon("A", Type.AQUA, 10, 29, 35, 16);
		int levelGained = B.gainXp(51);
		assertEquals(2, B.getLevel());
		assertEquals(1, B.getXp());
		assertEquals(1, levelGained);
	}

	@Test
	public void xpGainsAllowMultipleLevelUps() {
		Bugemon B = createBugemon("A", Type.AQUA, 10, 29, 35, 16);
		int levelGained = B.gainXp(300);
		assertEquals(4, B.getLevel());
		assertEquals(0, B.getXp());
		assertEquals(3, levelGained);
	}

	@Test
	public void resetFightStatsToBaseStats() {
		Stats s = new Stats(-10, -9, -5, -6); // debuff stats
		Bugemon B = createBugemon("A", Type.AQUA, 100, 20, 10, 10);

		B.changeFightStats(s);

		assertEquals(90, B.getFightStats().getHp());
		assertEquals(11, B.getFightStats().getAttack());
		assertEquals(5, B.getFightStats().getDefense());
		assertEquals(4, B.getFightStats().getInitiative());

		B.removeStatsDebuffs();

		assertEquals(B.getBaseStats().getHp(), B.getFightStats().getHp());
		assertEquals(B.getBaseStats().getAttack(), B.getFightStats().getAttack());
		assertEquals(B.getBaseStats().getDefense(), B.getFightStats().getDefense());
		assertEquals(B.getBaseStats().getInitiative(), B.getFightStats().getInitiative());
	}

	@Test
	public void checkInitiative() throws Exception {
		BugemonSpeciesRepository repository = new BugemonSpeciesMockRepository();
		BugemonService service = new BugemonService(repository);

		Bugemon a = service.spawnBugemon("florachu");
		Bugemon b = service.spawnBugemon("florachu");
		b.changeFightStats(new Stats(0, 0, 0, -1));

		assertTrue(a.checkInitiative(b));
	}

	@Test
	public void isKOReturnsFalseWhenHpIsPositive() {
		Bugemon b = createBugemon("A", Type.AQUA, 100, 10, 10, 10);
		assertFalse(b.isKO());
	}

	@Test
	public void isKOReturnsTrueWhenHpIsZero() {
		Bugemon b = createBugemon("A", Type.AQUA, 0, 10, 10, 10);
		assertTrue(b.isKO());
	}

	@Test
	public void hasHpDecreasedReturnsFalseAtFullHp() {
		Bugemon b = createBugemon("A", Type.AQUA, 100, 10, 10, 10);
		assertFalse(b.hasHPDecreased());
	}

	@Test
	public void hasHpDecreasedReturnsTrueAfterDamage() {
		Bugemon b = createBugemon("A", Type.AQUA, 100, 10, 10, 10);
		b.changeFightStats(new Stats(-30, 0, 0, 0));
		assertTrue(b.hasHPDecreased());
	}

	@Test
	public void changeFightStatsAppliesDelta() {
		Bugemon b = createBugemon("A", Type.AQUA, 100, 10, 10, 10);
		b.changeFightStats(new Stats(-20, 5, -3, 2));
		assertEquals(80, b.getFightStats().getHp());
		assertEquals(15, b.getFightStats().getAttack());
		assertEquals(7, b.getFightStats().getDefense());
		assertEquals(12, b.getFightStats().getInitiative());
	}

	@Test
	public void changeFightStatsDoesNotExceedBaseHp() {
		Bugemon b = createBugemon("A", Type.AQUA, 100, 10, 10, 10);
		b.changeFightStats(new Stats(999, 0, 0, 0)); // try to heal over cap
		assertEquals(100, b.getFightStats().getHp());
	}

	@Test
	public void consumeLevelRewardReturnsFalseWhenNoRewardsPending() {
		Bugemon b = createBugemon("A", Type.AQUA, 100, 10, 10, 10);
		assertFalse(b.consumeLevelReward(new Stats(5, 5, 5, 5)));
	}

	@Test
	public void consumeLevelRewardAppliesRewardWhenPending() {
		Bugemon b = createBugemon("A", Type.AQUA, 100, 10, 10, 10);
		b.gainXp(51); // makes the bugemon level up so it gets a reward

		assertTrue(b.consumeLevelReward(new Stats(10, 5, 5, 5)));
		assertEquals(110, b.getBaseStats().getHp());
		assertEquals(15, b.getBaseStats().getAttack());
	}

	@Test
	public void consumeLevelRewardDecrementsRemainingRewards() {
		Bugemon b = createBugemon("A", Type.AQUA, 100, 10, 10, 10);
		b.gainXp(51);
		b.consumeLevelReward(new Stats(5, 5, 5, 5));
		assertEquals(0, b.getRemainingReward());
	}

}
