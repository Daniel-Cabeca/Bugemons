package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.model.Player;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;
import ulb.view.WindowPath;
import ulb.view.windows.CreateTeamWindow;
import ulb.controller.GameController;

import java.util.ArrayList;
import java.util.List;

public class TeamController implements CreateTeamWindow.ViewListener {
	private Player player;

	private final Listener listener;
	private final GameController gameController;
	private CreateTeamWindow view;
	private Stage stage;

	public TeamController(Stage stage, Listener listener, Player player, GameController gameController) {
		this.stage = stage;
		this.listener = listener;
		this.player = player;
		this.gameController = gameController;
	}

	public void show() throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.CREATE_TEAM));
		loader.load();
		view = loader.getController();
		view.setViewListener(this);
		view.setGameController(gameController);

		Parent root = loader.getRoot();
		if (stage.getScene() == null) {
			stage.setScene(new Scene(root));
		} else {
			stage.getScene().setRoot(root);
		}
		this.stage.show();
	}

	public void setTeam(List<String> selectedBugemons){
		List<Bugemon> teamABugemons = new ArrayList<Bugemon>();
		for (String bugemon : selectedBugemons) {
			teamABugemons.add(new Bugemon(bugemon.toLowerCase()));
		}
		Team playerTeam = new Team(teamABugemons);
		player.setTeam(playerTeam);
	}

	@Override
	public void onConfirmTeam(List<String> selectedBugemons) {
		setTeam(selectedBugemons);
		listener.onTeamConfirmed();
	}

	@Override
	public void onReturn() {
		listener.onReturn();
	}

	public interface Listener {
		void onTeamConfirmed();
		void onReturn();
	}
}
