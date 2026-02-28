package ulb.model.ability;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import ulb.model.type.Type;

public class AbilitySetTest {
	private Ability getAbilityA() {
		return new Ability("fouet_liane", "Fouet-Liane", Type.FLORA, "Inflige des dégâts et réduit légèrement la défense adverse.", 40);
	}

	private Ability getAbilityB() {
		return new Ability("pollen_sournois", "Pollen Sournois", Type.FLORA, "Inflige des dégâts et réduit l'initiative adverse au prochain tour.", 35);
	}

	@Test
	public void verifyEmptySize() {
		AbilitySet abilityset = new AbilitySet();
		assertEquals(abilityset.size(), 0);
	}

	@Test
	public void verifyEmptyContains() {
		AbilitySet Abilityset = new AbilitySet();
		Ability a = getAbilityA();
		assertFalse(Abilityset.contains(a));
	}

	@Test
	public void verifyAddNew() {
		AbilitySet abilityset = new AbilitySet();
		Ability a = getAbilityA();
		abilityset.add(a);

		assertEquals(abilityset.size(), 1);
		assertTrue(abilityset.contains(a));
	}

	@Test
	public void verifyAddDuplicate() {
		AbilitySet abilityset = new AbilitySet();
		Ability a = getAbilityA();
		abilityset.add(a);
		abilityset.add(a);

		assertEquals(abilityset.size(), 1);
		assertTrue(abilityset.contains(a));
	}

	@Test
	public void verifyRemovePresent() {
		AbilitySet abilityset = new AbilitySet();
		Ability a = getAbilityA();
		abilityset.add(a);
		abilityset.remove(a);

		assertEquals(abilityset.size(), 0);
		assertFalse(abilityset.contains(a));
	}

	@Test
	public void verifyRemoveAbsent() {
		AbilitySet abilityset = new AbilitySet();
		Ability a = getAbilityA();
		Ability b = getAbilityB();
		abilityset.add(a);
		abilityset.remove(b);

		assertEquals(abilityset.size(), 1);
		assertTrue(abilityset.contains(a));
		assertFalse(abilityset.contains(b));
	}

	@Test
	public void verifyIterator() {
		AbilitySet abilityset = new AbilitySet();
		Ability a = getAbilityA();
		Ability b = getAbilityB();

		boolean foundA = false;
		boolean foundB = false;

		for(Ability ability: abilityset) {
			if (ability.equals(a)) {
				assertFalse(foundA);
				foundA = true;
			}
			else if (ability.equals(b)) {
				assertFalse(foundB);
				foundB = true;
			}
			else {
				assertTrue(false);
			}
		}
	}

	@Test
	public void verifySwapCorrect() {
		AbilitySet abilityset = new AbilitySet();
		Ability a = getAbilityA();
		Ability b = getAbilityB();

		abilityset.add(a);
		abilityset.swap(b, a);

		assertFalse(abilityset.contains(a));
		assertTrue(abilityset.contains(b));
	}

	@Test
	public void verifySwapOldNotKnown() {
		AbilitySet abilityset = new AbilitySet();
		Ability a = getAbilityA();
		Ability b = getAbilityB();

		assertThrows(IllegalArgumentException.class, () -> { abilityset.swap(b, a); });
	}
}
