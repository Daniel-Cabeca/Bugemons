package ulb.repository.loader.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ulb.model.type.Type;import ulb.repository.loader.parser.TypeParser;

public class TypeParserTest {
	@Test
	public void testPyro() {
		assertEquals(Type.PYRO, TypeParser.fromString("Pyro"));
		assertEquals(Type.PYRO, TypeParser.fromString("pyro"));
	}

	@Test
	public void testFlora() {
		assertEquals(Type.FLORA, TypeParser.fromString("Flora"));
		assertEquals(Type.FLORA, TypeParser.fromString("flora"));
	}

	@Test
	public void testAqua() {
		assertEquals(Type.AQUA, TypeParser.fromString("Aqua"));
		assertEquals(Type.AQUA, TypeParser.fromString("aqua"));
	}

	@Test
	public void testLitho() {
		assertEquals(Type.LITHO, TypeParser.fromString("Litho"));
		assertEquals(Type.LITHO, TypeParser.fromString("litho"));
	}

	@Test
	public void testIncorrect() {
		assertThrows(IllegalArgumentException.class, () -> { TypeParser.fromString("ptdr"); });
	}
}
