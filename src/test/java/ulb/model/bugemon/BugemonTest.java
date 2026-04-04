package ulb.model.bugemon;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ulb.model.type.Type;
import ulb.repository.BugemonSpeciesRepository;
import ulb.repository.mock.BugemonSpeciesMockRepository;
import ulb.service.BugemonService;

public class BugemonTest {

	@Test
	public void xpGainsDoesNotAllowALevelUp() {
		Bugemon B = new Bugemon(Type.AQUA, 10, 29, 35, 16);
		int levelGained = B.gainXp(49);
		assertEquals(1, B.getLevel());
		assertEquals(49, B.getXp());
		assertEquals(0, levelGained);
	}

	@Test
	public void xpGainsAllowALevelUp() {
		Bugemon B = new Bugemon(Type.AQUA, 10, 29, 35, 16);
		int levelGained = B.gainXp(51);
		assertEquals(2, B.getLevel());
		assertEquals(1, B.getXp());
		assertEquals(1, levelGained);
	}

	@Test
	public void xpGainsAllowMultipleLevelUps() {
		Bugemon B = new Bugemon(Type.AQUA, 10, 29, 35, 16);
		int levelGained = B.gainXp(300);
		assertEquals(4, B.getLevel());
		assertEquals(0, B.getXp());
		assertEquals(3, levelGained);
	}

	@Test
	public void resetFightStatsToBaseStats(){
		Stats s = new Stats(-10,-9, -5, -6); // debuff stats
		Bugemon B = new Bugemon(Type.AQUA, 100, 20, 10, 10);

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
	public void checkInitiative(){
		BugemonSpeciesRepository repository = new BugemonSpeciesMockRepository();
		BugemonService service = new BugemonService(repository);

		Bugemon a = service.spawnBugemon("florachu");
		Bugemon b = service.spawnBugemon("florachu");
		b.changeFightStats(new Stats(0, 0, 0, -1));

		assertTrue(a.checkInitiative(b));
	}

}
