
package ulb.controller.windows;

import java.io.IOException;
import java.io.Serializable;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.communication.Message;
import ulb.communication.message.serverToClient.StatusMessage;


/**
 * Abstract class that is the backbone of all window controllers
 * @param <T> is the respective view class
 */
abstract class WindowController<T> {
    protected Stage stage;
    protected String windowPath;
    private FXMLLoader loader;
    protected T view;


    /**
     * @throws Exception if something wrong goes with loading the fxml file
     */
    protected void init() throws IOException{
        loader = new FXMLLoader(getClass().getResource(this.windowPath));
        loader.load();
        view = loader.getController();
    }


    /**
     * Change the current fxml window
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
