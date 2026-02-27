package ulb.model;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class InventoryTest {
	@Test
	public void testAddItem() {
		Effect baieEffect = new Effect("soin", "lanceur", 50);
		Item item = new Item("baie_tonique", "Baie Tonique", 
		"Restaure 50 PV au Bugémon actif.", "soin", 
		baieEffect, "baie_tonique.png");
		Inventory inventory = new Inventory();
		inventory.addItem(item, 3);
		assertTrue(inventory.getItems().containsKey(item));
		assertEquals(3, (int) inventory.getItems().get(item));
	}

	@Test
	public void testRemoveItem() {
		Effect baieEffect = new Effect("soin", "lanceur", 50);
		Item item = new Item("baie_tonique", "Baie Tonique", 
		"Restaure 50 PV au Bugémon actif.", "soin", 
		baieEffect, "baie_tonique.png");
		Inventory inventory = new Inventory();
		inventory.addItem(item, 3);
		inventory.removeItem(item);
		assertEquals(2, (int) inventory.getItems().get(item));
		inventory.removeItem(item);
		inventory.removeItem(item);
		assertFalse(inventory.getItems().containsKey(item));
	}
}
