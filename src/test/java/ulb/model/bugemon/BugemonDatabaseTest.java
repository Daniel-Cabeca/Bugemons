package ulb.model.bugemon;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
	public void addAlreadyExistingBugemonSpeciesThrowsException() {
		BugemonDatabase database = newDatabase();
		BugemonSpecies a = getSpeciesA();

		database.add(a);
		assertThrows(IllegalArgumentException.class, () -> { database.add(a); });
	}

	@Test
	public void getNonExistingBugemonSpeciesThrowsEception() {
		BugemonDatabase database = newDatabase();
		BugemonSpecies a = getSpeciesA();

		assertThrows(NoSuchElementException.class, () -> { database.get(a.getId()); });
	}

	@Test
	public void addAndGetBugemonSpeciesFunctionnality() {
		BugemonDatabase database = newDatabase();
		BugemonSpecies a = getSpeciesA();

		database.add(a);
		assertEquals(a, database.get(a.getId()));
	}

	@Test
	public void existsIsTrueWhenBugemonSpeciesIsInDataBase() {
		BugemonDatabase database = newDatabase();
		BugemonSpecies a = getSpeciesA();

		database.add(a);
		assertTrue(database.exists(a.getId()));
	}

	@Test
	public void existsIsFalseWhenBugemonSpeciesIsInDataBase() {
		BugemonDatabase database = newDatabase();
		BugemonSpecies a = getSpeciesA();

		assertFalse(database.exists(a.getId()));
	}
}
