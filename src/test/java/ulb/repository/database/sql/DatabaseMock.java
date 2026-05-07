package ulb.repository.database.sql;

import ulb.exceptions.LoadException;

/**
 * In-memory database that is already initialized.
 */
public class DatabaseMock extends DatabaseInMemory {
	public DatabaseMock() throws LoadException {
		super();

		DatabaseInitializer initializer = new DatabaseInitializer(this);
		initializer.initialize();
	}
}
