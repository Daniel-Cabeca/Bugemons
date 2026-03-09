package ulb.repository.loader.json;

import java.net.URL;
import java.io.InputStream;
import java.io.IOException;

import ulb.repository.loader.LoadException;

/**
 * Gives access to the default json resource files.
 */
public abstract class JsonResources {
	public static final String PATH_BUGEMON_SPECIES	= "/json/bugemons.json";
	public static final String PATH_ABILITIES		= "/json/attaques.json";
	public static final String PATH_ITEMS			= "/json/objets.json";

	public static URL getUrl(String path) throws LoadException {
		URL url = JsonResources.class.getResource(path);

		if (url == null) {
			throw new LoadException("Resource not found: "+ path);
		}

		return url;
	}

	public static InputStream getStream(String path) throws LoadException {
		try {
			URL url = JsonResources.getUrl(path);
			return url.openStream();
		} catch(IOException e) {
			throw new LoadException(e.getMessage());
		}
	}
}
