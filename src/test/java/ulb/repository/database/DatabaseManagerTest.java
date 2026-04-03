package ulb.repository.database;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManagerTest {
	@Test
	public void defaultConstructorDoesNotThrow() {
		assertDoesNotThrow(() -> { new DatabaseManager(); });
	}

	@Test
	public void establishConnectionDoesNotReturnNull() {
		String url = DatabaseManager.MOCK_URL;
		Connection connection = DatabaseManager.establishConnection(url);

		assertNotNull(connection);
	}

	@Test
	public void establishConnectionNoException() {
		String url = DatabaseManager.MOCK_URL;
		assertDoesNotThrow(() -> { DatabaseManager.establishConnection(url); });
	}
}
