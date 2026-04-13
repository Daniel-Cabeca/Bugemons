package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.view.WindowPath;
import ulb.view.windows.ChooseBugemonWindow;

public class ChooseBugemonController implements ChooseBugemonWindow.ViewListener {

    private final Listener listener;
    private final Stage stage;
    private final PlayerDTO player;

    private ChooseBugemonWindow view;

    public ChooseBugemonController(Stage stage, Listener listener, PlayerDTO player) {
        this.stage = stage;
        this.listener = listener;
        this.player = player;
    }

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

    @Override
    public void onBugemonChosen(BugemonDTO bugemon) {
        listener.onBugemonChosen(bugemon);
    }

        @Override
    public void onReturnFloorRewardWindow() {
        listener.onReturnFloorRewardWindow();
    }

	public interface Listener {
		void onBugemonChosen(BugemonDTO bugemon);
		void onReturnFloorRewardWindow();
    }
}
