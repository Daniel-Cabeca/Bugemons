package ulb.repository.json.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import ulb.repository.json.Json;

import ulb.model.effect.Effect;
import ulb.model.effect.Effect.EffectType;
import ulb.model.effect.Effect.EffectTarget;
import ulb.model.effect.Effect.EffectDuration;
import ulb.model.effect.Effect.StatType;

import ulb.repository.LoadException;

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

	public static EffectDuration parseEffectDurationFromStr(String str) {
		EffectJsonParser parser = new EffectJsonParser();
		JsonNode node = Json.getNode("\""+ str +"\"");
		return parser.parseDuration(node);
	}

	public static StatType parseStatTypeFromStr(String str) {
		EffectJsonParser parser = new EffectJsonParser();
		JsonNode node = Json.getNode("\""+ str +"\"");
		return parser.parseStatType(node);
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
		assertThrows(LoadException.class, () -> { parseEffectDurationFromStr("doesnotexist"); });
	}

	@Test
	public void testParseDurationPermanent() {
		assertEquals(EffectDuration.PERMANENT, parseEffectDurationFromStr("permanent"));
	}

	@Test
	public void testParseDurationOneTurn() {
		assertEquals(EffectDuration.ROUND, parseEffectDurationFromStr("tour"));
		assertEquals(EffectDuration.ROUND, parseEffectDurationFromStr("1_tour"));
	}

	// Stat type

	@Test
	public void testParseStatTypeError() {
		assertThrows(LoadException.class, () -> { parseStatTypeFromStr("doesnotexist"); });
	}

	@Test
	public void testParseStatTypeHp() {
		assertEquals(StatType.HP, parseStatTypeFromStr("pv"));
	}

	@Test
	public void testParseStatTypeAttack() {
		assertEquals(StatType.ATTACK, parseStatTypeFromStr("attaque"));
	}

	@Test
	public void testParseStatTypeDefense() {
		assertEquals(StatType.DEFENSE, parseStatTypeFromStr("defense"));
	}

	@Test
	public void testParseStatTypeInitiative() {
		assertEquals(StatType.INITIATIVE, parseStatTypeFromStr("initiative"));
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

		assertEquals(EffectType.HEAL, obtained.getType());
		assertEquals(EffectDuration.PERMANENT, obtained.getDuration());
		assertEquals(70, obtained.getModifiers().get(Effect.StatType.HP));
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

		assertEquals(EffectType.STAT_MODIFIER, obtained.getType());
		assertEquals(10, obtained.getModifiers().get(Effect.StatType.INITIATIVE));
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

		assertEquals(EffectType.STAT_MODIFIER, obtained.getType());
		assertEquals(10, obtained.getModifiers().get(Effect.StatType.ATTACK));
		assertEquals(10, obtained.getModifiers().get(Effect.StatType.INITIATIVE));
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

		assertEquals(EffectType.RESET_MALUS, obtained.getType());
		assertEquals(EffectDuration.ROUND, obtained.getDuration());
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

		assertEquals(EffectType.SWITCH, obtained.getType());
		assertEquals(EffectDuration.ROUND, obtained.getDuration());
	}
}
