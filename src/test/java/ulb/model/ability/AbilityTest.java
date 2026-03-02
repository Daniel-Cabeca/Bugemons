package ulb.model.ability;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import ulb.model.sample.AbilitySample;

public class AbilityTest {
	@Test
	public void verifyEqualsDifferent() {
		Ability a = AbilitySample.getFouetLiane();
		Ability b = AbilitySample.getPollenSournois();
		assertFalse(a.equals(b));
	}

	@Test
	public void verifyEqualsSameInstance() {
		Ability a = AbilitySample.getFouetLiane();
		assertTrue(a.equals(a));
	}

	@Test
	public void verifyEqualsNotAbility() {
		Ability a = AbilitySample.getFouetLiane();
		String b = new String("fouet_liane");
		assertFalse(a.equals(b));
	}

	@Test
	public void verifyEqualsSame() {
		Ability a = AbilitySample.getFouetLiane();
		Ability b = AbilitySample.getFouetLiane();
		assertTrue(a.equals(b));
	}
}
