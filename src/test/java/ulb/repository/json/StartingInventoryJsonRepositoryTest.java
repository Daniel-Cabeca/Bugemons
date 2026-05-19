package ulb.repository.json;

import org.junit.jupiter.api.Test;
import ulb.model.item.Inventory;
import ulb.model.item.Item;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StartingInventoryJsonRespositoryTest {
	@Test
	public void testStartingInventoryWasCloned() throws Exception {
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
		StartingInventoryJsonRepository startingInventoryJsonRepository =
				new StartingInventoryJsonRepository(itemRepository);

		Inventory startingA = startingInventoryJsonRepository.findStartingInventory();
		Inventory startingB = startingInventoryJsonRepository.findStartingInventory();
		Item item = itemRepository.findById("baie_revigorante");

		startingA.removeItem(item);
		assertEquals(3, startingB.getQuantity(item));
	}
}
