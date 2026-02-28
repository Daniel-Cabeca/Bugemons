package ulb.model.ability;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import ulb.model.type.Type;
import java.util.Iterator;

public class AbilitySetTest {
	private Ability getAbilityA() {
		return new Ability("fouet_liane", "Fouet-Liane", Type.FLORA, "Inflige des dégâts et réduit légèrement la défense adverse.", 40);
	}

	private Ability getAbilityB() {
		return new Ability("pollen_sournois", "Pollen Sournois", Type.FLORA, "Inflige des dégâts et réduit l'initiative adverse au prochain tour.", 35);
	}

	private Ability getAbilityC() {
		return new Ability("racines_vives", "Racines Vives", Type.FLORA, "Inflige des dégâts et augmente légèrement la défense du lanceur.", 35);
	}

	private Ability getAbilityD() {
		return new Ability("spore_collante", "Spore Collante", Type.FLORA, "Inflige des dégâts et diminue l'initiative adverse.", 30);
	}

	private Ability getAbilityE() {
		return new Ability("feuille_tranchante", "Feuille Tranchante", Type.FLORA, "Inflige des dégâts élevés.", 55);
	}

	private AbilitySet getAbilitySetA() {
		Ability abilityA = getAbilityA();
		Ability abilityB = getAbilityB();
		Ability abilityC = getAbilityC();

		return new AbilitySet(abilityA, abilityB, abilityC);
	}

	@Test
	public void verifyContainsTrue() {
		AbilitySet abilityset = getAbilitySetA();
		Ability a = getAbilityA();
		assertTrue(abilityset.contains(a));
	}

	@Test
	public void verifyContainsFalse() {
		AbilitySet abilityset = getAbilitySetA();
		Ability d = getAbilityD();
		assertFalse(abilityset.contains(d));
	}

	@Test
	public void verifyGetAbilityCorrect() {
		AbilitySet abilityset = getAbilitySetA();
		Ability a = getAbilityA();
		Ability b = getAbilityB();
		Ability c = getAbilityC();

		assertEquals(abilityset.getAbility(0), a);
		assertEquals(abilityset.getAbility(1), b);
		assertEquals(abilityset.getAbility(2), c);
	}

	@Test
	public void verifyGetAbilityThrows() {
		AbilitySet abilityset = getAbilitySetA();

		assertThrows(IndexOutOfBoundsException.class, () -> { abilityset.getAbility(-1); });
		assertThrows(IndexOutOfBoundsException.class, () -> { abilityset.getAbility(abilityset.size()); });
	}

	@Test
	public void verifySetAbilityCorrect() {
		AbilitySet abilityset = getAbilitySetA();
		Ability d = getAbilityD();

		abilityset.setAbility(0, d);
		assertEquals(abilityset.getAbility(0), d);
	}

	@Test
	public void verifySetAbilityThrows() {
		AbilitySet abilityset = getAbilitySetA();
		Ability d = getAbilityD();

		assertThrows(IndexOutOfBoundsException.class, () -> { abilityset.setAbility(-1, d); });
		assertThrows(IndexOutOfBoundsException.class, () -> { abilityset.setAbility(abilityset.size(), d); });
	}

	@Test
	public void verifyIterator() {
		Ability a = getAbilityA();
		Ability b = getAbilityB();
		Ability c = getAbilityC();
		AbilitySet abilityset = getAbilitySetA();

		Iterator<Ability> iterator = abilityset.iterator();

		assertTrue(iterator.hasNext());
		assertEquals(a, iterator.next());

		assertTrue(iterator.hasNext());
		assertEquals(b, iterator.next());

		assertTrue(iterator.hasNext());
		assertEquals(c, iterator.next());

		assertFalse(iterator.hasNext());
	}

	@Test
	public void verifySwapAbilityCorrect() {
		Ability a = getAbilityA();
		Ability d = getAbilityD();
		AbilitySet abilityset = getAbilitySetA();

		abilityset.swapAbility(d, a);

		assertFalse(abilityset.contains(a));
		assertTrue(abilityset.contains(d));
	}

	@Test
	public void verifySwapAbilityOldNotKnown() {
		Ability d = getAbilityD();
		Ability e = getAbilityE();
		AbilitySet abilityset = getAbilitySetA();

		assertThrows(IllegalArgumentException.class, () -> { abilityset.swapAbility(d, e); });
	}
}
