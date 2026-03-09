package ulb.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

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
		assertThrows(NoSuchElementException.class, () -> { service.spawnBugemon("doesnotexist"); });
	}

	@Test
	public void testSpawnBugemonCorrectSpecies() {
		BugemonService service = getMockService();
		Bugemon obtained = service.spawnBugemon("florachu");

		assertEquals("florachu", obtained.getSpecies().getId());
	}

	@Test
	public void testSpawnBugemonGotOne() {
		BugemonService service = getMockService();
		Bugemon obtained = service.spawnBugemonRandom();

		assertNotNull(obtained);
	}

	@Test
	public void testGetAllSpeciesNotNull() {
		BugemonService service = getMockService();
		assertNotNull(service.getAllSpecies());
	}
}
