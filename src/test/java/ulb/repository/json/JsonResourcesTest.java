package ulb.repository.json;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import ulb.exceptions.LoadException;

public class JsonResourcesTest {
	@Test
	public void testGetUrlCorrect() throws Exception {
		String path = JsonResources.PATH_ITEMS;
		assertDoesNotThrow(() -> { JsonResources.getUrl(path); });
	}

	@Test
	public void testGetUrlIncorrect() throws Exception {
		String path = "doesnotexist";
		assertThrows(LoadException.class, () -> { JsonResources.getUrl(path); });
	}

	@Test
	public void testGetStreamCorrect() throws Exception {
		String path = JsonResources.PATH_ITEMS;
		assertDoesNotThrow(() -> { JsonResources.getStream(path); });
	}

	@Test
	public void testGetStreamIncorrect() throws Exception {
		String path = "doesnotexist";
		assertThrows(LoadException.class, () -> { JsonResources.getStream(path); });
	}
}
