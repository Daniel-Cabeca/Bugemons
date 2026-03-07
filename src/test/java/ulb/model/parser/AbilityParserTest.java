package ulb.model.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.text.ParseException;

import ulb.model.ability.Ability;
import ulb.model.ability.AbilityDatabase;
import ulb.model.type.Type;

import ulb.model.ability.AbilityDatabaseTest;

public class AbilityParserTest {
	private JsonNode getJsonNodeA() {
		try {
			String STR = """
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

			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(STR);

			return node;
		} catch (Exception e) {
			assertTrue(false);
			return null;
		}
	}

	private Ability getAbilityA() {
		return new Ability(
			"fouet_liane",
			"Fouet-Liane",
			Type.FLORA,
			"Inflige des dégâts et réduit légèrement la défense adverse.",
			40
		);
	}

	@Test
	public void testFromJsonCorrect() {
		try {
			JsonNode node = getJsonNodeA();
			Ability expected = getAbilityA();
			Ability obtained = AbilityParser.fromJson(node);

			assertEquals(expected.getId(), obtained.getId());
			assertEquals(expected.getName(), obtained.getName());
			assertEquals(expected.getType(), obtained.getType());
			assertEquals(expected.getDescription(), obtained.getDescription());
			assertEquals(expected.getPower(), obtained.getPower());
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	@Test
	public void testFromJsonParseError() {
		try {
			String STR = """
				{
					"id": "fouet_liane",
					"nom": "Fouet-Liane",
					"type": "mdr",
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

			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(STR);

			assertThrows(ParseException.class, () -> { AbilityParser.fromJson(node); });
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	@Test
	public void testLoadDatabase() {
		try {
			AbilityDatabase database = AbilityDatabaseTest.newDatabase();
			AbilityParser.load(database);

			assertTrue(database.exists("fouet_liane"));
		} catch (Exception e) {
			assertTrue(false);
		}
	}
}
