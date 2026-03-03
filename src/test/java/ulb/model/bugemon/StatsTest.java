package ulb.model.bugemon;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;



public class StatsTest {
	@Test
	public void testPositifChange() {
		Stats stat1 = new Stats(10, 10, 10, 10);
		Stats stat2 = new Stats(10, 1, 5, 7);
		stat1.change(stat2);
		assertEquals(20, stat1.hp);
		assertEquals(15, stat1.defense);
		assertEquals(11, stat1.attack);
		assertEquals(17, stat1.initiative);
	}

	@Test
	public void testOverflowChange() {
		Stats stat1 = new Stats(10, 10, 10, 10);
		Stats stat2 = new Stats(-11, -20, -8, -20);
		stat1.change(stat2);
		assertEquals(0, stat1.hp);
		assertEquals(2, stat1.defense);
		assertEquals(0, stat1.attack);
		assertEquals(0, stat1.initiative);
	}

	@Test
	public void testEqualsTrue() {
		Stats a = new Stats(1, 1, 1, 1);
		Stats b = new Stats(1, 1, 1, 1);
		assertTrue(a.equals(b));
	}

	@Test
	public void verifyEqualsSameInstance() {
		Stats a = new Stats(1, 1, 1, 1);
		assertTrue(a.equals(a));
	}

	@Test
	public void testEqualsObject() {
		Stats a = new Stats(1, 1, 1, 1);
		assertFalse(a.equals(3));
	}

	@Test
	public void testEqualsDifferentHp() {
		Stats a = new Stats(1, 1, 1, 1);
		Stats b = new Stats(2, 1, 1, 1);
		assertFalse(a.equals(b));
	}

	@Test
	public void testEqualsDifferentAttack() {
		Stats a = new Stats(1, 1, 1, 1);
		Stats b = new Stats(1, 2, 1, 1);
		assertFalse(a.equals(b));
	}

	@Test
	public void testEqualsDifferentDefense() {
		Stats a = new Stats(1, 1, 1, 1);
		Stats b = new Stats(1, 1, 2, 1);
		assertFalse(a.equals(b));
	}

	@Test
	public void testEqualsDifferentInitiative() {
		Stats a = new Stats(1, 1, 1, 1);
		Stats b = new Stats(1, 1, 1, 2);
		assertFalse(a.equals(b));
	}
}
