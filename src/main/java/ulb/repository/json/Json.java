package ulb.repository.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ulb.exceptions.LoadException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Utility class for parsing json inputs into json nodes.
 */
public abstract class Json {
	/**
	 * Convenience method for parsing from a String object.
	 *
	 * @param stream The input string
	 * @return The json node
	 * @throws LoadException If an error occured
	 */
	public static JsonNode getNode(String str) throws LoadException {
		InputStream stream = new ByteArrayInputStream(str.getBytes());
		return getNode(stream);
	}

	/**
	 * Parses a JsonNode from an input stream.
	 *
	 * @param stream The input stream
	 * @return The json node
	 * @throws LoadException If an error occured
	 */
	public static JsonNode getNode(InputStream stream) throws LoadException {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(stream);

			return node;
		} catch (JsonProcessingException e) {
			throw new LoadException(e.getMessage());
		} catch (IOException e) {
			throw new LoadException(e.getMessage());
		}
	}

	/**
	 * Convenience method for parsing from a URL.
	 *
	 * @param path The url to read from
	 * @return The json node
	 * @throws LoadException If an error occured
	 */
	public static JsonNode getNode(URL url) throws LoadException {
		try {
			InputStream stream = url.openStream();
			return getNode(stream);
		} catch (IOException e) {
			throw new LoadException(e.getMessage());
		}
	}
}
