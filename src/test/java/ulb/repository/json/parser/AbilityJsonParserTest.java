package ulb.repository.json.parser;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import ulb.exceptions.LoadException;
import ulb.model.ability.Ability;
import ulb.model.type.Type;
import ulb.repository.json.Json;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AbilityJsonParserTest {
	@Test
	public void testOneCorrect() throws Exception {
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

	public static Ability parseAbilityFromString(String str) throws LoadException {
		AbilityJsonParser parser = new AbilityJsonParser();
		JsonNode node = Json.getNode(str);
		return parser.parseOne(node);
	}
}
