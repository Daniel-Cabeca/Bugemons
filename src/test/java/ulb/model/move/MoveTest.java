package ulb.model.move;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import ulb.model.type.Type;

public class TestMove {
	private Move getMoveA() {
		return new Move("fouet_liane", "Fouet-Liane", Type.FLORA, "Inflige des dégâts et réduit légèrement la défense adverse.", 40);
	}

	private Move getMoveB() {
		return new Move("pollen_sournois", "Pollen Sournois", Type.FLORA, "Inflige des dégâts et réduit l'initiative adverse au prochain tour.", 35);
	}

	@Test
	public void verifyEqualsDifferent() {
		Move a = getMoveA();
		Move b = getMoveB();
		assertFalse(a.equals(b));
	}

	@Test
	public void verifyEqualsSameInstance() {
		Move a = getMoveA();
		assertTrue(a.equals(a));
	}

	@Test
	public void verifyEqualsNotMove() {
		Move a = getMoveA();
		String b = new String("fouet_liane");
		assertFalse(a.equals(b));
	}

	@Test
	public void verifyEqualsSame() {
		Move a = getMoveA();
		Move b = getMoveA();
		assertTrue(a.equals(b));
	}
}
