package ulb.repository.database;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManagerTest {
	@Test
	public void testDefaultConstructorDoesNotThrow() {
		assertDoesNotThrow(() -> { new DatabaseManager(); });
	}
}
