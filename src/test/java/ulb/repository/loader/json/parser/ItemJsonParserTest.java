package ulb.repository.loader.json.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import ulb.repository.loader.json.Json;

import ulb.model.Effect;
import ulb.model.item.Item;



//TODO remove imports below

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;


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



	//TODO change, move or remove tests below

	/**
	* Gets the resource file as an absolute path
	*
	* @param valid boolean indicating which resource file to use
	* @return the absolute path of the resource file
	* @throws Exception if the resource cannot be found, or if the resource URL cannot be converted to a URI
	*/
	private String getResourcePath(boolean valid) throws Exception {
		// Uses a separate test JSON to avoid modifying or depending on the real data
		if (valid) {
			return new File(getClass().getResource("/json/objets_test.json").toURI()).getAbsolutePath();
		} else {
			return new File(getClass().getResource("/json/objets_invalid_effect.json").toURI()).getAbsolutePath();
		}
	}

	@Test
	public void loadStartingInventoryReturnsNonNull() throws Exception {
		String path = getResourcePath(true);
		Map<String, Integer> inventory = ItemJsonParser.loadInventory(path);
		assertNotNull(inventory);
	}

	@Test
	public void loadStartingInventoryReturnsCorrectSize() throws Exception {
		String path = getResourcePath(true);
		Map<String, Integer> inventory = ItemJsonParser.loadInventory(path);
		assertEquals(4, inventory.size());
	}

	@Test
	public void loadStartingInventoryParsesQuantity() throws Exception {
		String path = getResourcePath(true);
		Map<String, Integer> inventory = ItemJsonParser.loadInventory(path);
		assertEquals(Integer.valueOf(3), inventory.get("baie_revigorante"));
		assertEquals(Integer.valueOf(2), inventory.get("baie_tonique"));
		assertEquals(Integer.valueOf(1), inventory.get("gel_defensif"));
		assertEquals(Integer.valueOf(1), inventory.get("serum_offensif"));
	}

	@Test
	public void loadStartingInventoryThrowsOnInvalidPath() {
		assertThrows(IOException.class, () -> ItemJsonParser.loadInventory("/nonexistent/path/to/file.json"));
	}
}
