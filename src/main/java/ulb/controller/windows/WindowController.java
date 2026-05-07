package ulb.controller.windows;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.view.FxmlLoader;
import ulb.exceptions.ViewLoadException;


/**
 * Abstract class that represent a WindowController
 *
 * @param <T> Window of the windowController
 */
abstract class WindowController<T> {
    protected Stage stage;
    protected T view;
    protected String windowPath;
    private FXMLLoader loader;

    protected WindowController(Stage stage, String windowPath) throws ViewLoadException {
        this.stage = stage;
        this.windowPath = windowPath;
        this.init();
    }

    protected T getView() { return this.view; }
    protected Stage getStage(){ return this.stage; }
    /**
     * Used to initiate a WindowController object
     */
    protected void init() throws ViewLoadException {
        loader = FxmlLoader.load(this, this.windowPath);
        view = loader.getController();
    }

    /**
     * Changes the fxml window to the current fxml window
     */
    public void show() {
        Parent root = loader.getRoot();
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        this.stage.show();
    }

}
