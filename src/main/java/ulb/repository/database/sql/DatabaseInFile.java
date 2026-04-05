package ulb.repository.database.sql;

import java.nio.file.Files;
import java.nio.file.Path;

import java.util.Map;
import java.util.HashMap;

/**
 * Database stored as a file.
 * Implemented as a map of singletons.
 */
public class DatabaseInFile extends Database {
	public static final String NAME_DEFAULT = "bugemon";

	private static final Map<String, DatabaseInFile> instances = new HashMap<>();

	private final String name;
	private final boolean isNew;

	DatabaseInFile(String name, boolean isNew) {
		super("jdbc:sqlite:"+ name +".db");
		this.name = name;
		this.isNew = isNew;
	}

	DatabaseInFile(String name) {
		this(name, !Files.exists(getPath(name)));
	}

	/**
	 * Gets the Database instance corresponding to a given name.
	 *
	 * @param name The database's name
	 * @return The corresponding Database instance
	 */
	public static DatabaseInFile get(String name) {
		DatabaseInFile db = instances.get(name);

		if (db == null) {
			db = new DatabaseInFile(name);
			instances.put(name, db);
		}

		return db;
	}

	public String getName() { return this.name; }
	public Path getPath() { return getPath(this.getName()); }
	static Path getPath(String name) { return Path.of(name +".db"); }

	/**
	 * {@inheritDoc}
	 */
	public boolean isNew() {
		return this.isNew;
	}
}
