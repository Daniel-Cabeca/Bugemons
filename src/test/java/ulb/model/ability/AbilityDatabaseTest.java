package ulb.model.ability;

import org.junit.Test;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

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
	public void testAddThrow() {
		AbilityDatabase database = newDatabase();
		Ability a = AbilitySample.getA();

		database.add(a);
		assertThrows(IllegalArgumentException.class, () -> { database.add(a); });
	}

	@Test
	public void testGetThrow() {
		AbilityDatabase database = newDatabase();
		Ability a = AbilitySample.getA();

		assertThrows(NoSuchElementException.class, () -> { database.get(a.getId()); });
	}

	@Test
	public void testAddGet() {
		AbilityDatabase database = newDatabase();
		Ability a = AbilitySample.getA();

		database.add(a);
		assertEquals(a, database.get(a.getId()));
	}

	@Test
	public void testExistsTrue() {
		AbilityDatabase database = newDatabase();
		Ability a = AbilitySample.getA();

		database.add(a);
		assertTrue(database.exists(a.getId()));
	}

	@Test
	public void testExistsFalse() {
		AbilityDatabase database = newDatabase();
		Ability a = AbilitySample.getA();

		assertFalse(database.exists(a.getId()));
	}
}
