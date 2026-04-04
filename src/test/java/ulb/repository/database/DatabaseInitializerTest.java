package ulb.repository.database;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseInitializerTest {
	@Test
	public void createTablesScriptDoesNotThrow() {
		Database db = Database.get("create_tables_test");
		db.delete();
		db.connect();

		DatabaseInitializer dbInitializer = new DatabaseInitializer(db);
		assertDoesNotThrow(() -> { dbInitializer.createTables(); });

		db.disconnect();
		db.delete();
	}

	@Test
	public void populateWithDefaultDataDoesNotThrow() {
		Database db = Database.get("populate_test");
		db.delete();
		db.connect();

		DatabaseInitializer dbInitializer = new DatabaseInitializer(db);
		dbInitializer.createTables();
		assertDoesNotThrow(() -> { dbInitializer.populate(); });

		db.disconnect();
		db.delete();
	}

	@Test
	public void prepareDefaultDatabaseDoesNotThrow() {
		assertDoesNotThrow(() -> { DatabaseInitializer.prepareDefaultDatabase(); });
	}
}
