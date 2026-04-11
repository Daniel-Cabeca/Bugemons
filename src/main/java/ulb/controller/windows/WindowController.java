package ulb.controller.windows;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class WindowController<T> {

    private Stage stage;
    private FXMLLoader loader;
    private T view;

    protected WindowController(Stage stage, String windowPath) {
        this.stage = stage;
        try {
            this.loader = new FXMLLoader(getClass().getResource(windowPath));
            this.loader.load();
            this.view = loader.getController();
        } catch (IOException e) {
            throw new IllegalStateException("Could not load FXML file: " + windowPath, e);
        }
    }

    protected final Stage getStage() {
        return stage;
    }

    protected final T getView() {
        return view;
    }

    public void show() {
        Parent root = loader.getRoot();
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        stage.show();
    }
}
