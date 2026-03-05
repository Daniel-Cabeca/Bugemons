package ulb.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import ulb.model.item.Item;
import ulb.model.item.Inventory;

public class PlayerTest {
	@Test
	public void testAddDefaultItems() {
		Player player = new Player();
		Inventory inventory = player.getInventory();
		Map<Item, Integer> items = inventory.getItems();

		int baiesRevigorantes = 0;
		int baiesToniques = 0;
		int gelsDefensifs = 0;
		int serumsOffensifs = 0;

		for (Map.Entry<Item, Integer> entry : items.entrySet()) {
			Item item = entry.getKey();
			int quantity = entry.getValue();

			switch (item.getName()) {
				case "Baie Revigorante":
					baiesRevigorantes = quantity;
					break;
				case "Baie Tonique":
					baiesToniques = quantity;
					break;
				case "Gel Défensif":
					gelsDefensifs = quantity;
					break;
				case "Sérum Offensif":
					serumsOffensifs = quantity;
					break;
				default:
					// no other item should not be in the default inventory
					break;
			}
		}

		assertEquals(3, baiesRevigorantes);
		assertEquals(2, baiesToniques);
		assertEquals(1, gelsDefensifs);
		assertEquals(1, serumsOffensifs);
		// Ensure only the default items are present
		assertEquals(4, items.size());
	}
}
