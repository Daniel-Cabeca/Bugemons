package ulb.model.item;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ulb.model.effect.Effect;
import ulb.model.sample.EffectSample;

import ulb.repository.ItemRepository;
import ulb.repository.mock.ItemMockRepository;

public class InventoryTest {
	@Test
	public void addItemFunctionnality() {
		Effect baieEffect = EffectSample.getHeal();
		Item item = new Item("baie_tonique", "Baie Tonique", 
		"Restaure 10 PV au Bugémon actif.", "soin", 
		baieEffect, "baie_tonique.png");
		Inventory inventory = new Inventory();
		inventory.addItem(item, 3);
		assertTrue(inventory.getItems().containsKey(item));
		assertEquals(3, (int) inventory.getItems().get(item));
	}

	@Test
	public void removeItemFunctionnality() {
		Effect baieEffect = EffectSample.getHeal();
		Item item = new Item("baie_tonique", "Baie Tonique", 
		"Restaure 10 PV au Bugémon actif.", "soin", 
		baieEffect, "baie_tonique.png");
		Inventory inventory = new Inventory();
		inventory.addItem(item, 3);
		inventory.removeItem(item);
		assertEquals(2, (int) inventory.getItems().get(item));
		inventory.removeItem(item);
		inventory.removeItem(item);
		assertFalse(inventory.getItems().containsKey(item));
	}

	@Test
	public void getQuantityOfItemNotInInventory() {
		ItemRepository itemRepository = new ItemMockRepository();
		Item item = itemRepository.findById("baie_revigorante");

		Inventory inventory = new Inventory();
		assertEquals(0, inventory.getQuantity(item));
	}

	@Test
	public void getQuantityOfItemInInventory() {
		ItemRepository itemRepository = new ItemMockRepository();
		Item item = itemRepository.findById("baie_revigorante");

		Inventory inventory = new Inventory();
		inventory.addItem(item, 5);

		assertEquals(5, inventory.getQuantity(item));
	}
}
