package ulb.model.ability;

import org.junit.jupiter.api.Test;

import ulb.model.sample.AbilitySample;
import ulb.model.sample.BugemonSample;
import ulb.model.bugemon.Bugemon;

import static org.junit.jupiter.api.Assertions.*;

public class AbilityTest {
	@Test
	public void abilitiesAreEqual() {
		Ability a = AbilitySample.getA();
		Ability b = AbilitySample.getA();
		assertTrue(a.equals(b));
	}

	@Test
	public void abilitiesAreNotEqual() {
		Ability a = AbilitySample.getA();
		Ability b = AbilitySample.getB();
		assertFalse(a.equals(b));
	}

	@Test
	public void abilityAndObjectAreNotEqual() {
		Ability a = AbilitySample.getA();
		assertFalse(a.equals(3));
	}

	@Test
	public void sameInstancesAreEqual() {
		Ability a = AbilitySample.getA();
		assertTrue(a.equals(a));
	}

	@Test
	public void abilityAndStringAreNotEqual() {
		Ability a = AbilitySample.getB();
		String b = new String("fouet_liane");
		assertFalse(a.equals(b));
	}

}
