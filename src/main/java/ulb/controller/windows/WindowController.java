package ulb.controller.windows;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.DTO.player.PlayerDTO;
import ulb.message.ClientToServerMessage;
import ulb.view.FxmlLoader;
import ulb.exceptions.ViewLoadException;

import java.io.Serializable;
import java.util.logging.Logger;


/**
 * Abstract class that represent a WindowController
 *
 * @param <T> ViewWindow of the windowController
 */
public abstract class WindowController<T> {
    protected Stage stage;
    protected T view;
    protected String windowPath;
    private FXMLLoader loader;
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

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

    public interface ClientListener{
        Serializable onGetData(ClientToServerMessage message);
        boolean onPostData(ClientToServerMessage message);
        PlayerDTO onGetPlayerDTO();
        PlayerDTO onLoadPlayer(String userName);
        void onShowWindow(WindowName window);
    }
    public enum WindowName{
        REGISTER, MODE,GAME_MODE, ATTACK_REPLACEMENT, BATTLE, BATTLE_END,
        CHOOSE_BUGEMEON,CONFIRM_TEAM,FLOOR, FLOOR_REWARD, LEVEL_UP, LOAD_TEAM,
        NEXT_ROOM, SOCIAL_PANEL, TEAM, WAIT
    }
}
