package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.view.FxmlLoader;
import ulb.exceptions.ViewLoadException;
import ulb.view.WindowPath;
import ulb.view.windows.BattleEndWindow;

/**
 * Controller for the battle end summary screen.
 */
public class BattleEndController implements BattleEndWindow.ViewListener {
    private Listener listener;
    private Stage stage;
    private BattleEndWindow view;

    /**
     * Creates the battle end controller.
     *
     * @param stage The application stage
     * @param listener Listener handling return action
     */
    public BattleEndController(Stage stage, Listener listener) {
        this.stage = stage;
        this.listener = listener;
    }

    /**
     * Displays battle result information.
     *
     * @param victory Whether the battle was won
     * @param totalXP Total experience gained
     */
    public void show(boolean victory, int totalXP, String opponent, boolean multiplayerBattle) throws ViewLoadException {
        FXMLLoader loader = FxmlLoader.load(this, WindowPath.BATTLE_END);
        view = loader.getController();
        this.view.setViewListener(this);

        Parent root = loader.getRoot();
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        this.view.setResult(victory, totalXP, opponent, multiplayerBattle);
        this.stage.show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onHandleReturn() {
        this.listener.onHandleReturn();
    }

    /**
     * Listener for battle end actions.
     */
    public interface Listener{
        /** Handles return action from the battle end window. */
        void onHandleReturn();
    }
}
