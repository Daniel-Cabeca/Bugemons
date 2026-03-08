package ulb.repository.loader.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.InputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.net.URL;

import ulb.repository.loader.LoadFailureException;

/**
 * Utility class for parsing json inputs into json nodes.
 */
public abstract class Json {
	/**
	 * Parses a JsonNode from an input stream.
	 *
	 * @param stream The input stream
	 * @return The json node
	 * @throws LoadFailureException If an error occured
	 */
	public static JsonNode getNode(InputStream stream) throws LoadFailureException {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(stream);

			return node;
		} catch (JsonProcessingException e) {
			throw new LoadFailureException(e.getMessage());
		} catch (IOException e) {
			throw new LoadFailureException(e.getMessage());
		}
	}

	/**
	 * Convenience method for parsing from a String object.
	 *
	 * @param stream The input string
	 * @return The json node
	 * @throws LoadFailureException If an error occured
	 */
	public static JsonNode getNode(String str) throws LoadFailureException {
		InputStream stream = new ByteArrayInputStream(str.getBytes());
		return getNode(stream);
	}

	/**
	 * Convenience method for parsing from a URL.
	 *
	 * @param path The url to read from
	 * @return The json node
	 * @throws LoadFailureException If an error occured
	 */
	public static JsonNode getNode(URL url) throws LoadFailureException {
		try {
			InputStream stream = url.openStream();
			return getNode(stream);
		} catch (IOException e) {
			throw new LoadFailureException(e.getMessage());
		}
	}
}
