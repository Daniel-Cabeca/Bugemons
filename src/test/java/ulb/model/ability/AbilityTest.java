package ulb.model.ability;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.Math;
import java.util.Random;

import ulb.utils.RandomTestUtils;

import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.BugemonSpecies;
import ulb.model.type.Type;
import ulb.model.bugemon.Stats;
import ulb.model.ability.Ability;
import ulb.model.ability.AbilitySet;

public class AbilityTest {
	private Ability createAbilityA() {
		return new Ability("a", "A", Type.FLORA, "Ability a", 10);
	}

	private Ability createAbilityB() {
		return new Ability("b", "B", Type.FLORA, "Ability b", 10);
	}

	private Ability createAbilityC() {
		return new Ability("c", "C", Type.FLORA, "Ability c", 10);
	}

	@Test
	public void abilitiesAreEqual() {
		Ability a = createAbilityA();
		Ability abis = createAbilityA();
		assertTrue(a.equals(abis));
	}

	@Test
	public void abilitiesAreNotEqual() {
		Ability a = createAbilityA();
		Ability b = createAbilityB();
		assertFalse(a.equals(b));
	}

	@Test
	public void abilityAndObjectAreNotEqual() {
		Ability a = createAbilityA();
		assertFalse(a.equals(3));
	}

	@Test
	public void sameInstancesAreEqual() {
		Ability a = createAbilityA();
		assertTrue(a.equals(a));
	}

	@Test
	public void abilityAndStringAreNotEqual() {
		Ability a = createAbilityA();
		String b = new String("fouet_liane");
		assertFalse(a.equals(b));
	}

	@Test
	public void correctDamageFormulaNoCrit() {
		Random random = RandomTestUtils.findRandom( (rand) -> { return rand.nextDouble() > Ability.CRITICAL_HIT_CHANCE; });

		Ability abilityA = new Ability("idA", "nameA", Type.FLORA, "descriptionA", 10);
		Ability abilityB = new Ability("idB", "nameB", Type.FLORA, "descriptionB", 10);
		Ability abilityC = new Ability("idC", "nameC", Type.FLORA, "descriptionC", 10);

		BugemonSpecies testSpecies = new BugemonSpecies(
			"id",
			"name",
			Type.FLORA,
			new Stats(100, 100, 100, 100),
			new AbilitySet(abilityA, abilityB, abilityC),
			"sprite",
			true
		);

		Bugemon ownBugemon = new Bugemon(testSpecies);
		Bugemon oppositeBugemon = new Bugemon(testSpecies);

		int obtainedDamage = abilityA.getDamage(ownBugemon, oppositeBugemon, random);

		float expectedAttackFactor = (100.0f + ownBugemon.getAttack()) / 100.0f; // = 2
		float expectedDefenseFactor = 100.0f / (100.0f + oppositeBugemon.getDefense()); // = 0.5
		float expectedRawDamage = abilityA.getPower() * expectedAttackFactor * expectedDefenseFactor; // = 10
		int expectedDamage = Math.round(expectedRawDamage); // no crit, no effectiveness

		assertEquals(expectedDamage, obtainedDamage);
	}

	@Test
	public void correctDamageFormulaWithCrit() {
		Random random = RandomTestUtils.findRandom( (rand) -> { return rand.nextDouble() <= Ability.CRITICAL_HIT_CHANCE; });

		Ability abilityA = new Ability("idA", "nameA", Type.FLORA, "descriptionA", 10);
		Ability abilityB = new Ability("idB", "nameB", Type.FLORA, "descriptionB", 10);
		Ability abilityC = new Ability("idC", "nameC", Type.FLORA, "descriptionC", 10);

		BugemonSpecies testSpecies = new BugemonSpecies(
			"id",
			"name",
			Type.FLORA,
			new Stats(100, 100, 100, 100),
			new AbilitySet(abilityA, abilityB, abilityC),
			"sprite",
			true
		);

		Bugemon ownBugemon = new Bugemon(testSpecies);
		Bugemon oppositeBugemon = new Bugemon(testSpecies);

		int obtainedDamage = abilityA.getDamage(ownBugemon, oppositeBugemon, random);

		float expectedAttackFactor = (100.0f + ownBugemon.getAttack()) / 100.0f; // = 2
		float expectedDefenseFactor = 100.0f / (100.0f + oppositeBugemon.getDefense()); // = 0.5
		float expectedRawDamage = abilityA.getPower() * expectedAttackFactor * expectedDefenseFactor; // = 10
		int expectedDamage = Math.round(expectedRawDamage * Ability.CRITICAL_HIT_FACTOR); // with crit, no effectiveness

		assertEquals(expectedDamage, obtainedDamage);
	}
}
