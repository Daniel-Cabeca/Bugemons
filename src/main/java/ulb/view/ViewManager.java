package ulb.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.communication.Message;
import ulb.communication.types.*;
import ulb.controller.GameController;
import ulb.view.windows.BattleWindow;
import ulb.view.windows.Window;

import java.io.IOException;

public abstract class ViewManager {
    private Stage stage;
    private GameController controller;

    public void setGameController(GameController gameController) { this.controller = gameController; }

    public void setStage(Stage stageNew){this.stage = stageNew;}

    public GameController getGameController() {
        return this.controller;
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
                window.setViewManager(this);
                if (window instanceof BattleWindow battleWindow) {
                    battleWindow.setTowerManager(controller.getTowerManager());
                }
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

    /**
     * Handles all messages received from a window by sending to the controller and getting its response when needed
     * @param message the message received from a window
     */
    public Message handleMessage(Message message) {
        Message controllerResponse = this.controller.handleMessage(message);

        if (controllerResponse == null) return null;

        MessageType messageType = controllerResponse.getMessageType();
        switch (messageType) {
            case SWITCH_WINDOW:
                this.switchWindow(((SwitchWindowMessage) controllerResponse).getSwitchWindow());
                return null;
            case SETUP_GAME_MODE, AUTO_TURN_RESPONSE, REWARD_PLACE, LEVEL_UP, BATTLE_END_INFO:
                return controllerResponse;
            default:
                return null;
        }
    }
}
