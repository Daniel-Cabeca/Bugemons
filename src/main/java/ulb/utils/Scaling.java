package ulb.utils;

import javafx.scene.Scene;
import javafx.scene.layout.Region;

/**
 * Utility class for scaling UI content based on scene size.
 */
public class Scaling {

	private static final double BASE_WIDTH = 1000;
	private static final double BASE_HEIGHT = 700;

	/**
	 * Applies dynamic scaling to a region.
	 *
	 * @param content The UI region to scale
	 */
	public static void applyScaling(Region content) {

		content.sceneProperty().addListener((obs, oldScene, scene) -> {
			if (scene != null) {
				scene.widthProperty().addListener((o, oldVal, newVal) -> scale(content, scene));
				scene.heightProperty().addListener((o, oldVal, newVal) -> scale(content, scene));
				scale(content, scene);
			}
		});
	}

	/**
	 * Updates the scale of the region based on scene size.
	 *
	 * @param content The UI region
	 * @param scene The scene
	 */
	private static void scale(Region content, Scene scene) {
		double scaleX = scene.getWidth() / BASE_WIDTH;
		double scaleY = scene.getHeight() / BASE_HEIGHT;

		double scale = Math.min(scaleX, scaleY);

		content.setScaleX(scale);
		content.setScaleY(scale);
	}
}