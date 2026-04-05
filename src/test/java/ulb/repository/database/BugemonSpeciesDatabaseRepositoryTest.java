package ulb.repository.database;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import ulb.model.bugemon.BugemonSpecies;
import ulb.repository.database.sql.DatabaseMock;
import java.util.NoSuchElementException;

public class BugemonSpeciesDatabaseRepositoryTest {

	@Test
	public void findByIdGivesCorrectSpecies() {
		BugemonSpeciesDatabaseRepository repository = new BugemonSpeciesDatabaseRepository(new DatabaseMock());

		String id = "florachu";

		BugemonSpecies obtained = repository.findById(id);

		assertEquals(id, obtained.getId());
	}

}