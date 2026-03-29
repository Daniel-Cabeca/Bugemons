package ulb.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.communication.Message;
import ulb.communication.Messenger.AutoTurnResponseMessage;
import ulb.communication.types.SetupGameModeMessage;
import ulb.communication.types.SetupTeamMessage;
import ulb.communication.types.SwitchWindowMessage;
import ulb.controller.BattleController;
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
     * Handles all messages received from a window
     * @param message the message received from a window
     */
    public Message handleMessage(Message message) {
        Message controllerResponse = this.controller.handleMessage(message);
        if (controllerResponse instanceof SwitchWindowMessage) {
            this.switchWindow(((SwitchWindowMessage) controllerResponse).getSwitchWindow());
        }  else if (controllerResponse instanceof SetupGameModeMessage) {
            return controllerResponse;
        } else if (controllerResponse instanceof AutoTurnResponseMessage) {
            return controllerResponse;
        }
        return null;
    }

}
