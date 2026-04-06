package ulb.repository.database;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import ulb.model.ability.Ability;import ulb.model.effect.Effect;import ulb.model.effect.EffectHeal;import ulb.model.effect.EffectTarget;import ulb.model.item.Item;
import ulb.model.type.Type;import ulb.repository.database.sql.Database;
import ulb.repository.database.sql.DatabaseInMemory;import ulb.repository.database.sql.DatabaseInitializer;import ulb.repository.database.sql.DatabaseMock;import ulb.utils.DuplicateElementException;import java.util.stream.StreamSupport;

public class ItemDatabaseRepositoryTest {
	@Test
	public void findByIdGivesCorrectItem() {
		ItemDatabaseRepository repository = new ItemDatabaseRepository(new DatabaseMock());

		String id = "baie_revigorante";
		Item obtained = repository.findById(id);
		assertEquals(id, obtained.getId());
	}

	@Test
    	public void findAllGivesCorrectItems() throws DuplicateElementException {
    		Database database = new DatabaseInMemory();
    		DatabaseInitializer databaseInitializer = new DatabaseInitializer(database);
    		databaseInitializer.createTables();

    		ItemDatabaseRepository repository = new ItemDatabaseRepository(database);

			Effect effect = new EffectHeal(EffectTarget.OPPOSITE_BUGEMON,5);

    		Item item1 = new Item("1","potion","","",effect,"");
    		Item item2 = new Item("2","potion2","","",effect,"");

    		repository.insertItem(item1);
    		repository.insertItem(item2);

    		Iterable<Item> obtained = repository.findAll();

    		assertTrue(StreamSupport.stream(obtained.spliterator(), false)
                    .anyMatch(a -> a.getId().equals(item1.getId())));
    		assertTrue(StreamSupport.stream(obtained.spliterator(), false)
                    .anyMatch(a -> a.getId().equals(item1.getId())));
    	}
}
