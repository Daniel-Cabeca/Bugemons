package ulb.repository.loader.json.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.text.ParseException;

import ulb.model.ability.Ability;
import ulb.model.type.Type;
import ulb.repository.loader.LoadFailureException;
import ulb.repository.loader.json.Json;

public class AbilityJsonParserTest {
	public static Ability parseAbilityFromString(String str) {
		AbilityJsonParser parser = new AbilityJsonParser();
		JsonNode node = Json.getNode(str);
		return parser.parseOne(node);
	}

	@Test
	public void testOneCorrect() {
		String str = """
			{
				"id": "fouet_liane",
				"nom": "Fouet-Liane",
				"type": "Flora",
				"description": "Inflige des dégâts et réduit légèrement la défense adverse.",
				"puissance": 40,
				"effets": [
				{
					"type": "stat_modifier",
					"cible": "adversaire",
					"stat": "defense",
					"modificateur": -5,
					"duree": "permanent"
				}
				]
			}
			""";

		Ability obtained = parseAbilityFromString(str);

		assertEquals("fouet_liane", obtained.getId());
		assertEquals("Fouet-Liane", obtained.getName());
		assertEquals(Type.FLORA, obtained.getType());
		assertEquals("Inflige des dégâts et réduit légèrement la défense adverse.", obtained.getDescription());
		assertEquals(40, obtained.getPower());
	}
}
