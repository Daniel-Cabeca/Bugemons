package ulb.model.item;

import org.junit.jupiter.api.Test;
import ulb.repository.ItemRepository;
import ulb.repository.mock.ItemMockRepository;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
	@Test
	public void addItemFunctionnality() throws Exception {
		ItemRepository repository = new ItemMockRepository();
		Item item = repository.findById("baie_tonique");

		Inventory inventory = new Inventory();
		inventory.addItem(item, 3);
		assertTrue(inventory.getItems().containsKey(item));
		assertEquals(3, (int) inventory.getItems().get(item));
	}

	@Test
	public void removeItemFunctionnality() throws Exception {
		ItemRepository repository = new ItemMockRepository();
		Item item = repository.findById("baie_tonique");

		Inventory inventory = new Inventory();
		inventory.addItem(item, 3);
		inventory.removeItem(item);
		assertEquals(2, (int) inventory.getItems().get(item));
		inventory.removeItem(item);
		inventory.removeItem(item);
		assertFalse(inventory.getItems().containsKey(item));
	}

	@Test
	public void getQuantityOfItemNotInInventory() throws Exception {
		ItemRepository itemRepository = new ItemMockRepository();
		Item item = itemRepository.findById("baie_revigorante");

		Inventory inventory = new Inventory();
		assertEquals(0, inventory.getQuantity(item));
	}

	@Test
	public void getQuantityOfItemInInventory() throws Exception {
		ItemRepository itemRepository = new ItemMockRepository();
		Item item = itemRepository.findById("baie_revigorante");

		Inventory inventory = new Inventory();
		inventory.addItem(item, 5);

		assertEquals(5, inventory.getQuantity(item));
	}
}
