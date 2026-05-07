package ulb.repository.json.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import ulb.repository.json.Json;

import ulb.exceptions.LoadException;
import ulb.repository.ItemRepository;
import ulb.repository.mock.ItemMockRepository;

import ulb.model.item.Inventory;

public class InventoryJsonParserTest {
	public static Inventory parseOneFromString(String str) throws LoadException {
		ItemRepository itemRepository = new ItemMockRepository();
		JsonNode node = Json.getNode(str);
		InventoryJsonParser parser = new InventoryJsonParser(itemRepository);

		return parser.parseOne(node);
	}

	@Test
	public void testParseUnknownItemThrows() throws Exception {
		String str = """
			{
				"doesnotexist": 3
			}
			""";
		assertThrows(LoadException.class, () -> { parseOneFromString(str); });
	}

	@Test
	public void testParseCorrect() throws Exception {
		String str = """
			{
				"baie_revigorante": 3,
				"baie_tonique": 2,
				"gel_defensif": 1,
				"serum_offensif": 1
			}
			""";

		ItemRepository itemRepository = new ItemMockRepository();
		Inventory obtained = parseOneFromString(str);

		assertEquals(3, obtained.getQuantity(itemRepository.findById("baie_revigorante")));
		assertEquals(2, obtained.getQuantity(itemRepository.findById("baie_tonique")));
		assertEquals(1, obtained.getQuantity(itemRepository.findById("gel_defensif")));
		assertEquals(1, obtained.getQuantity(itemRepository.findById("serum_offensif")));
	}
}
