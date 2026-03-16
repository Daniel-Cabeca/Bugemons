package ulb.repository.json.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import ulb.repository.json.Json;

import ulb.model.Effect;
import ulb.model.Effect.EffectType;
import ulb.model.Effect.EffectTarget;
import ulb.model.Effect.EffectDuration;

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

	// Target

	@Test
	public void testParseTargetError() {
		assertThrows(LoadException.class, () -> { parseEffectTargetFromStr("doesnotexist"); });
	}

	@Test
	public void testParseTargetLanceur() {
		assertEquals(EffectTarget.LANCEUR, parseEffectTargetFromStr("lanceur"));
	}

	@Test
	public void testParseTargetAdversaire() {
		assertEquals(EffectTarget.ADVERSAIRE, parseEffectTargetFromStr("adversaire"));
	}

	@Test
	public void testParseTargetEquipe() {
		assertEquals(EffectTarget.EQUIPE, parseEffectTargetFromStr("equipe"));
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
		assertEquals(EffectDuration.TOUR, parseEffectDurationFromStr("tour"));
		assertEquals(EffectDuration.TOUR, parseEffectDurationFromStr("1_tour"));
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
	public void testParseOneSoin() {
		String str = """
			{
				"type": "soin",
				"cible": "lanceur",
				"valeur": 70
			}
			""";

		Effect obtained = parseEffectFromStr(str);

		assertEquals(EffectType.SOIN, obtained.getType());
		assertEquals(EffectDuration.PERMANENT, obtained.getDuration());
		assertEquals(70, obtained.getModifiers().get(Effect.StatType.PV));
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
		assertEquals(10, obtained.getModifiers().get(Effect.StatType.ATTAQUE));
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
		assertEquals(EffectDuration.TOUR, obtained.getDuration());
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
		assertEquals(EffectDuration.TOUR, obtained.getDuration());
	}
}
