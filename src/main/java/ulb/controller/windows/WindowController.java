package ulb.controller.windows;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.DTO.player.PlayerDTO;
import ulb.communication.GameMode;
import ulb.message.ClientToServerMessage;
import ulb.view.FxmlLoader;
import ulb.exceptions.ViewLoadException;

import java.io.Serializable;
import java.util.logging.Level;
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
    protected final Logger LOGGER = Logger.getLogger(this.getClass().getName());
    protected ClientListener clientListener;

    /**
     * A super constructor for all windowControlls
     * @param stage used to show fxml windows
     * @param windowPath the fxml path of the viewWindow
     * @param clientListener attribute that communicate with the clientContoller
     */
    protected WindowController(Stage stage, String windowPath, ClientListener clientListener){
        this.stage = stage;
        this.windowPath = windowPath;
        try {
            this.loadView();
        } catch (ViewLoadException e){
            LOGGER.log(Level.WARNING, "\u001B[31m" + "Impossible d'afficher l'écran de " + this.getClass().getName() + "\u001B[0m", e);
        }
        this.clientListener = clientListener;
    }

    protected T getView() { return this.view; }
    protected Stage getStage(){ return this.stage; }
    /**
     * Read the fxml file and load the view window
     */
    protected void loadView() throws ViewLoadException {
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

    /**
     * ClientListener is a way to communicate between WindowControllers and the ClientController
     */
    public interface ClientListener{
        Serializable onGetData(ClientToServerMessage message);
        boolean onPostData(ClientToServerMessage message);
        PlayerDTO onGetPlayerDTO(String userName);
        PlayerDTO onLoadPlayer(String userName);
        void onLogOut();
        void onShowWindow(WindowName window);
        void onSetGameMode(GameMode gameMode);
    }

    /**
     * WindowName enum is used by showWindow function to show the right window
     * Each name represent an FXML ViewWindow to show
     */
    public enum WindowName{
        REGISTER, MODE,GAME_MODE, ATTACK_REPLACEMENT, BATTLE, BATTLE_END,
        CHOOSE_BUGEMEON,CONFIRM_TEAM,FLOOR, FLOOR_REWARD, LEVEL_UP, LOAD_TEAM,
        NEXT_ROOM, SOCIAL_PANEL, TEAM, WAIT
    }
}
