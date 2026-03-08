package ulb.repository.loader.json;

import com.fasterxml.jackson.databind.JsonNode;

import ulb.model.type.Type;
import ulb.repository.loader.LoadFailureException;

class TypeJsonParser {
	public Type parseOne(JsonNode node) throws LoadFailureException {
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
				throw new LoadFailureException("Unrecognized type.");
		}
	}
}
