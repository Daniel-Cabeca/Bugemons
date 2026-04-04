package ulb.repository.database;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

class DatabaseFileTest {
	@Test
	public void testExistsFalse() {
		DatabaseFile dbFile = new DatabaseFile("doesnotexist.db");
		assertFalse(dbFile.exists());
	}

	@Test
	public void testExistsTrue() throws IOException {
		DatabaseFile dbFile = new DatabaseFile("exists.db");
		Path path = dbFile.getPath();

		Files.createFile(path);
		boolean exists = dbFile.exists();
		Files.delete(path);

		assertTrue(exists);
	}

	@Test
	public void testDelete() throws IOException {
		DatabaseFile dbFile = new DatabaseFile("todelete.db");
		Path path = dbFile.getPath();

		Files.createFile(path);
		dbFile.delete();
		assertFalse(Files.exists(path));
	}
}
