package ulb.repository.json.parser;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;
import ulb.model.bugemon.BugemonSpecies;
import ulb.repository.AbilityRepository;
import ulb.repository.json.Json;
import ulb.repository.mock.AbilityMockRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BugemonSpeciesJsonParserTest {
	@Test
	public void testFromJsonCorrect() throws Exception {
		String str = """
				{
					"id": "florachu",
					"nom": "Florachu",
					"type": "Flora",
					"stats": {
						"pv": 90,
						"attaque": 55,
						"defense": 40,
						"initiative": 50
					},
					"attaques": ["fouet_liane", "pollen_sournois", "racines_vives"],
					"sprite": "florachu.png",
					"starter": true
				}
				""";

		BugemonSpecies species = parseOneFromString(str);

		assertEquals("florachu", species.getId());
		assertEquals("Florachu", species.getName());
		assertEquals(90, species.getHp());
		assertEquals(55, species.getAttack());
		assertEquals(40, species.getDefense());
		assertEquals(50, species.getInitiative());
		assertEquals("florachu.png", species.getSprite());
		assertEquals("/png/florachu.png", species.getSpritePath());
		assertEquals(true, species.isStarter());
	}

	public static BugemonSpecies parseOneFromString(String str) throws LoadException, EntityNotFoundException {
		BugemonSpeciesJsonParser parser = getParser();

		JsonNode node = Json.getNode(str);
		return parser.parseOne(node);
	}

	public static BugemonSpeciesJsonParser getParser() throws LoadException, EntityNotFoundException {
		AbilityRepository abilityRepository = new AbilityMockRepository();
		abilityRepository.findById("pollen_sournois");
		return new BugemonSpeciesJsonParser(abilityRepository);
	}
}
