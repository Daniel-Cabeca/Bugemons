package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.model.Player;
import ulb.model.bugemon.Bugemon;
import ulb.view.WindowPath;
import ulb.view.windows.ChooseBugemonWindow;

public class ChooseBugemonController implements ChooseBugemonWindow.ViewListener {

	private final Listener listener;
	private Stage stage;
	private Player player;
	private ChooseBugemonWindow view;

	public ChooseBugemonController(Stage stage,Listener listener, Player player) {
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
        this.stage.show();
    }

    @Override
        public void onBugemonChosen(Bugemon bugemon) {
                listener.onBugemonChosen(bugemon);
        }

        @Override
    public void onReturnFloorRewardWindow() {
        listener.onReturnFloorRewardWindow();
    }

	public interface Listener {
		void onBugemonChosen(Bugemon bugemon);
		void onReturnFloorRewardWindow();
    }

}
