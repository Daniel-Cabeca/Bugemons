package ulb.repository.mock;

import ulb.exceptions.LoadException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Gives access to the mock json resource files.
 */
public abstract class MockResources {
	public static final String PATH_BUGEMON_SPECIES = "/mock/bugemons.json";
	public static final String PATH_ABILITIES = "/mock/attaques.json";
	public static final String PATH_ITEMS = "/mock/objets.json";

	public static InputStream getStream(String path) throws LoadException {
		try {
			URL url = MockResources.getUrl(path);
			return url.openStream();
		} catch (IOException e) {
			throw new LoadException(e.getMessage());
		}
	}

	public static URL getUrl(String path) throws LoadException {
		URL url = MockResources.class.getResource(path);

		if (url == null) {
			throw new LoadException("Resource not found: " + path);
		}

		return url;
	}
}
