package ulb.repository.json.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import ulb.repository.json.Json;

import ulb.model.item.Item;

public class ItemJsonParserTest {
	public static Item parseItemFromString(String str) {
		ItemJsonParser parser = new ItemJsonParser();
		JsonNode node = Json.getNode(str);
		return parser.parseOne(node);
	}

	private static Item getMockItemA() {
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

	@Test
	public void parsesId() {
		Item item = getMockItemA();
		assertEquals("baie_revigorante", item.getId());
	}

	@Test
	public void parsesName() {
		Item item = getMockItemA();
		assertEquals("Baie Revigorante", item.getName());
	}

	@Test
	public void parsesDescription() {
		Item item = getMockItemA();
		assertEquals("Restaure 20 PV au Bugémon actif.", item.getDescription());
	}

	@Test
	public void parsesCategory() {
		Item item = getMockItemA();
		assertEquals("soin", item.getCategory());
	}

	@Test
	public void parsesSprite() {
		Item item = getMockItemA();
		assertEquals("baie_revigorante.png", item.getSprite());
	}
}
