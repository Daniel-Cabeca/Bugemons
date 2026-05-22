package ulb.repository.json.parser;

import com.fasterxml.jackson.databind.JsonNode;
import ulb.exceptions.LoadException;
import ulb.model.type.Type;

/**
 * Object that parses types from JSON nodes.
 */
public class TypeJsonParser {
	/**
	 * Returns one Type enum value from a JSON node.
	 *
	 * @param node The JSON node
	 * @return The Type enum value
	 * @throws LoadException If the type is not recognized
	 */
	public Type parseOne(JsonNode node) throws LoadException {
		String str = node.asText();
		str = str.toLowerCase();

		switch (str) {
			case "pyro":
				return Type.PYRO;
			case "flora":
				return Type.FLORA;
			case "aqua":
				return Type.AQUA;
			case "litho":
				return Type.LITHO;
			default:
				throw new LoadException("Unrecognized type.");
		}
	}
}
