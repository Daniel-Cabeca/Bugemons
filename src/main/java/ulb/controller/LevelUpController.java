package ulb.controller;

import ulb.view.windows.LevelUpWindow;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.reward.RewardDTO;
import ulb.view.WindowPath;

/**
 * Controller for the level up reward selection screen.
 */
public class LevelUpController implements LevelUpWindow.ViewListener{


    private final Listener listener;
    private LevelUpWindow view;
    private Stage stage;

    /**
     * Creates the level up controller.
     *
     * @param stage The application stage
     * @param listener The listener providing level up data and callbacks
     */
    public LevelUpController(Stage stage, Listener listener) {
        this.stage = stage;
        this.listener = listener;
    }

    /**
     * Displays the level up screen with current rewards.
     *
     * @throws Exception If the FXML cannot be loaded
     */
    public void show() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.LEVEL_UP));
        loader.load();

        view = loader.getController();
        view.setViewListener(this);

		List<RewardDTO> rewards = listener.getLevelUpRewards();
		BugemonDTO bugemon = rewards.get(0).getBugemon();

        view.initializeView(bugemon, rewards);

        Parent root = loader.getRoot();
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        this.stage.show();
    }

    /**
     * Handles reward selection.
     *
     * @param reward The selected reward
     * @param event The triggering UI action event
     */
    @Override
    public void onRewardChosen(RewardDTO reward, ActionEvent event) {
        listener.onRewardChosen(reward, event);
    }

    /**
     * Listener for level up data and actions.
     */
    public interface Listener {
        List<RewardDTO> getLevelUpRewards();
        void onRewardChosen(RewardDTO reward, ActionEvent event);
    }
}
