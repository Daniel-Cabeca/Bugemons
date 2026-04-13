package ulb.repository.database.sql;

import ulb.repository.LoadException;

import java.sql.*;

/**
 * Base SQL database wrapper exposing helper operations.
 */
public abstract class Database {
	private final String url;
	private final Connection connection;

	/**
	 * Creates a database wrapper and opens its connection.
	 *
	 * @param url The JDBC URL
	 */
	public Database(String url) {
		this.url = url;
		this.connection = connect(this.getUrl());
	}

	/**
	 * Opens a SQL connection to the provided URL.
	 *
	 * @param url The JDBC URL
	 * @return The opened connection
	 */
	static Connection connect(String url) {
		try {
			return DriverManager.getConnection(url);
		} catch (SQLException e) {
			throw new LoadException("Failed to connect to database '"+ url +"': "+ e.getMessage());
		}
	}

	/**
	 * Returns the database JDBC URL.
	 *
	 * @return The configured database URL
	 */
	public String getUrl() { return this.url; }
	/**
	 * Returns the active SQL connection.
	 *
	 * @return The active SQL connection
	 */
	public Connection getConnection() { return this.connection; }

	/**
	 * Creates an SQL statement.
	 *
	 * @return The SQL statement
	 */
	public Statement createStatement() {
		try {
			return this.getConnection().createStatement();
		} catch (SQLException e) {
			throw new RuntimeException("Failed to create an SQL statement: "+ e.getMessage());
		}
	}

	/**
	 * Creates a prepared SQL statement.
	 *
	 * @param sql The statement as text
	 * @return The prepared SQL statement
	 */
	public PreparedStatement prepareStatement(String sql) {
		try {
			return this.getConnection().prepareStatement(sql);
		} catch (SQLException e) {
			throw new RuntimeException("Failed to create a prepared SQL statement: "+ e.getMessage());
		}
	}

	/**
	 * Tests if a table is empty.
	 *
	 * @param table The name of the table to test
	 * @return True if the table is empty, false otherwise
	 */
	public boolean isTableEmpty(String table) {
		String sql = "SELECT EXISTS(SELECT 1 FROM "+ table +" LIMIT 1)";

		try {
			Statement statement = this.getConnection().createStatement();
			ResultSet res = statement.executeQuery(sql);

			if (res.next()) {
				return res.getInt(1) == 0;
			}
			return true;
		} catch (SQLException e) {
			throw new IllegalArgumentException("SQL table does not exist: "+ table);
		}
	}

	/**
	 * Whether the database has been newly created.
	 *
	 * @return True if it is a new databas,e false otherwise
	 */
	public boolean isNew() {
		Statement statement = this.createStatement();

		try {
			ResultSet res = statement.executeQuery(
				"SELECT name FROM sqlite_master WHERE type='table' AND name='items';"
			);

			return !res.next();
		} catch (SQLException e) {
			throw new RuntimeException("Failed to test if the database is new: "+ e.getMessage());
		}
	}
}
