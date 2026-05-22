package ulb.repository.database.sql;

import ulb.exceptions.LoadException;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Database stored as a file.
 * Implemented as a map of singletons.
 */
public class DatabaseInFile extends Database {
	/** The default database name. */
	public static final String NAME_DEFAULT = "bugemon";
	/** The default directory where the database is saved. */
	public static final Path DIRECTORY = resolveDataDirectory("infof307");

	/** Map associating each existing database connection's name to its instance. */
	private static final Map<String, DatabaseInFile> instances = new HashMap<>();

	/** The name of the database. */
	private final String name;

	/**
	 * Creates a file-backed database instance and infers if it is new.
	 *
	 * @param name The database base filename
	 */
	DatabaseInFile(String name) throws LoadException {
		super("jdbc:sqlite:" + getPath(name));
		this.name = name;
	}

	/**
	 * Returns the file path associated with a database name.
	 *
	 * @param name The database base filename
	 * @return The corresponding file path
	 */
	static Path getPath(String name) {
		return DIRECTORY.resolve(name + ".db");
	}

	/**
	 * Gets the Database instance corresponding to a given name.
	 *
	 * @param name The database's name
	 * @return The corresponding Database instance
	 */
	public static DatabaseInFile get(String name) throws LoadException {
		DatabaseInFile db = instances.get(name);

		if (db == null) {
			DIRECTORY.toFile().mkdirs();
			db = new DatabaseInFile(name);
			instances.put(name, db);

		}

		return db;
	}

	/**
	 * Builds the path to the database file from the application name.
	 *
	 * @param appName The application name
	 * @return The path to the database file
	 */
	private static Path resolveDataDirectory(String appName) {
		String os = System.getProperty("os.name").toLowerCase();

		if (os.contains("win")) {
			// Windows : %APPDATA%\appName
			String appData = System.getenv("APPDATA");
			if (appData != null) return Path.of(appData, appName);

		} else if (os.contains("mac")) {
			// macOS : ~/Library/Application Support/appName
			return Path.of(System.getProperty("user.home"), "Library", "Application Support", appName);

		} else {
			// Linux/Unix : $XDG_DATA_HOME/appName ou ~/.local/share/appName
			String xdg = System.getenv("XDG_DATA_HOME");
			if (xdg != null && !xdg.isBlank()) return Path.of(xdg, appName);
			return Path.of(System.getProperty("user.home"), ".local", "share", appName);
		}

		// Fallback universel
		return Path.of(System.getProperty("user.home"), "." + appName);
	}

	/**
	 * Returns the path to this database file.
	 *
	 * @return The file path for this database
	 */
	public Path getPath() { return getPath(this.getName()); }

	/**
	 * Returns the database name.
	 *
	 * @return The database name without extension
	 */
	public String getName() { return this.name; }
}
