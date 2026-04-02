package ulb.repository.database;

import ulb.repository.LoadException;
import ulb.repository.json.JsonResources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Class for SQL scripts in the class resources.
 */
public class SqlScript {
	private final String query;

	public SqlScript(String path) throws LoadException {
		try {
			URL url = JsonResources.class.getResource(path);
			InputStream stream = url.openStream();

			if (stream == null) {
				throw new LoadException("SQL script not found: "+ path);
			}

			this.query = new String(stream.readAllBytes());
		} catch (IOException e) {
			throw new LoadException("Failed to read SQL script "+ path +": "+ e.getMessage());
		}
	}

	public String getQuery() { return this.query; }
}
