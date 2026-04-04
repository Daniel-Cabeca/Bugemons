package ulb.repository.database;

import java.nio.file.Path;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;

import java.util.Map;
import java.util.HashMap;

/**
 * Represents an SQLite database stored as a file and holds its connection.
 * Implemented as a map of singletons.
 */
public class Database {
	public static final String NAME_DEFAULT = "bugemon";
	public static final String NAME_MOCK = "mock";

	private static final Map<String, Database> instances = new HashMap<>();

	private final String name;
	private final DatabaseFile file;
	private final DatabaseConnection connection;

	private Database(String name) {
		this.name = name;
		this.file = new DatabaseFile(this.getFilePath());
		this.connection = new DatabaseConnection(this.getUrl());
	}

	/**
	 * Gets the Database instance corresponding to a given name.
	 *
	 * @param name The database's name
	 * @return The corresponding Database instance
	 */
	public static Database get(String name) {
		Database db = instances.get(name);

		if (db == null) {
			db = new Database(name);
			instances.put(name, db);
		}

		return db;
	}

	public String getName() { return this.name; }
	public DatabaseFile getFile() { return this.file; }
	public DatabaseConnection getConnection() { return this.connection; }

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

	/**
	 * Whether the database exists.
	 *
	 * @return True if the database exists, false otherwise
	 */
	public boolean exists() {
		return this.getFile().exists();
	}

	/**
	 * Deletes the database.
	 * Cannot be done if the connection is open.
	 *
	 * @throws IllegalStateException If the connection is open
	 */
	public void delete() {
		if (this.isConnected()) {
			throw new IllegalStateException("Cannot delete an open database.");
		}

		this.getFile().delete();
	}

	/**
	 * Whether the database's connection is established.
	 *
	 * @return True if the connection is established, false otherwise
	 */
	public boolean isConnected() {
		return this.getConnection().isOpen();
	}

	/**
	 * Establishes the connection with the database.
	 */
	public void connect() {
		this.getConnection().open();
	}

	/**
	 * Closes the connection with the database.
	 */
	public void disconnect() {
		this.getConnection().close();
	}

	/**
	 * Creates an SQL statement.
	 *
	 * @return The SQL statement
	 * @throws IllegalStateException If the database is not connected.
	 */
	public Statement createStatement() {
		return this.getConnection().createStatement();
	}

	/**
	 * Creates a prepared SQL statement.
	 *
	 * @param sql The statement as text
	 * @return The SQL prepared statement
	 * @throws IllegalStateException If the database is not connected.
	 */
	public PreparedStatement prepareStatement(String sql) {
		return this.getConnection().prepareStatement(sql);
	}
}
