package ulb.repository.json;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.io.ByteArrayInputStream;

import ulb.model.item.Item;
import ulb.model.item.Inventory;

class InventoryJsonRespositoryTest {
	@Test
	public void testStartingInventoryWasCloned() {
		String str = """
			{
				"objets": [
					{
					"id": "baie_revigorante",
					"nom": "Baie Revigorante",
					"description": "Restaure 20 PV au Bugémon actif.",
					"categorie": "soin",
					"effet": {
						"type": "soin",
						"cible": "lanceur",
						"valeur": 20
					},
					"sprite": "baie_revigorante.png"
					}
				],
				"inventaire_depart": {
					"baie_revigorante": 3
				}
			}
			""";

		InputStream stream = new ByteArrayInputStream(str.getBytes());
		ItemJsonRepository itemRepository = new ItemJsonRepository(stream);
		InventoryJsonRepository inventoryRepository = new InventoryJsonRepository(itemRepository);

		Inventory startingA = inventoryRepository.findStartingInventory();
		Inventory startingB = inventoryRepository.findStartingInventory();
		Item item = itemRepository.findById("baie_revigorante");

		startingA.removeItem(item);
		assertEquals(3, startingB.getQuantity(item));
	}
}
