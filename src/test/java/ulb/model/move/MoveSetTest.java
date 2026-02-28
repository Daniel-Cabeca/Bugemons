package ulb.model.move;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import ulb.model.type.Type;

public class MoveSetTest {
	private Move getMoveA() {
		return new Move("fouet_liane", "Fouet-Liane", Type.FLORA, "Inflige des dégâts et réduit légèrement la défense adverse.", 40);
	}

	private Move getMoveB() {
		return new Move("pollen_sournois", "Pollen Sournois", Type.FLORA, "Inflige des dégâts et réduit l'initiative adverse au prochain tour.", 35);
	}

	@Test
	public void verifyEmptySize() {
		MoveSet moveset = new MoveSet();
		assertEquals(moveset.size(), 0);
	}

	@Test
	public void verifyEmptyContains() {
		MoveSet moveset = new MoveSet();
		Move a = getMoveA();
		assertFalse(moveset.contains(a));
	}

	@Test
	public void verifyAddNew() {
		MoveSet moveset = new MoveSet();
		Move a = getMoveA();
		moveset.add(a);

		assertEquals(moveset.size(), 1);
		assertTrue(moveset.contains(a));
	}

	@Test
	public void verifyAddDuplicate() {
		MoveSet moveset = new MoveSet();
		Move a = getMoveA();
		moveset.add(a);
		moveset.add(a);

		assertEquals(moveset.size(), 1);
		assertTrue(moveset.contains(a));
	}

	@Test
	public void verifyRemovePresent() {
		MoveSet moveset = new MoveSet();
		Move a = getMoveA();
		moveset.add(a);
		moveset.remove(a);

		assertEquals(moveset.size(), 0);
		assertFalse(moveset.contains(a));
	}

	@Test
	public void verifyRemoveAbsent() {
		MoveSet moveset = new MoveSet();
		Move a = getMoveA();
		Move b = getMoveB();
		moveset.add(a);
		moveset.remove(b);

		assertEquals(moveset.size(), 1);
		assertTrue(moveset.contains(a));
		assertFalse(moveset.contains(b));
	}

	@Test
	public void verifyIterator() {
		MoveSet moveset = new MoveSet();
		Move a = getMoveA();
		Move b = getMoveB();

		boolean foundA = false;
		boolean foundB = false;

		for(Move move: moveset) {
			if (move.equals(a)) {
				assertFalse(foundA);
				foundA = true;
			}
			else if (move.equals(b)) {
				assertFalse(foundB);
				foundB = true;
			}
			else {
				assertTrue(false);
			}
		}
	}
}
