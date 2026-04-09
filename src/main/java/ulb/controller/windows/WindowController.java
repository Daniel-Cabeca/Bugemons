
package ulb.controller.windows;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


abstract class WindowController<T> {
    private Stage stage;
    private String windowPath;
    private FXMLLoader loader;
    private T view;


    public void setStage(Stage stage){
        this.stage = stage;
    }
    protected Stage getStage(){
        return this.stage;
    }
    protected void setWindowPath(String windowPath) {
        this.windowPath = windowPath;
    }
    protected T getView() {
        return this.view;
    }

    protected void init() throws Exception{
        loader = new FXMLLoader(getClass().getResource(this.windowPath));
        loader.load();
        view = loader.getController();
    }

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
