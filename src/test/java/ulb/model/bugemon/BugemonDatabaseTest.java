package ulb.model.bugemon;

import org.junit.Test;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.util.NoSuchElementException;

import ulb.model.type.Type;
import ulb.model.ability.AbilitySet;

public class BugemonDatabaseTest {
	public static BugemonDatabase newDatabase() {
		// bypass the singleton pattern for unit tests
		try {
			Constructor<BugemonDatabase> constructor = BugemonDatabase.class.getDeclaredConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance();
		} catch (Exception e) {
			assertTrue(false);
		}

		return null;
	}

	private BugemonSpecies getSpeciesA() {
		Stats stats = new Stats();
		AbilitySet abilities = new AbilitySet();

		return new BugemonSpecies("florachu", "Florachu", Type.FLORA, stats, abilities, "florachu.png", true);
	}

	private BugemonSpecies getSpeciesB() {
		Stats stats = new Stats();
		AbilitySet abilities = new AbilitySet();

		return new BugemonSpecies("moussil", "Moussil", Type.FLORA, stats, abilities, "moussil.png", false);
	}

	@Test
	public void testAddThrow() {
		BugemonDatabase database = newDatabase();
		BugemonSpecies a = getSpeciesA();

		database.add(a);
		assertThrows(IllegalArgumentException.class, () -> { database.add(a); });
	}

	@Test
	public void testGetThrow() {
		BugemonDatabase database = newDatabase();
		BugemonSpecies a = getSpeciesA();

		assertThrows(NoSuchElementException.class, () -> { database.get(a.getId()); });
	}

	@Test
	public void testAddGet() {
		BugemonDatabase database = newDatabase();
		BugemonSpecies a = getSpeciesA();

		database.add(a);
		assertEquals(a, database.get(a.getId()));
	}

	@Test
	public void testExistsTrue() {
		BugemonDatabase database = newDatabase();
		BugemonSpecies a = getSpeciesA();

		database.add(a);
		assertTrue(database.exists(a.getId()));
	}

	@Test
	public void testExistsFalse() {
		BugemonDatabase database = newDatabase();
		BugemonSpecies a = getSpeciesA();

		assertFalse(database.exists(a.getId()));
	}
}
