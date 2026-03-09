package ulb.repository.loader.json;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.InputStream;

import ulb.repository.loader.ItemLoader;
import ulb.repository.loader.json.parser.ItemJsonParser;
import ulb.model.item.Item;
import ulb.repository.loader.LoadException;

public class ItemJsonLoader implements ItemLoader {
	private final InputStream stream;
	private final ItemJsonParser itemParser = new ItemJsonParser();

	public ItemJsonLoader(InputStream stream) {
		this.stream = stream;
	}

	@Override
	public Iterable<Item> loadAll() throws LoadException {
		JsonNode node = Json.getNode(this.stream);
		JsonNode itemsArray = node.get("objets");

		return this.itemParser.parseList(itemsArray);
	}
}
