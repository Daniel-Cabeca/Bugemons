package ulb.repository.database.sql;

/**
 * In-memory database that is already initialized.
 */
public class DatabaseMock extends DatabaseInMemory {
	public DatabaseMock() {
		super();

		DatabaseInitializer initializer = new DatabaseInitializer(this);
		initializer.initialize();
	}
}
