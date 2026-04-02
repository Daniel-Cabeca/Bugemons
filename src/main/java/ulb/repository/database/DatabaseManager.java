package ulb.repository.database;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import ulb.repository.LoadException;

public class DatabaseManager {
	public static final String DEFAULT_URL = "jdbc:sqlite:bugemon.db";
	public static final String MOCK_URL = "jdbc:sqlite:mock.db";
	public static final String INIT_SCRIPT_PATH = "/sql/init_db.sql";

	private Connection connection;

	public DatabaseManager() throws LoadException {
		this(DEFAULT_URL);
	}

	public DatabaseManager(String url) throws LoadException {
		this.connection = establishConnection(url);
		this.createTables();
	}

	/**
	 * Establishes the connection with the database.
	 *
	 * @param url The url to the database
	 * @return The database connection
	 * @throws SQLException If failed to establish connection
	 */
	private static Connection establishConnection(String url) throws LoadException {
		try {
			Connection connection = DriverManager.getConnection(url);
			System.out.println("SQLite connection established.");
			return connection;
		} catch (SQLException e) {
			throw new LoadException("SQL error when establishing connection: "+ e.getMessage());
		}
	}

	/**
	 * Creates the tables for the database.
	 *
	 * @throws LoadException If failed to create the tables
	 */
	private void createTables() throws LoadException {
		SqlScript script = new SqlScript(INIT_SCRIPT_PATH);
		String sql = script.getQuery();

		try {
			Statement statement = this.getConnection().createStatement();
			statement.execute(sql);
		} catch (SQLException e) {
			throw new LoadException("SQL error when initializing the database: "+ e.getMessage());
		}
	}

	public Connection getConnection() { return connection; }
}
