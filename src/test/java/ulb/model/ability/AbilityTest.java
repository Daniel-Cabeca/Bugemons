package ulb.model.ability;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import ulb.model.type.Type;

public class AbilityTest {
	private Ability getAbilityA() {
		return new Ability("fouet_liane", "Fouet-Liane", Type.FLORA, "Inflige des dégâts et réduit légèrement la défense adverse.", 40);
	}

	private Ability getAbilityB() {
		return new Ability("pollen_sournois", "Pollen Sournois", Type.FLORA, "Inflige des dégâts et réduit l'initiative adverse au prochain tour.", 35);
	}

	@Test
	public void verifyEqualsDifferent() {
		Ability a = getAbilityA();
		Ability b = getAbilityB();
		assertFalse(a.equals(b));
	}

	@Test
	public void verifyEqualsSameInstance() {
		Ability a = getAbilityA();
		assertTrue(a.equals(a));
	}

	@Test
	public void verifyEqualsNotAbility() {
		Ability a = getAbilityA();
		String b = new String("fouet_liane");
		assertFalse(a.equals(b));
	}

	@Test
	public void verifyEqualsSame() {
		Ability a = getAbilityA();
		Ability b = getAbilityA();
		assertTrue(a.equals(b));
	}
}
