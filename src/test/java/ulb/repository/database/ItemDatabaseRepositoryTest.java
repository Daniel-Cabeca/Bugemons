package ulb.repository.database;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import ulb.model.item.Item;
import ulb.repository.database.sql.Database;
import ulb.repository.database.sql.DatabaseMock;

public class ItemDatabaseRepositoryTest {
	@Test
	public void findByIdGivesCorrectItem() {
		ItemDatabaseRepository repository = new ItemDatabaseRepository(new DatabaseMock());

		String id = "baie_revigorante";
		Item obtained = repository.findById(id);
		assertEquals(id, obtained.getId());
	}
}
