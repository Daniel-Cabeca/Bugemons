package ulb.repository.database;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;

/**
 * An SQLite database file.
 */
class DatabaseFile {
	private final Path path;

	public DatabaseFile(Path path) {
		this.path = path;
	}

	public DatabaseFile(String path) {
		this(Path.of(path));
	}

	public Path getPath() { return this.path; }

	/**
	 * Tests whether the database file  exists or not.
	 *
	 * @return True if the database file exists, false otherwise
	 */
	public boolean exists() {
		return Files.exists(this.getPath());
	}

	/**
	 * Deletes the database file.
	 */
	public void delete() {
		try {
			Files.delete(this.getPath());
		} catch(IOException e) {
			throw new RuntimeException("Failed to delete database file '"+ this.getPath() +"': "+ e.getMessage());
		}
	}
}
