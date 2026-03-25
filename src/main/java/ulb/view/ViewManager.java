package ulb.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.controller.GameController;
import ulb.view.windows.Window;

import java.io.IOException;

public class ViewManager {
    protected static final String WINDOWS_PATH= "/ulb/view/";
    protected static final String MODE_WINDOW_PATH = WINDOWS_PATH + "ModeWindow.fxml";

    private final Stage stage;
    private final GameController controller;

    public ViewManager(Stage stage, GameController controller) {
        this.stage = stage;
        this.controller = controller;
    }

    /**
     * Switches to the window by loading the fxml file and setting the controller
     *
     * @param windowFxmlPath the path of the fxml file of the window
     */
    public void switchWindow(String windowFxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(windowFxmlPath));
            Parent root = loader.load();

            if (loader.getController() instanceof Window window) {
                window.setGameController(controller);
                window.onLoad();
            }

            if (stage.getScene() == null) {
                Scene scene = new Scene(root);
                stage.setScene(scene);
            } else {
                stage.getScene().setRoot(root);
            }

        } catch (IOException e) {
            System.err.println("Error: Could not load window " + windowFxmlPath);
            e.printStackTrace();
        }
    }
}
