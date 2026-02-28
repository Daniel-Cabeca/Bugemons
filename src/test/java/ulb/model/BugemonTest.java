package ulb.model;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Vector;
import java.util.List;

import ulb.model.type.Type;

public class TestBugemon {
	@Test
	public void xpBelowLevel() {
		Bugemon B = new Bugemon("A", Type.AQUA, 10, 29, 35, 16, 1);
		B.gainXP(49);
		assertEquals(1, B.getLevel());
		assertEquals(49, B.getXP());
	}

	@Test
	public void xpAbouveLevel() {
		Bugemon B = new Bugemon("A", Type.AQUA, 10, 29, 35, 16, 1);
		B.gainXP(51);
		assertEquals(2, B.getLevel());
		assertEquals(1, B.getXP());
	}

	@Test
	public void xpAboveMultipleLevels() {
		Bugemon B = new Bugemon("A", Type.AQUA, 10, 29, 35, 16, 1);
		B.gainXP(300);
		assertEquals(4, B.getLevel());
		assertEquals(0, B.getXP());
	}
}
