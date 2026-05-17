package ulb.view;

import javafx.fxml.FXMLLoader;
import ulb.exceptions.ViewLoadException;

import java.io.IOException;
import java.net.URL;

/**
 * Utility class for loading JavaFX FXML files in a consistent way.
 */
public final class FxmlLoader {
	private FxmlLoader() {}

	/**
	 * Loads an FXML file and wraps loading problems into a ViewLoadException.
	 *
	 * @param owner Object used to resolve the FXML resource
	 * @param path FXML resource path
	 * @return Loaded FXMLLoader
	 */
	public static FXMLLoader load(Object owner, String path) throws ViewLoadException {
		if (owner == null) {
			throw new IllegalArgumentException("Owner cannot be null.");
		}

		URL url = owner.getClass().getResource(path);
		if (url == null) {
			throw new ViewLoadException("FXML introuvable : " + path);
		}

		FXMLLoader loader = new FXMLLoader(url);
		try {
			loader.load();
			return loader;
		} catch (IOException e) {
			throw new ViewLoadException("Impossible de charger le FXML : " + path);
		}
	}
}
