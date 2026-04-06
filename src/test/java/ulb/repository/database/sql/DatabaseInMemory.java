package ulb.repository.database.sql;

/**
 * Temporary database stored in memory.
 */
public class DatabaseInMemory extends Database {
	public DatabaseInMemory() {
		super("jdbc:sqlite::memory:");
	}
}
