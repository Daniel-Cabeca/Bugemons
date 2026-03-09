package ulb.repository.loader.json.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;

import ulb.model.type.Type;
import ulb.repository.loader.LoadException;
import ulb.repository.loader.json.Json;

public class TypeJsonParserTest {
	public static Type parseTypeFromString(String str) {
		TypeJsonParser parser = new TypeJsonParser();
		JsonNode node = Json.getNode("\""+ str +"\"");
		return parser.parseOne(node);
	}

	@Test
	public void testIncorrect() {
		assertThrows(LoadException.class, () -> { parseTypeFromString("ptdr"); });
	}

	@Test
	public void testPyro() {
		assertEquals(Type.PYRO, parseTypeFromString("pyro"));
	}

	@Test
	public void testFlora() {
		assertEquals(Type.FLORA, parseTypeFromString("flora"));
	}

	@Test
	public void testAqua() {
		assertEquals(Type.AQUA, parseTypeFromString("aqua"));
	}

	@Test
	public void testLitho() {
		assertEquals(Type.LITHO, parseTypeFromString("litho"));
	}

	@Test
	public void testCase() {
		assertEquals(Type.PYRO, parseTypeFromString("Pyro"));
		assertEquals(Type.PYRO, parseTypeFromString("PYRO"));
		assertEquals(Type.PYRO, parseTypeFromString("PyRO"));
	}
}
