package ulb.repository.loader.json.parser;

import com.fasterxml.jackson.databind.JsonNode;

import ulb.model.type.Type;
import ulb.repository.loader.LoadException;

public class TypeJsonParser {
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
