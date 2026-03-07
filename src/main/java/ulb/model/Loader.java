package ulb.model;

import java.io.IOException;
import java.text.ParseException;

import ulb.repository.loader.json.AbilityParser;
import ulb.repository.loader.json.BugemonParser;

public abstract class Loader {
	public static void load() throws IOException, ParseException {
		AbilityParser.load();
		BugemonParser.load();
	}
}
