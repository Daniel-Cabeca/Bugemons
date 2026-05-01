package ulb.repository.json.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import ulb.repository.json.Json;

import ulb.model.effect.Effect;
import ulb.model.effect.EffectList;
import ulb.model.effect.EffectHeal;
import ulb.model.effect.EffectResetMalus;
import ulb.model.effect.EffectStatModifier;
import ulb.model.effect.EffectSwitch;
import ulb.model.effect.EffectTarget;
import ulb.model.effect.EffectStatDuration;
import ulb.model.effect.EffectStatType;

import ulb.exceptions.LoadException;

public class EffectJsonParserTest {
	public static Effect parseEffectFromStr(String str) {
		EffectJsonParser parser = new EffectJsonParser();
		JsonNode node = Json.getNode(str);
		return parser.parseOne(node);
	}

	public static EffectTarget parseEffectTargetFromStr(String str) {
		EffectJsonParser parser = new EffectJsonParser();
		JsonNode node = Json.getNode("\""+ str +"\"");
		return parser.parseTarget(node);
	}

	public static EffectStatDuration parseEffectStatDurationFromStr(String str) {
		EffectJsonParser parser = new EffectJsonParser();
		JsonNode node = Json.getNode("\""+ str +"\"");
		return parser.parseDuration(node);
	}

	public static EffectStatType parseEffectStatTypeFromStr(String str) {
		EffectJsonParser parser = new EffectJsonParser();
		JsonNode node = Json.getNode("\""+ str +"\"");
		return parser.parseEffectStatType(node);
	}

	public static EffectList parseEffectListFromStr(String str) {
		EffectJsonParser parser = new EffectJsonParser();
		JsonNode node = Json.getNode(str);
		return parser.parseList(node);
	}

	// Target

	@Test
	public void testParseTargetError() {
		assertThrows(LoadException.class, () -> { parseEffectTargetFromStr("doesnotexist"); });
	}

	@Test
	public void testParseTargetLanceur() {
		assertEquals(EffectTarget.OWN_BUGEMON, parseEffectTargetFromStr("lanceur"));
	}

	@Test
	public void testParseTargetAdversaire() {
		assertEquals(EffectTarget.OPPOSITE_BUGEMON, parseEffectTargetFromStr("adversaire"));
	}

	@Test
	public void testParseTargetEquipe() {
		assertEquals(EffectTarget.OWN_TEAM, parseEffectTargetFromStr("equipe"));
	}

	// Duration

	@Test
	public void testParseDurationError() {
		assertThrows(LoadException.class, () -> { parseEffectStatDurationFromStr("doesnotexist"); });
	}

	@Test
	public void testParseDurationPermanent() {
		assertEquals(EffectStatDuration.PERMANENT, parseEffectStatDurationFromStr("permanent"));
	}

	@Test
	public void testParseDurationOneTurn() {
		assertEquals(EffectStatDuration.ROUND, parseEffectStatDurationFromStr("tour"));
		assertEquals(EffectStatDuration.ROUND, parseEffectStatDurationFromStr("1_tour"));
	}

	// Stat type

	@Test
	public void testParseEffectStatTypeError() {
		assertThrows(LoadException.class, () -> { parseEffectStatTypeFromStr("doesnotexist"); });
	}

	@Test
	public void testParseEffectStatTypeHp() {
		assertEquals(EffectStatType.HP, parseEffectStatTypeFromStr("pv"));
	}

	@Test
	public void testParseEffectStatTypeAttack() {
		assertEquals(EffectStatType.ATTACK, parseEffectStatTypeFromStr("attaque"));
	}

	@Test
	public void testParseEffectStatTypeDefense() {
		assertEquals(EffectStatType.DEFENSE, parseEffectStatTypeFromStr("defense"));
	}

	@Test
	public void testParseEffectStatTypeInitiative() {
		assertEquals(EffectStatType.INITIATIVE, parseEffectStatTypeFromStr("initiative"));
	}

	// ParseOne

	@Test
	public void testParseOneUnknownType() {
		String str = """
			{
				"type": "doesnotexist"
			}
			""";

		assertThrows(LoadException.class, () -> { parseEffectFromStr(str); });
	}

	@Test
	public void testParseOneHeal() {
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
	public void testParseOneStatModifier() {
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
	public void testParseOneStatModifierMultiple() {
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
	public void testParseOneResetMalus() {
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
	public void testParseOneSwitch() {
		String str = """
			{
				"type": "switch",
				"cible": "lanceur"
			}
			""";

		Effect obtained = parseEffectFromStr(str);

		assertInstanceOf(EffectSwitch.class, obtained);
		assertEquals(EffectTarget.OWN_BUGEMON, obtained.getTarget());
	}

	// ParseList

	@Test
	public void parseListCorrectSize() {
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
}
