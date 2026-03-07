package ulb.repository.loader.json;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;

import ulb.repository.loader.LoadFailureException;

public class JsonResourcesTest {
	@Test
	public void testGetUrlCorrect() {
		String path = JsonResources.PATH_ITEMS;
		assertDoesNotThrow(() -> { JsonResources.getUrl(path); });
	}

	@Test
	public void testGetUrlIncorrect() {
		String path = "doesnotexist";
		assertThrows(LoadFailureException.class, () -> { JsonResources.getUrl(path); });
	}

	@Test
	public void testGetStreamCorrect() {
		String path = JsonResources.PATH_ITEMS;
		assertDoesNotThrow(() -> { JsonResources.getStream(path); });
	}

	@Test
	public void testGetStreamIncorrect() {
		String path = "doesnotexist";
		assertThrows(LoadFailureException.class, () -> { JsonResources.getStream(path); });
	}
}
