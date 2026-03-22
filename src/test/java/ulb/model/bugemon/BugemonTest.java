package ulb.model.bugemon;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ulb.model.sample.BugemonSample;
import ulb.model.type.Type;

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

		assertEquals(90, B.getFightStats().hp);
		assertEquals(11, B.getFightStats().attack);
		assertEquals(5, B.getFightStats().defense);
		assertEquals(4, B.getFightStats().initiative);

		B.removeStatsDebuffs();

		assertEquals(B.getBaseStats().hp, B.getFightStats().hp);
		assertEquals(B.getBaseStats().attack, B.getFightStats().attack);
		assertEquals(B.getBaseStats().defense, B.getFightStats().defense);
		assertEquals(B.getBaseStats().initiative, B.getFightStats().initiative);
	}

	@Test
	public void checkInitiative(){
		Bugemon A = BugemonSample.getA();
		Bugemon B = BugemonSample.getB();
		B.changeFightStats(new Stats(0, 0, 0, -1));

		assertTrue(A.checkInitiative(B));
	}

}
