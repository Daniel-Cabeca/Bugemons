package ulb.model.ability;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import java.util.Iterator;

import ulb.model.sample.AbilitySample;
import ulb.model.sample.AbilitySetSample;

public class AbilitySetTest {
	@Test
	public void verifyContainsTrue() {
		AbilitySet abilityset = AbilitySetSample.getFlorachu();
		Ability a = AbilitySample.getFouetLiane();
		assertTrue(abilityset.contains(a));
	}

	@Test
	public void verifyContainsFalse() {
		AbilitySet abilityset = AbilitySetSample.getFlorachu();
		Ability d = AbilitySample.getExplosionArdente();
		assertFalse(abilityset.contains(d));
	}

	@Test
	public void verifyGetAbilityCorrect() {
		AbilitySet abilityset = AbilitySetSample.getFlorachu();
		Ability a = AbilitySample.getFouetLiane();
		Ability b = AbilitySample.getPollenSournois();
		Ability c = AbilitySample.getRacinesVives();

		assertEquals(abilityset.getAbility(0), a);
		assertEquals(abilityset.getAbility(1), b);
		assertEquals(abilityset.getAbility(2), c);
	}

	@Test
	public void verifyGetAbilityThrows() {
		AbilitySet abilityset = AbilitySetSample.getFlorachu();

		assertThrows(IndexOutOfBoundsException.class, () -> { abilityset.getAbility(-1); });
		assertThrows(IndexOutOfBoundsException.class, () -> { abilityset.getAbility(abilityset.size()); });
	}

	@Test
	public void verifySetAbilityCorrect() {
		AbilitySet abilityset = AbilitySetSample.getFlorachu();
		Ability d = AbilitySample.getExplosionArdente();

		abilityset.setAbility(0, d);
		assertEquals(abilityset.getAbility(0), d);
	}

	@Test
	public void verifySetAbilityThrows() {
		AbilitySet abilityset = AbilitySetSample.getFlorachu();
		Ability d = AbilitySample.getExplosionArdente();

		assertThrows(IndexOutOfBoundsException.class, () -> { abilityset.setAbility(-1, d); });
		assertThrows(IndexOutOfBoundsException.class, () -> { abilityset.setAbility(abilityset.size(), d); });
	}

	@Test
	public void verifyIterator() {
		AbilitySet abilityset = AbilitySetSample.getFlorachu();
		Iterator<Ability> iterator = abilityset.iterator();

		assertTrue(iterator.hasNext());
		assertSame(abilityset.getAbility(0), iterator.next());

		assertTrue(iterator.hasNext());
		assertSame(abilityset.getAbility(1), iterator.next());

		assertTrue(iterator.hasNext());
		assertSame(abilityset.getAbility(2), iterator.next());

		assertFalse(iterator.hasNext());
	}

	@Test
	public void verifySwapAbilityCorrect() {
		AbilitySet abilityset = AbilitySetSample.getFlorachu();
		Ability a = AbilitySample.getFouetLiane();
		Ability d = AbilitySample.getExplosionArdente();

		abilityset.swapAbility(d, a);

		assertFalse(abilityset.contains(a));
		assertTrue(abilityset.contains(d));
	}

	@Test
	public void verifySwapAbilityOldNotKnown() {
		AbilitySet abilityset = AbilitySetSample.getFlorachu();
		Ability d = AbilitySample.getExplosionArdente();
		Ability e = AbilitySample.getFlammesFolles();

		assertThrows(IllegalArgumentException.class, () -> { abilityset.swapAbility(d, e); });
	}
}
