package ulb.repository.inmemory;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.NoSuchElementException;

import ulb.model.ability.Ability;
import ulb.repository.loader.json.Json;
import ulb.repository.loader.json.parser.AbilityJsonParser;

public class AbilityInMemoryRepositoryTest {
	public static Ability getAbilityFromString(String jsonString) {
		JsonNode node = Json.getNode(jsonString);
		AbilityJsonParser parser = new AbilityJsonParser();

		return parser.parseOne(node);
	}

	public static List<Ability> getMockDataCorrect() {
		String strA = """
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

		String strB = """
			{
				"id": "pollen_sournois",
				"nom": "Pollen Sournois",
				"type": "Flora",
				"description": "Inflige des dégâts et réduit l'initiative adverse au prochain tour.",
				"puissance": 35,
				"effets": [
					{
					"type": "stat_modifier",
					"cible": "adversaire",
					"stat": "initiative",
					"modificateur": -10,
					"duree": "1_tour"
					}
				]
			}
			""";

		Ability abilityA = getAbilityFromString(strA);
		Ability abilityB = getAbilityFromString(strB);

		List<Ability> abilities = new ArrayList<>();
		abilities.add(abilityA);
		abilities.add(abilityB);

		return abilities;
	}

	public static List<Ability> getMockDataDuplicates() {
		String strA = """
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

		Ability abilityA = getAbilityFromString(strA);
		Ability abilityB = getAbilityFromString(strA);

		List<Ability> abilities = new ArrayList<>();
		abilities.add(abilityA);
		abilities.add(abilityB);

		return abilities;
	}

	@Test
	public void testFindNotPresent() {
		AbilityInMemoryRepository abilityRepository = new AbilityInMemoryRepository();

		assertThrows(NoSuchElementException.class, () -> { abilityRepository.findById("doesnotexist"); });
	}

	@Test
	public void testAddCorrect() {
		ArrayList<Ability> abilities = new ArrayList<Ability>(getMockDataCorrect());
		AbilityInMemoryRepository abilityRepository = new AbilityInMemoryRepository();

		abilityRepository.add(abilities);

		assertEquals(abilities.get(0), abilityRepository.findById(abilities.get(0).getId()));
	}

	@Test
	public void testAddDuplicates() {
		ArrayList<Ability> abilities = new ArrayList<Ability>(getMockDataDuplicates());
		AbilityInMemoryRepository abilityRepository = new AbilityInMemoryRepository();

		assertThrows(IllegalArgumentException.class, () -> { abilityRepository.add(abilities); });
	}
}
