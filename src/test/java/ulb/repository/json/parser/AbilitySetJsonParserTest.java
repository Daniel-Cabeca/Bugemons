package ulb.repository.json.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import ulb.repository.json.Json;

import ulb.model.ability.AbilitySet;

import ulb.repository.AbilityRepository;
import ulb.repository.mock.AbilityMockRepository;
import ulb.repository.LoadException;

public class AbilitySetJsonParserTest {
	public static AbilitySet getFromString(String str) {
		AbilityRepository abilityRepository = new AbilityMockRepository();
		AbilitySetJsonParser parser = new AbilitySetJsonParser(abilityRepository);

		JsonNode node = Json.getNode(str);
		return parser.parseOne(node);
	}

	@Test
	public void testTooFew() {
		String str = """
			["fouet_liane", "pollen_sournois"]
			""";

		assertThrows(LoadException.class, () -> { getFromString(str); });
	}

	@Test
	public void testTooMany() {
		String str = """
			["fouet_liane", "pollen_sournois", "racines_vives", "racines_vives"]
			""";

		assertThrows(LoadException.class, () -> { getFromString(str); });
	}

	@Test
	public void testJsonCorrect() {
		String str = """
			["fouet_liane", "pollen_sournois", "racines_vives"]
			""";

		AbilitySet abilitySet = getFromString(str);

		assertEquals("fouet_liane", abilitySet.getAbility(0).getId());
		assertEquals("pollen_sournois", abilitySet.getAbility(1).getId());
		assertEquals("racines_vives", abilitySet.getAbility(2).getId());
	}

	@Test
	public void testJsonFakeAbility() {
		String str = """
			["fouet_liane", "pollen_sournois", "doesnotexist"]
			""";

		assertThrows(LoadException.class, () -> { getFromString(str); });
	}
}
