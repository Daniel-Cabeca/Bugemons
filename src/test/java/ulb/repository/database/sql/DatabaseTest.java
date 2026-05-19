package ulb.repository.database.sql;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseTest {
	@Test
	public void testTableIsEmptyTrue() throws Exception {
		Database db = new DatabaseInMemory();
		DatabaseInitializer initializer = new DatabaseInitializer(db);
		initializer.createTables();
		assertTrue(db.isTableEmpty("items"));
	}

	@Test
	public void testTableIsEmptyFalse() throws Exception {
		Database db = new DatabaseMock();
		assertFalse(db.isTableEmpty("items"));
	}
}
