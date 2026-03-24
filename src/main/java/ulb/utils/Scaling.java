package ulb.utils;

import javafx.scene.Scene;
import javafx.scene.layout.Region;

public class Scaling {

    private static final double BASE_WIDTH = 1000;
    private static final double BASE_HEIGHT = 700;

    public static void applyScaling(Region content) {

        content.sceneProperty().addListener((obs, oldScene, scene) -> {
            if (scene != null) {
                scene.widthProperty().addListener((o, oldVal, newVal) ->
                        scale(content, scene));
                scene.heightProperty().addListener((o, oldVal, newVal) ->
                        scale(content, scene));
                scale(content, scene);
            }
        });
    }

    private static void scale(Region content, Scene scene) {
        double scaleX = scene.getWidth() / BASE_WIDTH;
        double scaleY = scene.getHeight() / BASE_HEIGHT;

        double scale = Math.min(scaleX, scaleY);

        content.setScaleX(scale);
        content.setScaleY(scale);
    }
}