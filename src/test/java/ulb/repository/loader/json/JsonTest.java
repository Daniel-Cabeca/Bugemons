package ulb.repository.loader.json;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;

import ulb.repository.loader.LoadException;

public class JsonTest {
	@Test
	public void testGetNodeCorrect() {
		String str = """
			{ "property": "value" }
			""";

		JsonNode node = Json.getNode(str);
		assertEquals(node.get("property").asText(), "value");
	}

	@Test
	public void testGetNodeException() {
		String str = """
			{ property": "value" }
			""";

		assertThrows(LoadException.class, () -> { Json.getNode(str); });
	}
}
