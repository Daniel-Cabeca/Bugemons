package ulb.repository.database.sql;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {
	@Test
	public void testTableIsEmptyTrue() {
		Database db = new DatabaseInMemory();
		DatabaseInitializer initializer = new DatabaseInitializer(db);
		initializer.createTables();
		assertTrue(db.isTableEmpty("items"));
	}

	@Test
	public void testTableIsEmptyFalse() {
		Database db = new DatabaseMock();
		assertFalse(db.isTableEmpty("items"));
	}
}
