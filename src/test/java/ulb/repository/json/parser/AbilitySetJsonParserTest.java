package ulb.repository.json.parser;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import ulb.exceptions.LoadException;
import ulb.model.ability.AbilitySet;
import ulb.repository.AbilityRepository;
import ulb.repository.json.Json;
import ulb.repository.mock.AbilityMockRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AbilitySetJsonParserTest {
	@Test
	public void testTooFew() throws Exception {
		String str = """
				["fouet_liane", "pollen_sournois"]
				""";

		assertThrows(LoadException.class, () -> {
			getFromString(str);
		});
	}

	public static AbilitySet getFromString(String str) throws LoadException {
		AbilityRepository abilityRepository = new AbilityMockRepository();
		AbilitySetJsonParser parser = new AbilitySetJsonParser(abilityRepository);

		JsonNode node = Json.getNode(str);
		return parser.parseOne(node);
	}

	@Test
	public void testTooMany() throws Exception {
		String str = """
				["fouet_liane", "pollen_sournois", "racines_vives", "racines_vives"]
				""";

		assertThrows(LoadException.class, () -> {
			getFromString(str);
		});
	}

	@Test
	public void testJsonCorrect() throws Exception {
		String str = """
				["fouet_liane", "pollen_sournois", "racines_vives"]
				""";

		AbilitySet abilitySet = getFromString(str);

		assertEquals("fouet_liane", abilitySet.getAbility(0).getId());
		assertEquals("pollen_sournois", abilitySet.getAbility(1).getId());
		assertEquals("racines_vives", abilitySet.getAbility(2).getId());
	}

	@Test
	public void testJsonFakeAbility() throws Exception {
		String str = """
				["fouet_liane", "pollen_sournois", "doesnotexist"]
				""";

		assertThrows(LoadException.class, () -> {
			getFromString(str);
		});
	}
}
