
package ulb.controller.windows;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.controller.ClientController;


/**
 * Abstract class that is the backbone of all window controllers
 * @param <T> is the respective view class
 */
abstract class WindowController<T> {
    protected Stage stage;
    protected String windowPath;
    protected ClientController clientController;
    private FXMLLoader loader;
    protected T view;

    /**
     * Common and mandatory constructor to all WindowControllers
     * @param stage is the primaryStage to change the javafx window
     * @param windowPath is the path of the fxml window file
     * @param clientController
     */
    protected WindowController(Stage stage, String windowPath, ClientController clientController){
        this.stage = stage;
        this.windowPath = windowPath;
        this.clientController = clientController;
        try {
            this.init();
        } catch (Exception e) {
            System.err.println("Couldn't load the FXML file : " + e);
        }
    }


    /**
     * Load the fxml file of its respective window
     * @throws Exception if something wrong goes with loading the fxml file
     */
    protected void init() throws IOException{
        loader = new FXMLLoader(getClass().getResource(this.windowPath));
        loader.load();
        view = loader.getController();
    }


    /**
     * Change the current javafx window
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
