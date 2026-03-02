package ulb.model.bugemon;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

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
}
