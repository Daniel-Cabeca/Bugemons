package ulb.model.ability;

import org.junit.jupiter.api.Test;

import ulb.model.sample.AbilitySample;
import ulb.model.sample.BugemonSample;
import ulb.model.bugemon.Bugemon;

import static org.junit.jupiter.api.Assertions.*;

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

	@Test
	public void verifyApplyStatEffect(){
		Ability a = AbilitySample.getG();
		Bugemon b = BugemonSample.getA();
		a.applyEffect(b);
		assertEquals(b.getBaseStats().defense - 10, b.getDefense());
	}

	@Test
	public void verifyApplyHealEffect(){
		Ability a = AbilitySample.getF();
		Bugemon b = BugemonSample.getG();
		a.applyEffect(b);
		assertEquals(b.getBaseStats().hp, b.getHp());
	}
}
