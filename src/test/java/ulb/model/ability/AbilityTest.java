package ulb.model.ability;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import ulb.model.sample.AbilitySample;

public class AbilityTest {
	@Test
	public void verifyEqualsDifferent() {
		Ability a = AbilitySample.getA();
		Ability b = AbilitySample.getB();
		assertFalse(a.equals(b));
	}

	@Test
	public void verifyEqualsObject() {
		Ability a = AbilitySample.getA();
		assertFalse(a.equals(3));
	}

	@Test
	public void verifyEqualsSameInstance() {
		Ability a = AbilitySample.getA();
		assertTrue(a.equals(a));
	}

	@Test
	public void verifyEqualsNotAbility() {
		Ability a = AbilitySample.getB();
		String b = new String("fouet_liane");
		assertFalse(a.equals(b));
	}

	@Test
	public void verifyEqualsTrue() {
		Ability a = AbilitySample.getA();
		Ability b = AbilitySample.getA();
		assertTrue(a.equals(b));
	}
}
