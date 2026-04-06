package ulb.model.bugemon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class StatsTest {
	@Test
	public void positifStatsChange() {
		Stats stat1 = new Stats(10, 10, 10, 10);
		Stats stat2 = new Stats(10, 1, 5, 7);
		stat1.change(stat2);
		assertEquals(20, stat1.getHp());
		assertEquals(15, stat1.getDefense());
		assertEquals(11, stat1.getAttack());
		assertEquals(17, stat1.getInitiative());
	}

	@Test
	public void negativeStatsOverflowChange() {
		Stats stat1 = new Stats(10, 10, 10, 10);
		Stats stat2 = new Stats(-11, -20, -8, -20);
		stat1.change(stat2);
		assertEquals(0, stat1.getHp());
		assertEquals(2, stat1.getDefense());
		assertEquals(0, stat1.getAttack());
		assertEquals(0, stat1.getInitiative());
	}

	@Test
	public void statsAreEqual() {
		Stats a = new Stats(1, 1, 1, 1);
		Stats b = new Stats(1, 1, 1, 1);
		assertTrue(a.equals(b));
	}

	@Test
	public void sameInstancesAreEqual() {
		Stats a = new Stats(1, 1, 1, 1);
		assertTrue(a.equals(a));
	}

	@Test
	public void statsAndObjectAreNotEqual() {
		Stats a = new Stats(1, 1, 1, 1);
		assertFalse(a.equals(3));
	}

	@Test
	public void statsWithDifferentHpValuesAreNotEqual() {
		Stats a = new Stats(1, 1, 1, 1);
		Stats b = new Stats(2, 1, 1, 1);
		assertFalse(a.equals(b));
	}

	@Test
	public void statsWithDifferentAttackValuesAreNotEqual() {
		Stats a = new Stats(1, 1, 1, 1);
		Stats b = new Stats(1, 2, 1, 1);
		assertFalse(a.equals(b));
	}

	@Test
	public void statsWithDifferentDefenseValuesAreNotEqual() {
		Stats a = new Stats(1, 1, 1, 1);
		Stats b = new Stats(1, 1, 2, 1);
		assertFalse(a.equals(b));
	}

	@Test
	public void statsWithDifferentInitiativeValuesAreNotEqual() {
		Stats a = new Stats(1, 1, 1, 1);
		Stats b = new Stats(1, 1, 1, 2);
		assertFalse(a.equals(b));
	}
}
