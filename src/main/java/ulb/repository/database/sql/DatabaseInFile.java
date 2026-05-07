package ulb.repository.database.sql;

import ulb.exceptions.LoadException;

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

	/**
	 * Creates a file-backed database instance.
	 *
	 * @param name The database base filename
	 * @param isNew Whether the database file is newly created
	 */
	DatabaseInFile(String name, boolean isNew) throws LoadException {
		super("jdbc:sqlite:"+ name +".db");
		this.name = name;
		this.isNew = isNew;
	}

	/**
	 * Creates a file-backed database instance and infers if it is new.
	 *
	 * @param name The database base filename
	 */
	DatabaseInFile(String name) throws LoadException {
		this(name, !Files.exists(getPath(name)));
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
			db = new DatabaseInFile(name);
			instances.put(name, db);
		}

		return db;
	}

	/**
	 * Returns the database name.
	 *
	 * @return The database name without extension
	 */
	public String getName() { return this.name; }
	/**
	 * Returns the path to this database file.
	 *
	 * @return The file path for this database
	 */
	public Path getPath() { return getPath(this.getName()); }
	/**
	 * Returns the file path associated with a database name.
	 *
	 * @param name The database base filename
	 * @return The corresponding file path
	 */
	static Path getPath(String name) { return Path.of(name +".db"); }
}
