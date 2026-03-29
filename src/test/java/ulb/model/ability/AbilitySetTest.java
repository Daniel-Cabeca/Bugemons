package ulb.model.ability;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;

import ulb.model.type.Type;

public class AbilitySetTest {
	private static Ability makeAbilityA() {
		return new Ability("a", "A", Type.FLORA, "Inflige des dégâts.", 40);
	}

	private static Ability makeAbilityB() {
		return new Ability("b", "B", Type.FLORA, "Inflige des dégâts.", 40);
	}

	private static Ability makeAbilityC() {
		return new Ability("c", "C", Type.FLORA, "Inflige des dégâts.", 40);
	}

	private static Ability makeAbilityD() {
		return new Ability("d", "D", Type.FLORA, "Inflige des dégâts.", 40);
	}

	private static Ability makeAbilityE() {
		return new Ability("e", "E", Type.FLORA, "Inflige des dégâts.", 40);
	}

	private static AbilitySet makeAbilitySetABC() {
		return new AbilitySet(makeAbilityA(), makeAbilityB(), makeAbilityC());
	}

	@Test
	public void containsIsTrueWhenAbilityInAbilitySet() {
		AbilitySet abilityset = makeAbilitySetABC();
		Ability a = makeAbilityA();
		assertTrue(abilityset.contains(a));
	}

	@Test
	public void containsIsFalseWhenAbilityNotInAbilitySet() {
		AbilitySet abilityset = makeAbilitySetABC();
		Ability d = makeAbilityD();
		assertFalse(abilityset.contains(d));
	}

	@Test
	public void getAbilityFunctionnality() {
		AbilitySet abilityset = makeAbilitySetABC();
		Ability a = makeAbilityA();
		Ability b = makeAbilityB();
		Ability c = makeAbilityC();

		assertEquals(abilityset.getAbility(0), a);
		assertEquals(abilityset.getAbility(1), b);
		assertEquals(abilityset.getAbility(2), c);
	}

	@Test
	public void getAbilityWhenIndexIsOutOfBoundThrowsException() {
		AbilitySet abilityset = makeAbilitySetABC();

		assertThrows(IndexOutOfBoundsException.class, () -> { abilityset.getAbility(-1); });
		assertThrows(IndexOutOfBoundsException.class, () -> { abilityset.getAbility(abilityset.size()); });
	}

	@Test
	public void setAbilityFunctionnality() {
		AbilitySet abilityset = makeAbilitySetABC();
		Ability d = makeAbilityD();

		abilityset.setAbility(0, d);
		assertEquals(abilityset.getAbility(0), d);
	}

	@Test
	public void setAbilityThrowsExceptionWhenIndexIsOutOfBound() {
		AbilitySet abilityset = makeAbilitySetABC();
		Ability d = makeAbilityD();

		assertThrows(IndexOutOfBoundsException.class, () -> { abilityset.setAbility(-1, d); });
		assertThrows(IndexOutOfBoundsException.class, () -> { abilityset.setAbility(abilityset.size(), d); });
	}

	@Test
	public void iteratorFunctionnality() {
		AbilitySet abilityset = makeAbilitySetABC();
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
		AbilitySet abilityset = makeAbilitySetABC();
		Ability a = makeAbilityA();
		Ability d = makeAbilityD();

		abilityset.swapAbility(d, a);

		assertFalse(abilityset.contains(a));
		assertTrue(abilityset.contains(d));
	}

	@Test
	public void swapAbilityWhenOldAbilityNotKnownThrowsException() {
		AbilitySet abilityset = makeAbilitySetABC();
		Ability d = makeAbilityD();
		Ability e = makeAbilityE();

		assertThrows(IllegalArgumentException.class, () -> { abilityset.swapAbility(d, e); });
	}

	@Test
	public void abilitySetsAreEqual() {
		AbilitySet a = makeAbilitySetABC();
		AbilitySet b = makeAbilitySetABC();
		assertTrue(a.equals(b));
	}

	@Test
	public void abilitySetsAreNotEqual() {
		AbilitySet base = makeAbilitySetABC();

		AbilitySet a = new AbilitySet(makeAbilityD(), makeAbilityB(), makeAbilityC());
		AbilitySet b = new AbilitySet(makeAbilityA(), makeAbilityD(), makeAbilityC());
		AbilitySet c = new AbilitySet(makeAbilityA(), makeAbilityB(), makeAbilityD());

		assertFalse(base.equals(a));
		assertFalse(base.equals(b));
		assertFalse(base.equals(c));
	}

	@Test
	public void abilitySetAndObjectAreNotEqual() {
		AbilitySet a = makeAbilitySetABC();
		assertFalse(a.equals(3));
	}

	@Test
	public void sameInstancesAreEqual() {
		AbilitySet a = makeAbilitySetABC();
		assertTrue(a.equals(a));
	}

}
