package ulb.repository.database;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;

class DatabaseTest {
	@Test
	public void verifyGetUrl() {
		Database db = new Database("name");
		assertEquals("jdbc:sqlite:name.db", db.getUrl());
	}

	@Test
	public void verifyGetFilePath() {
		Database db = new Database("name");
		assertEquals(Path.of("name.db"), db.getFilePath());
	}

	@Test
	public void deleteThrowsIfConnected() {
		Database db = new Database("mock");
		db.connect();
		assertThrows(IllegalStateException.class, () -> { db.delete(); });
	}
}
