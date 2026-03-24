package ulb.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewManager {
    protected static final String WINDOWS_PATH= "/ulb/view/";
    protected static final String MODE_WINDOW_PATH = WINDOWS_PATH + "ModeWindow.fxml";



    public void switchWindow(String windowFxmlPath, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(windowFxmlPath));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }
}
