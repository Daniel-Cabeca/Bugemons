package ulb.repository.database.sql;

import org.junit.jupiter.api.AfterEach;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DatabaseInFileTest {
	private static final String TEST_DB_NAME = "test";
	private static final Path TEST_DB_PATH = DatabaseInFile.getPath(TEST_DB_NAME);

	@AfterEach
	void deleteFile() throws IOException {
		Files.deleteIfExists(TEST_DB_PATH);
	}
}
