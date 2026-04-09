package ulb.repository.database.sql;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseInitializerTest {
	@Test
	public void createTablesScriptDoesNotThrow() {
		Database db = new DatabaseInMemory();
		DatabaseInitializer dbInitializer = new DatabaseInitializer(db);
		assertDoesNotThrow(() -> { dbInitializer.createTables(); });
	}

	@Test
	public void populateWithDefaultDataDoesNotThrow() {
		Database db = new DatabaseInMemory();
		DatabaseInitializer dbInitializer = new DatabaseInitializer(db);
		dbInitializer.createTables();
		assertDoesNotThrow(() -> { dbInitializer.populate(); });
	}

	@Test
	public void prepareDefaultDatabaseDoesNotThrow() {
		assertDoesNotThrow(() -> { DatabaseInitializer.prepareDefaultDatabase(); });
	}
}
