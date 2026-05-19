package ulb.repository.json.parser;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import ulb.exceptions.LoadException;
import ulb.model.type.Type;
import ulb.repository.json.Json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TypeJsonParserTest {
	@Test
	public void testIncorrect() throws Exception {
		assertThrows(LoadException.class, () -> {
			parseTypeFromString("ptdr");
		});
	}

	public static Type parseTypeFromString(String str) throws LoadException {
		TypeJsonParser parser = new TypeJsonParser();
		JsonNode node = Json.getNode("\"" + str + "\"");
		return parser.parseOne(node);
	}

	@Test
	public void testPyro() throws Exception {
		assertEquals(Type.PYRO, parseTypeFromString("pyro"));
	}

	@Test
	public void testFlora() throws Exception {
		assertEquals(Type.FLORA, parseTypeFromString("flora"));
	}

	@Test
	public void testAqua() throws Exception {
		assertEquals(Type.AQUA, parseTypeFromString("aqua"));
	}

	@Test
	public void testLitho() throws Exception {
		assertEquals(Type.LITHO, parseTypeFromString("litho"));
	}

	@Test
	public void testCase() throws Exception {
		assertEquals(Type.PYRO, parseTypeFromString("Pyro"));
		assertEquals(Type.PYRO, parseTypeFromString("PYRO"));
		assertEquals(Type.PYRO, parseTypeFromString("PyRO"));
	}
}
