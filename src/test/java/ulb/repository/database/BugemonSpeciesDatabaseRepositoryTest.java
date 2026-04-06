package ulb.repository.database;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import ulb.model.ability.Ability;import ulb.model.ability.AbilitySet;import ulb.model.bugemon.BugemonSpecies;
import ulb.model.bugemon.Stats;import ulb.model.type.Type;import ulb.repository.database.sql.Database;import ulb.repository.database.sql.DatabaseInMemory;import ulb.repository.database.sql.DatabaseInitializer;import ulb.repository.database.sql.DatabaseMock;import ulb.utils.DuplicateElementException;
import java.util.NoSuchElementException;import java.util.stream.StreamSupport;

public class BugemonSpeciesDatabaseRepositoryTest {

	@Test
	public void findByIdGivesCorrectSpecies() {
		BugemonSpeciesDatabaseRepository repository = new BugemonSpeciesDatabaseRepository(new DatabaseMock());

		String id = "florachu";

		BugemonSpecies obtained = repository.findById(id);

		assertEquals(id, obtained.getId());
	}

	@Test
	public void findAllGivesCorrectAbilities() throws DuplicateElementException {
		Database database = new DatabaseInMemory();
		DatabaseInitializer databaseInitializer = new DatabaseInitializer(database);
		databaseInitializer.createTables();

		AbilityDatabaseRepository abilityRepository = new AbilityDatabaseRepository(database);
		BugemonSpeciesDatabaseRepository bugemonSpeciesRepository = new BugemonSpeciesDatabaseRepository(database);

		Ability ability1 = new Ability("1","kamehameha", Type.FLORA,"",5);
		Ability ability2 = new Ability("2","boom", Type.PYRO,"",2);
		Ability ability3 = new Ability("3","splash", Type.AQUA,"",0);

		abilityRepository.insertAbility(ability1);
		abilityRepository.insertAbility(ability2);
		abilityRepository.insertAbility(ability3);

		AbilitySet abilitySet = new AbilitySet(ability1, ability2, ability3);
		BugemonSpecies bugemonSpecies1 = new BugemonSpecies("1", "cat", Type.LITHO,
			new Stats(10, 10, 10, 10),
			abilitySet, "cat.png", true);
		BugemonSpecies bugemonSpecies2 = new BugemonSpecies("2", "dog", Type.LITHO,
			new Stats(10, 10, 10, 10),
			abilitySet, "dog.png", true);

		bugemonSpeciesRepository.insertSpecie(bugemonSpecies1);
		bugemonSpeciesRepository.insertSpecie(bugemonSpecies2);

		Iterable<BugemonSpecies> obtained = bugemonSpeciesRepository.findAll();

		assertTrue(StreamSupport.stream(obtained.spliterator(), false)
                .anyMatch(a -> a.getId().equals(bugemonSpecies1.getId())));
		assertTrue(StreamSupport.stream(obtained.spliterator(), false)
                .anyMatch(a -> a.getId().equals(bugemonSpecies2.getId())));
	}
}