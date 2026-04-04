package ulb.repository.database;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionTest {
	@Test
	public void isOpenAfterOpen() {
		DatabaseConnection dbConnection = new DatabaseConnection("jdbc:sqlite::memory:");
		dbConnection.open();
		assertTrue(dbConnection.isOpen());

		dbConnection.close();
	}

	@Test
	public void openThrowsIfAlreadyOpen() {
		DatabaseConnection dbConnection = new DatabaseConnection("jdbc:sqlite::memory:");
		dbConnection.open();
		assertThrows(IllegalStateException.class, () -> { dbConnection.open(); });
	}

	@Test
	public void isNotOpenAfterClose() {
		DatabaseConnection dbConnection = new DatabaseConnection("jdbc:sqlite::memory:");
		dbConnection.open();
		dbConnection.close();
		assertFalse(dbConnection.isOpen());
	}

	@Test
	public void closeDoesNotThrowIfNotOpen() {
		DatabaseConnection dbConnection = new DatabaseConnection("jdbc:sqlite::memory:");
		assertDoesNotThrow(() -> { dbConnection.close(); });
	}
}
