package ulb.repository.database;

import ulb.repository.LoadException;
import ulb.repository.json.JsonResources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class for SQL scripts in the class resources.
 */
public class SqlScript {
	private final String sql;

	public SqlScript(String path) throws LoadException {
		try {
			URL url = JsonResources.class.getResource(path);
			InputStream stream = url.openStream();

			if (stream == null) {
				throw new LoadException("SQL script not found: "+ path);
			}

			this.sql = new String(stream.readAllBytes());
		} catch (IOException e) {
			throw new LoadException("Failed to read SQL script "+ path +": "+ e.getMessage());
		}
	}

	public String getSql() { return this.sql; }

	/**
	 * Executes the script.
	 *
	 * @param connection The SQL connection
	 * @throws SQLException If an SQL error occurs
	 */
	public void execute(Connection connection) throws SQLException {
		Statement statement = connection.createStatement();

		for(String query: this.getSql().split(";")) {
			String trimmedQuery = query.trim();
			if (!trimmedQuery.isEmpty()) {
				statement.execute(trimmedQuery);
			}
		}
	}
}
