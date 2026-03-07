package ulb.repository.loader.json;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;

import ulb.model.Effect;
import ulb.model.Effect.EffectType;
import ulb.repository.loader.LoadFailureException;

public class EffectJsonParserTest {
	public static EffectType parseEffectTypeFromStr(String str) {
		EffectJsonParser parser = new EffectJsonParser();
		JsonNode node = Json.getNode("\""+ str +"\"");
		return parser.parseEffectType(node);
	}

	public static Effect parseEffectFromStr(String str) {
		EffectJsonParser parser = new EffectJsonParser();
		JsonNode node = Json.getNode(str);
		return parser.parseOne(node);
	}

	@Test
	public void testEffectTypeIncorrect() {
		assertThrows(LoadFailureException.class, () -> { parseEffectTypeFromStr("test"); });
	}

	@Test
	public void testEffectTypeSoin() {
		assertEquals(EffectType.SOIN, parseEffectTypeFromStr("soin"));
	}

	@Test
	public void testEffectTypeStatModifier() {
		assertEquals(EffectType.STAT_MODIFIER, parseEffectTypeFromStr("stat_modifier"));
	}

	@Test
	public void testParseOneCorrect() {
		String str = """
			{
				"type": "stat_modifier",
				"cible": "adversaire",
				"stat": "defense",
				"modificateur": -5,
				"duree": "permanent"
			}
			""";

		Effect obtained = parseEffectFromStr(str);
		assertNotNull(obtained);
	}
}
