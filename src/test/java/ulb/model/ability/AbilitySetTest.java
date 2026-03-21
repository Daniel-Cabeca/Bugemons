package ulb.model.ability;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Iterator;

import ulb.model.sample.AbilitySample;
import ulb.model.sample.AbilitySetSample;

public class AbilitySetTest {
	@Test
	public void containsIsTrueWhenAbilityInAbilitySet() {
		AbilitySet abilityset = AbilitySetSample.getABC();
		Ability a = AbilitySample.getA();
		assertTrue(abilityset.contains(a));
	}

	@Test
	public void containsIsFalseWhenAbilityNotInAbilitySet() {
		AbilitySet abilityset = AbilitySetSample.getABC();
		Ability d = AbilitySample.getD();
		assertFalse(abilityset.contains(d));
	}

	@Test
	public void getAbilityFunctionnality() {
		AbilitySet abilityset = AbilitySetSample.getABC();
		Ability a = AbilitySample.getA();
		Ability b = AbilitySample.getB();
		Ability c = AbilitySample.getC();

		assertEquals(abilityset.getAbility(0), a);
		assertEquals(abilityset.getAbility(1), b);
		assertEquals(abilityset.getAbility(2), c);
	}

	@Test
	public void getAbilityWhenIndexIsOutOfBoundThrowsException() {
		AbilitySet abilityset = AbilitySetSample.getABC();

		assertThrows(IndexOutOfBoundsException.class, () -> { abilityset.getAbility(-1); });
		assertThrows(IndexOutOfBoundsException.class, () -> { abilityset.getAbility(abilityset.size()); });
	}

	@Test
	public void setAbilityFunctionnality() {
		AbilitySet abilityset = AbilitySetSample.getABC();
		Ability d = AbilitySample.getD();

		abilityset.setAbility(0, d);
		assertEquals(abilityset.getAbility(0), d);
	}

	@Test
	public void setAbilityThrowsExceptionWhenIndexIsOutOfBound() {
		AbilitySet abilityset = AbilitySetSample.getABC();
		Ability d = AbilitySample.getD();

		assertThrows(IndexOutOfBoundsException.class, () -> { abilityset.setAbility(-1, d); });
		assertThrows(IndexOutOfBoundsException.class, () -> { abilityset.setAbility(abilityset.size(), d); });
	}

	@Test
	public void iteratorFunctionnality() {
		AbilitySet abilityset = AbilitySetSample.getABC();
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
	public void swapAbilityFunctionnality() {
		AbilitySet abilityset = AbilitySetSample.getABC();
		Ability a = AbilitySample.getA();
		Ability d = AbilitySample.getD();

		abilityset.swapAbility(d, a);

		assertFalse(abilityset.contains(a));
		assertTrue(abilityset.contains(d));
	}

	@Test
	public void swapAbilityWhenOldAbilityNotKnownThrowsException() {
		AbilitySet abilityset = AbilitySetSample.getABC();
		Ability d = AbilitySample.getD();
		Ability e = AbilitySample.getE();

		assertThrows(IllegalArgumentException.class, () -> { abilityset.swapAbility(d, e); });
	}

	@Test
	public void abilitySetsAreEqual() {
		AbilitySet a = AbilitySetSample.getABC();
		AbilitySet b = AbilitySetSample.getABC();
		assertTrue(a.equals(b));
	}

	@Test
	public void abilitySetsAreNotEqual() {
		AbilitySet base = AbilitySetSample.getABC();

		AbilitySet a = new AbilitySet(AbilitySample.getD(), AbilitySample.getB(), AbilitySample.getC());
		AbilitySet b = new AbilitySet(AbilitySample.getA(), AbilitySample.getD(), AbilitySample.getC());
		AbilitySet c = new AbilitySet(AbilitySample.getA(), AbilitySample.getB(), AbilitySample.getD());

		assertFalse(base.equals(a));
		assertFalse(base.equals(b));
		assertFalse(base.equals(c));
	}

	@Test
	public void abilitySetAndObjectAreNotEqual() {
		AbilitySet a = AbilitySetSample.getABC();
		assertFalse(a.equals(3));
	}

	@Test
	public void sameInstancesAreEqual() {
		AbilitySet a = AbilitySetSample.getABC();
		assertTrue(a.equals(a));
	}

}
