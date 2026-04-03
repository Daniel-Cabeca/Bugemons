package ulb.repository.database;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ItemDatabaseRepositoryTest {
	@Test
	public void defaultConstructorDoesNotThrow() {
		DatabaseManager manager = new DatabaseManager();

		assertDoesNotThrow(() -> { new ItemDatabaseRepository(manager.getConnection()); });
	}
}
