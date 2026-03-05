package ulb.model.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.text.ParseException;

import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.BugemonSpecies;
import ulb.model.bugemon.BugemonDatabase;
import ulb.model.bugemon.Stats;
import ulb.model.type.Type;
import ulb.model.ability.AbilitySet;
import ulb.model.ability.AbilityDatabase;

import ulb.model.sample.SamplesLoader;
import ulb.model.bugemon.BugemonDatabaseTest;

public class BugemonParserTest {
	@BeforeAll
	public static void load() throws Exception {
		SamplesLoader.load();
	}

	private static JsonNode getJsonNodeA() {
		try {
			String STR = """
				{
				"id": "a",
				"nom": "A",
				"type": "Flora",
				"stats": {
					"pv": 100,
					"attaque": 10,
					"defense": 10,
					"initiative": 10
				},
				"attaques": ["a", "b", "c"],
				"sprite": "florachu.png",
				"starter": true
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

	private static BugemonSpecies getBugemonSpeciesA() {
		return new BugemonSpecies(
			"a",
			"A",
			Type.FLORA,
			new Stats(100, 10, 10, 10),
			new AbilitySet(
				AbilityDatabase.getInstance().get("a"),
				AbilityDatabase.getInstance().get("b"),
				AbilityDatabase.getInstance().get("c")
			),
			"florachu.png",
			true
		);
	}

	@Test
	public void testFromJsonCorrect() {
		try {
			JsonNode node = getJsonNodeA();
			BugemonSpecies expected = getBugemonSpeciesA();
			BugemonSpecies obtained = BugemonParser.fromJson(node);

			assertEquals(expected.getId(), obtained.getId());
			assertEquals(expected.getName(), obtained.getName());
			assertEquals(expected.getType(), obtained.getType());
			assertEquals(expected.getBaseStats(), obtained.getBaseStats());
			assertEquals(expected.getAbilities(), obtained.getAbilities());
			assertEquals(expected.getSprite(), obtained.getSprite());
			assertEquals(expected.isStarter(), obtained.isStarter());
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	@Test
	public void testFromJsonParseError() {
		try {
			String STR = """
				{
				"id": "a",
				"nom": "A",
				"type": "mdr",
				"stats": {
					"pv": 100,
					"attaque": 10,
					"defense": 10,
					"initiative": 10
				},
				"attaques": ["a", "b", "c"],
				"sprite": "florachu.png",
				"starter": true
				}
				""";

			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(STR);

			assertThrows(ParseException.class, () -> { BugemonParser.fromJson(node); });
		} catch (Exception e) {
			assertTrue(false);
		}
	}
}
