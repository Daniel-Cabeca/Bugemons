package ulb.model.parser;

import ulb.model.type.Type;

public abstract class TypeParser {
	public static Type fromString(String str) throws IllegalArgumentException {
		switch (str.toLowerCase()) {
			case "pyro":
				return Type.PYRO;
			case "flora":
				return Type.FLORA;
			case "aqua":
				return Type.AQUA;
			case "litho":
				return Type.LITHO;
			default:
				throw new IllegalArgumentException("Invalid type.");
		}
	}
}
