package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.model.Player;
import ulb.model.bugemon.Bugemon;
import ulb.view.WindowPath;
import ulb.view.windows.ChooseBugemonWindow;

/**
 * Controller for selecting a bugemon for a reward action.
 */
public class ChooseBugemonController implements ChooseBugemonWindow.ViewListener {

    private final Listener listener;
    private final Stage stage;
    private final Player player;

    private ChooseBugemonWindow view;

    /**
     * Creates the choose bugemon controller.
     *
     * @param stage The application stage
     * @param listener The listener notified of user selections
     * @param player The current player
     */
    public ChooseBugemonController(Stage stage, Listener listener, Player player) {
        this.stage = stage;
        this.listener = listener;
        this.player = player;
    }

    /**
     * Displays the choose bugemon screen.
     *
     * @throws Exception If the FXML cannot be loaded
     */
    public void show() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.CHOOSE_BUGEMON));
        loader.load();
        view = loader.getController();
        view.setViewListener(this);
        view.populatePlayerBugemons(player.getTeam());

        Parent root = loader.getRoot();
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        stage.show();
    }

    /**
     * Handles bugemon selection.
     *
     * @param bugemon The chosen bugemon
     */
    @Override
    public void onBugemonChosen(Bugemon bugemon) {
        listener.onBugemonChosen(bugemon);
    }

    /**
     * Handles returning to the floor reward window.
     */
    @Override
    public void onReturnFloorRewardWindow() {
        listener.onReturnFloorRewardWindow();
    }

    /**
     * Listener for choose bugemon actions.
     */
	public interface Listener {
		/**
		 * Called when a bugemon has been selected.
		 *
		 * @param bugemon The selected bugemon
		 */
		void onBugemonChosen(Bugemon bugemon);
		/** Called when returning to the floor reward window. */
		void onReturnFloorRewardWindow();
    }
}
