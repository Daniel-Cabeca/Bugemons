package ulb.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

import ulb.exceptions.EntityNotFoundException;
import ulb.model.bugemon.Bugemon;
import ulb.repository.BugemonSpeciesRepository;
import ulb.repository.mock.BugemonSpeciesMockRepository;

/**
 * Provides Bugemon-related business logic.
 */
public class BugemonServiceTest {
	public static BugemonService getMockService() {
		BugemonSpeciesRepository speciesRepository = new BugemonSpeciesMockRepository();
		return new BugemonService(speciesRepository);
	}

	@Test
	public void testSpawnBugemonNoException() {
		BugemonService service = getMockService();
		assertDoesNotThrow(() -> { service.spawnBugemon("florachu"); });
	}

	@Test
	public void testSpawnBugemonException() {
		BugemonService service = getMockService();
		assertThrows(EntityNotFoundException.class, () -> { service.spawnBugemon("doesnotexist"); });
	}

	@Test
	public void testSpawnBugemonCorrectSpecies() throws Exception {
		BugemonService service = getMockService();
		Bugemon obtained = service.spawnBugemon("florachu");

		assertEquals("florachu", obtained.getSpecies().getId());
	}

	@Test
	public void testSpawnBugemonGotOne() throws Exception {
		BugemonService service = getMockService();
		Bugemon obtained = service.spawnBugemonRandom();

		assertNotNull(obtained);
	}

	@Test
	public void testGetAllSpeciesNotNull() throws Exception {
		BugemonService service = getMockService();
		assertNotNull(service.getAllSpecies());
	}
}
