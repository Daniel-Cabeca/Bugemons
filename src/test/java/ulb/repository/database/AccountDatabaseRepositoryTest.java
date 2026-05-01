package ulb.repository.database;

import ulb.repository.database.sql.DatabaseInitializer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import ulb.model.ability.Ability;
import ulb.model.type.Type;
import ulb.repository.database.sql.Database;
import ulb.repository.database.sql.DatabaseInMemory;
import ulb.repository.database.sql.DatabaseInitializer;import ulb.repository.database.sql.DatabaseMock;
import ulb.utils.DuplicateElementException;

import java.util.Map;
import java.util.stream.StreamSupport;

public class AccountDatabaseRepositoryTest {


	@Test
	public void testLeaderboardSimple() {
		Database database = new DatabaseInMemory();
		new DatabaseInitializer(database).createTables();
		AccountDatabaseRepository repo = new AccountDatabaseRepository(database);

		// Inscription de deux joueurs
		repo.register("Alice", "123"); // ID 1
		repo.register("Bob", "456");   // ID 2

		// Ajout de points
		repo.addPoints(1, 100); // Alice gagne 100
		repo.addPoints(2, 250); // Bob gagne 250

		// Récupération
		Map<String, Integer> lb = repo.getLeaderboard();

		// Vérification de l'ordre (Bob doit être premier)
		String firstPlayer = lb.keySet().iterator().next();
		assertEquals("Bob", firstPlayer);
		assertEquals(250, lb.get("Bob"));
	}
}
