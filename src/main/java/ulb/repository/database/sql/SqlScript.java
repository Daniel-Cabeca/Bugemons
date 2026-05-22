package ulb.repository.database.sql;

import ulb.exceptions.LoadException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL script that may contain multiple statements.
 */
public class SqlScript {
	/** The separator found between statements within an SQL script. */
	private static final String QUERY_SEPARATOR = ";";

	/** The URL of the script. */
	private final URL url;

	/**
	 * Creates a script wrapper from an explicit URL.
	 *
	 * @param url The URL of the SQL script
	 */
	public SqlScript(URL url) {
		this.url = url;
	}

	/**
	 * Creates a script wrapper from a classpath resource path.
	 *
	 * @param path The classpath path to the SQL script
	 */
	public SqlScript(String path) throws LoadException {
		this.url = SqlScript.class.getResource(path);

		if (this.url == null) {
			throw new LoadException("SQL script not found: " + path);
		}
	}

	/**
	 * Executes the script.
	 *
	 * @param database The database to run the script on
	 */
	public void execute(Database database) throws SQLException, LoadException {
		Statement statement = database.createStatement();

		for (String statementStr : this.getStatements()) {
			statement.execute(statementStr);
		}
	}

	/**
	 * Gets the list of statements in the string.
	 *
	 * @return The list of statements
	 */
	public Iterable<String> getStatements() throws LoadException {
		String[] statements = this.getSql().split(QUERY_SEPARATOR);
		List<String> res = new ArrayList<>();

		for (String statement : statements) {
			statement = statement.trim();

			if (!statement.isEmpty()) {
				res.add(statement);
			}
		}

		return res;
	}

	/**
	 * Reads the script file's content.
	 *
	 * @return The script's content
	 */
	public String getSql() throws LoadException {
		try {
			InputStream stream = this.getUrl().openStream();
			byte[] bytes = stream.readAllBytes();
			return new String(bytes);
		} catch (IOException e) {
			throw new LoadException("Failed to read SQL script '" + this.getUrl() + "'.");
		}
	}

	/**
	 * Returns the script resource URL.
	 *
	 * @return The SQL script URL
	 */
	public URL getUrl() { return this.url; }
}
