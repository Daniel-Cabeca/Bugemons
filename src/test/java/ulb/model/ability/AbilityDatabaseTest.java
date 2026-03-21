package ulb.model.ability;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;
import java.util.NoSuchElementException;

import ulb.model.type.Type;

import ulb.model.sample.AbilitySample;

public class AbilityDatabaseTest {
	public static AbilityDatabase newDatabase() {
		// bypass the singleton pattern for unit tests
		try {
			Constructor<AbilityDatabase> constructor = AbilityDatabase.class.getDeclaredConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance();
		} catch (Exception e) {
			assertTrue(false);
		}

		return null;
	}

	@Test
	public void addAlreadyExistingAbilityThrowsException() {
		AbilityDatabase database = newDatabase();
		Ability a = AbilitySample.getA();

		database.add(a);
		assertThrows(IllegalArgumentException.class, () -> { database.add(a); });
	}

	@Test
	public void getNonExistingAbilityThrowsException() {
		AbilityDatabase database = newDatabase();
		Ability a = AbilitySample.getA();

		assertThrows(NoSuchElementException.class, () -> { database.get(a.getId()); });
	}

	@Test
	public void getAbilityFunctionnality() {
		AbilityDatabase database = newDatabase();
		Ability a = AbilitySample.getA();

		database.add(a);
		assertEquals(a, database.get(a.getId()));
	}

	@Test
	public void existsIsTrueWhenAbilityIsInDatabase() {
		AbilityDatabase database = newDatabase();
		Ability a = AbilitySample.getA();

		database.add(a);
		assertTrue(database.exists(a.getId()));
	}

	@Test
	public void existsIsFalseWhenAbilityIsNotInDatabase() {
		AbilityDatabase database = newDatabase();
		Ability a = AbilitySample.getA();

		assertFalse(database.exists(a.getId()));
	}
}
