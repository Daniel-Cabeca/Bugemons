package ulb.repository.database;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;

class DatabaseTest {
	@Test
	public void verifyGetUrl() {
		Database db = Database.get("name");
		assertEquals("jdbc:sqlite:name.db", db.getUrl());
	}

	@Test
	public void verifyGetFilePath() {
		Database db = Database.get("name");
		assertEquals(Path.of("name.db"), db.getFilePath());
	}

	@Test
	public void deleteThrowsIfConnected() {
		Database db = Database.get("test_delete_on_connected");
		db.connect();
		assertThrows(IllegalStateException.class, () -> { db.delete(); });

		db.disconnect();
		db.delete();
	}
}
