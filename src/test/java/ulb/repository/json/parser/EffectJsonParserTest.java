package ulb.repository.json.parser;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import ulb.exceptions.LoadException;
import ulb.model.effect.*;
import ulb.repository.json.Json;

import static org.junit.jupiter.api.Assertions.*;

public class EffectJsonParserTest {
	@Test
	public void testParseTargetError() throws Exception {
		assertThrows(LoadException.class, () -> {
			parseEffectTargetFromStr("doesnotexist");
		});
	}

	public static EffectTarget parseEffectTargetFromStr(String str) throws LoadException {
		EffectJsonParser parser = new EffectJsonParser();
		JsonNode node = Json.getNode("\"" + str + "\"");
		return parser.parseTarget(node);
	}

	@Test
	public void testParseTargetLanceur() throws Exception {
		assertEquals(EffectTarget.OWN_BUGEMON, parseEffectTargetFromStr("lanceur"));
	}

	@Test
	public void testParseTargetAdversaire() throws Exception {
		assertEquals(EffectTarget.OPPOSITE_BUGEMON, parseEffectTargetFromStr("adversaire"));
	}

	@Test
	public void testParseTargetEquipe() throws Exception {
		assertEquals(EffectTarget.OWN_TEAM, parseEffectTargetFromStr("equipe"));
	}

	// Target

	@Test
	public void testParseDurationError() throws Exception {
		assertThrows(LoadException.class, () -> {
			parseEffectStatDurationFromStr("doesnotexist");
		});
	}

	public static EffectStatDuration parseEffectStatDurationFromStr(String str) throws LoadException {
		EffectJsonParser parser = new EffectJsonParser();
		JsonNode node = Json.getNode("\"" + str + "\"");
		return parser.parseDuration(node);
	}

	@Test
	public void testParseDurationPermanent() throws Exception {
		assertEquals(EffectStatDuration.PERMANENT, parseEffectStatDurationFromStr("permanent"));
	}

	@Test
	public void testParseDurationOneTurn() throws Exception {
		assertEquals(EffectStatDuration.ROUND, parseEffectStatDurationFromStr("tour"));
		assertEquals(EffectStatDuration.ROUND, parseEffectStatDurationFromStr("1_tour"));
	}

	// Duration

	@Test
	public void testParseEffectStatTypeError() throws Exception {
		assertThrows(LoadException.class, () -> {
			parseEffectStatTypeFromStr("doesnotexist");
		});
	}

	public static EffectStatType parseEffectStatTypeFromStr(String str) throws LoadException {
		EffectJsonParser parser = new EffectJsonParser();
		JsonNode node = Json.getNode("\"" + str + "\"");
		return parser.parseEffectStatType(node);
	}

	@Test
	public void testParseEffectStatTypeHp() throws Exception {
		assertEquals(EffectStatType.HP, parseEffectStatTypeFromStr("pv"));
	}

	// Stat type

	@Test
	public void testParseEffectStatTypeAttack() throws Exception {
		assertEquals(EffectStatType.ATTACK, parseEffectStatTypeFromStr("attaque"));
	}

	@Test
	public void testParseEffectStatTypeDefense() throws Exception {
		assertEquals(EffectStatType.DEFENSE, parseEffectStatTypeFromStr("defense"));
	}

	@Test
	public void testParseEffectStatTypeInitiative() throws Exception {
		assertEquals(EffectStatType.INITIATIVE, parseEffectStatTypeFromStr("initiative"));
	}

	@Test
	public void testParseOneUnknownType() throws Exception {
		String str = """
				{
					"type": "doesnotexist"
				}
				""";

		assertThrows(LoadException.class, () -> {
			parseEffectFromStr(str);
		});
	}

	public static Effect parseEffectFromStr(String str) throws LoadException {
		EffectJsonParser parser = new EffectJsonParser();
		JsonNode node = Json.getNode(str);
		return parser.parseOne(node);
	}

	// ParseOne

	@Test
	public void testParseOneHeal() throws Exception {
		String str = """
				{
					"type": "soin",
					"cible": "lanceur",
					"valeur": 70
				}
				""";

		Effect obtained = parseEffectFromStr(str);

		assertInstanceOf(EffectHeal.class, obtained);
		assertEquals(EffectTarget.OWN_BUGEMON, obtained.getTarget());
	}

	@Test
	public void testParseOneStatModifier() throws Exception {
		String str = """
				{
					"type": "stat_modifier",
					"cible": "lanceur",
					"stat": "initiative",
					"modificateur": 10,
					"duree": "permanent"
				}
				""";

		Effect obtained = parseEffectFromStr(str);
		EffectStatModifier obtainedStatModifier = (EffectStatModifier) obtained;

		assertInstanceOf(EffectStatModifier.class, obtained);
		assertEquals(EffectStatDuration.PERMANENT, obtainedStatModifier.getDuration());
		assertEquals(10, obtainedStatModifier.getModifiers().get(EffectStatType.INITIATIVE));
	}

	@Test
	public void testParseOneStatModifierMultiple() throws Exception {
		String str = """
				{
					"type": "stat_modifier_multiple",
					"cible": "lanceur",
					"modificateurs": {
						"attaque": 10,
						"initiative": 10
					},
					"duree": "tour"
				}
				""";

		Effect obtained = parseEffectFromStr(str);
		EffectStatModifier obtainedStatModifier = (EffectStatModifier) obtained;

		assertInstanceOf(EffectStatModifier.class, obtained);
		assertEquals(EffectStatDuration.ROUND, obtainedStatModifier.getDuration());
		assertEquals(10, obtainedStatModifier.getModifiers().get(EffectStatType.ATTACK));
		assertEquals(10, obtainedStatModifier.getModifiers().get(EffectStatType.INITIATIVE));
	}

	@Test
	public void testParseOneResetMalus() throws Exception {
		String str = """
				{
					"type": "reset_malus",
					"cible": "lanceur"
				}
				""";

		Effect obtained = parseEffectFromStr(str);

		assertInstanceOf(EffectResetMalus.class, obtained);
		assertEquals(EffectTarget.OWN_BUGEMON, obtained.getTarget());
	}

	@Test
	public void parseListCorrectSize() throws Exception {
		String str = """
				[
					{
						"type": "stat_modifier",
						"cible": "lanceur",
						"stat": "defense",
						"modificateur": 15,
						"duree": "permanent"
					},
					{
						"type": "stat_modifier",
						"cible": "adversaire",
						"stat": "defense",
						"modificateur": -6,
						"duree": "permanent"
					}
				]
				""";

		EffectList obtained = parseEffectListFromStr(str);

		assertEquals(2, obtained.getSize());
	}

	// ParseList

	public static EffectList parseEffectListFromStr(String str) throws LoadException {
		EffectJsonParser parser = new EffectJsonParser();
		JsonNode node = Json.getNode(str);
		return parser.parseList(node);
	}
}
