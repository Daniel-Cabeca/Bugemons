package ulb.repository.database;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import ulb.model.ability.Ability;
import ulb.model.type.Type;
import ulb.repository.database.sql.Database;
import ulb.repository.database.sql.DatabaseInMemory;
import ulb.repository.database.sql.DatabaseInitializer;import ulb.repository.database.sql.DatabaseMock;
import ulb.utils.DuplicateElementException;

import java.util.stream.StreamSupport;


public class AbilityDatabaseRepositoryTest {


	@Test
	public void findByIdGivesCorrectAbility() throws Exception {
		AbilityDatabaseRepository repository = new AbilityDatabaseRepository(new DatabaseMock());

		String id = "surchauffe";
		Ability obtained = repository.findById(id);
		assertEquals(id, obtained.getId());
	}

	@Test
	public void findAllGivesCorrectAbilities() throws Exception {
		Database database = new DatabaseInMemory();
		DatabaseInitializer databaseInitializer = new DatabaseInitializer(database);
		databaseInitializer.createTables();

		AbilityDatabaseRepository repository = new AbilityDatabaseRepository(database);

		Ability ability1 = new Ability("1","kamehameha", Type.FLORA,"",5);
		Ability ability2 = new Ability("2","boom", Type.PYRO,"",2);

		repository.insertAbility(ability1);
		repository.insertAbility(ability2);

		Iterable<Ability> obtained = repository.findAll();

		assertTrue(StreamSupport.stream(obtained.spliterator(), false)
                .anyMatch(a -> a.getId().equals(ability1.getId())));
		assertTrue(StreamSupport.stream(obtained.spliterator(), false)
                .anyMatch(a -> a.getId().equals(ability2.getId())));
	}
}
