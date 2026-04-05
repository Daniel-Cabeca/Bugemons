package ulb.repository.database;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import ulb.model.ability.Ability;
import ulb.model.item.Item;
import ulb.repository.database.sql.Database;
import ulb.repository.database.sql.DatabaseMock;


public class AbilityDatabaseRepositoryTest {


	@Test
	public void findByIdGivesCorrectAbility() {
		AbilityDatabaseRepository repository = new AbilityDatabaseRepository(new DatabaseMock());

		String id = "surchauffe";
		Ability obtained = repository.findById(id);
		assertEquals(id, obtained.getId());
	}
}
