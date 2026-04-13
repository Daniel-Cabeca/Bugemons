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

public class LevelUpController implements LevelUpWindow.ViewListener{


    private final Listener listener;
    private LevelUpWindow view;
    private Stage stage;

    public LevelUpController(Stage stage, Listener listener) {
        this.stage = stage;
        this.listener = listener;
    }

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

    @Override
    public void onRewardChosen(RewardDTO reward, ActionEvent event) {
        listener.onRewardChosen(reward, event);
    }

    public interface Listener {
        List<RewardDTO> getLevelUpRewards();
        void onRewardChosen(RewardDTO reward, ActionEvent event);
    }
}
