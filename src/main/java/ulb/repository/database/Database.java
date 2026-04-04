package ulb.repository.database;

import java.nio.file.Path;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Represents an SQLite database stored as a file and holds its connection.
 */
public class Database {
	public static final String NAME_DEFAULT = "bugemon";
	public static final String NAME_MOCK = "mock";

	private final String name;
	private final DatabaseFile file;
	private final DatabaseConnection connection;

	public Database(String name) {
		this.name = name;
		this.file = new DatabaseFile(this.getFilePath());
		this.connection = new DatabaseConnection(this.getUrl());
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
	public void close() {
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
}
