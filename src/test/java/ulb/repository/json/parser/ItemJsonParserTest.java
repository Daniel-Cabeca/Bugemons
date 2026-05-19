package ulb.repository.json.parser;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import ulb.exceptions.LoadException;
import ulb.model.item.Item;
import ulb.repository.json.Json;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemJsonParserTest {
	@Test
	public void parsesId() throws Exception {
		Item item = getMockItemA();
		assertEquals("baie_revigorante", item.getId());
	}

	private static Item getMockItemA() throws LoadException {
		String str = """
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
				""";

		return parseItemFromString(str);
	}

	// ParseOne

	public static Item parseItemFromString(String str) throws LoadException {
		ItemJsonParser parser = new ItemJsonParser();
		JsonNode node = Json.getNode(str);
		return parser.parseOne(node);
	}

	@Test
	public void parsesName() throws Exception {
		Item item = getMockItemA();
		assertEquals("Baie Revigorante", item.getName());
	}

	@Test
	public void parsesDescription() throws Exception {
		Item item = getMockItemA();
		assertEquals("Restaure 20 PV au Bugémon actif.", item.getDescription());
	}

	@Test
	public void parsesCategory() throws Exception {
		Item item = getMockItemA();
		assertEquals("soin", item.getCategory());
	}

	@Test
	public void parsesSprite() throws Exception {
		Item item = getMockItemA();
		assertEquals("baie_revigorante.png", item.getSprite());
	}
}
