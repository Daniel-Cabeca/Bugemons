package ulb.repository.database.sql;

import ulb.exceptions.LoadException;

/**
 * Temporary database stored in memory.
 */
public class DatabaseInMemory extends Database {
	public DatabaseInMemory() throws LoadException {
		super("jdbc:sqlite::memory:");
	}
}
