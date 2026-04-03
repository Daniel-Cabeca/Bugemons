package ulb.repository.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility methods for SQL.
 */
public abstract class SQLUtils {
	/**
	 * Tests if a table is empty.
	 *
	 * @param connection The SQL connection
	 * @param table The table to test
	 * @return True if the table is empty, false otherwise
	 */
	public static boolean isTableEmpty(Connection connection, String table) {
		String sql = "SELECT EXISTS(SELECT 1 FROM "+ table +" LIMIT 1)";

		try {
			Statement statement = connection.createStatement();
			ResultSet res = statement.executeQuery(sql);

			if (res.next()) {
				return res.getInt(1) == 0;
			}
			return true;
		} catch (SQLException e) {
			throw new IllegalArgumentException("SQL table does not exist: "+ table);
		}
	}
}
