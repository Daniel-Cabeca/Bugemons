package ulb.repository.database;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

import ulb.repository.LoadException;

/**
 * Connection with an SQL database.
 */
public class DatabaseConnection {
	private final String url;
	private Connection sqlConnection = null;

	public DatabaseConnection(String url) {
		this.url = url;
	}

	public String getUrl() { return this.url; }
	public Connection getSqlConnection() { return this.sqlConnection; }

	/**
	 * Whether the connection is open or closed.
	 *
	 * @return True if the connection is open, false otherwise
	 */
	public boolean isOpen() {
		return this.sqlConnection != null;
	}

	/**
	 * Establish the SQL connection with the database.
	 *
	 * @throws LoadException If connection fails
	 */
	public void open() throws LoadException {
		if (this.isOpen()) {
			throw new IllegalStateException("Cannot connect to a database if already connected to it.");
		}

		try {
			this.sqlConnection = DriverManager.getConnection(this.getUrl());
		} catch (SQLException e) {
			throw new LoadException("Failed to connect to database '"+ this.getUrl() +"': "+ e.getMessage());
		}
	}

	/**
	 * Closes the SQL connection with the database.
	 */
	public void close() {
		if (this.isOpen()) {
			try {
				this.sqlConnection.close();
				this.sqlConnection = null;
			} catch (SQLException e) {
				throw new RuntimeException("Failed to close the database '"+ this.getUrl() +"': "+ e.getMessage());
			}
		}
	}
}
