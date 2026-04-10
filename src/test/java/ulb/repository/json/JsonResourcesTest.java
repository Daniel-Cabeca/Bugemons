package ulb.repository.json;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import ulb.repository.LoadException;

public class JsonResourcesTest {
	@Test
	public void testGetUrlCorrect() {
		String path = JsonResources.PATH_ITEMS;
		assertDoesNotThrow(() -> { JsonResources.getUrl(path); });
	}

	@Test
	public void testGetUrlIncorrect() {
		String path = "doesnotexist";
		assertThrows(LoadException.class, () -> { JsonResources.getUrl(path); });
	}

	@Test
	public void testGetStreamCorrect() {
		String path = JsonResources.PATH_ITEMS;
		assertDoesNotThrow(() -> { JsonResources.getStream(path); });
	}

	@Test
	public void testGetStreamIncorrect() {
		String path = "doesnotexist";
		assertThrows(LoadException.class, () -> { JsonResources.getStream(path); });
	}
}
