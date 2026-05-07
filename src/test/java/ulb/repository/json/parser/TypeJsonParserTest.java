package ulb.repository.json.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;

import ulb.model.type.Type;
import ulb.exceptions.LoadException;
import ulb.repository.json.Json;

public class TypeJsonParserTest {
	public static Type parseTypeFromString(String str) throws LoadException {
		TypeJsonParser parser = new TypeJsonParser();
		JsonNode node = Json.getNode("\""+ str +"\"");
		return parser.parseOne(node);
	}

	@Test
	public void testIncorrect() throws Exception {
		assertThrows(LoadException.class, () -> { parseTypeFromString("ptdr"); });
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
