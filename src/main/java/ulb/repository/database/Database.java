package ulb.repository.database;

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;

/**
 * Represents an SQLite database stored as a file and holds its connection.
 */
class Database {
	public static final String NAME_DEFAULT = "bugemon";
	public static final String NAME_MOCK = "mock";

	private final String name;
	private final DatabaseFile file;

	public Database(String name) {
		this.name = name;
		this.file = new DatabaseFile(this.getFilePath());
	}

	public String getName() { return this.name; }
	public DatabaseFile getFile() { return this.file; }

	/**
	 * Gives the jdbc url of the database.
	 *
	 * @return The jdbc url
	 */
	public String getUrl() {
		return "jdbc:sqlite:"+ this.getName() + ".db";
	}

	/**
	 * Gives the filename of the database.
	 *
	 * @return The database's filename
	 */
	public Path getFilePath() {
		return Path.of(this.getName() +".db");
	}
}
