package ulb.controller.windows;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.view.FxmlLoader;


/**
 * Abstract class that represent a WindowController
 *
 * @param <T> Window of the windowController
 */
abstract class WindowController<T> {
    private Stage stage;
    private String windowPath;
    private FXMLLoader loader;
    private T view;


    public void setStage(Stage stage){ this.stage = stage; }
    protected void setWindowPath(String windowPath) { this.windowPath = windowPath; }
    protected T getView() { return this.view; }
    protected Stage getStage(){ return this.stage; }

    /**
     * Used to initiate a WindowController object
     */
    protected void init(){
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
