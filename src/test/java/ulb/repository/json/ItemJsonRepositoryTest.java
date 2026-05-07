package ulb.repository.json;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.io.ByteArrayInputStream;

import ulb.model.item.Inventory;

class ItemJsonRespositoryTest {
	@Test
	public void testLoadOneItem() throws Exception {
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
		ItemJsonRepository repository = new ItemJsonRepository(stream);

		assertDoesNotThrow(() -> { repository.findById("baie_revigorante"); });
	}

	@Test
	public void testLoadStartingInventory() throws Exception {
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
		ItemJsonRepository repository = new ItemJsonRepository(stream);
		Inventory startingInventory = repository.getStartingInventory();

		assertEquals(3, startingInventory.getQuantity(repository.findById("baie_revigorante")));
	}

	@Test
	public void testDefaultDoesNotThrow() throws Exception {
		assertDoesNotThrow(() -> { new AbilityJsonRepository(); });
	}
}
