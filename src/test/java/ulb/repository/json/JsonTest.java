package ulb.repository.json;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import ulb.exceptions.LoadException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsonTest {
	@Test
	public void testGetNodeCorrect() throws Exception {
		String str = """
				{ "property": "value" }
				""";

		JsonNode node = Json.getNode(str);
		assertEquals(node.get("property").asText(), "value");
	}

	@Test
	public void testGetNodeException() throws Exception {
		String str = """
				{ property": "value" }
				""";

		assertThrows(LoadException.class, () -> {
			Json.getNode(str);
		});
	}
}
