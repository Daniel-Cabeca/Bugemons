package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.DTO.bugemon.CreateTeamBugemonDTO;
import ulb.model.Player;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.BugemonSpecies;
import ulb.model.team.Team;
import ulb.service.BugemonService;
import ulb.service.ServiceLoader;
import ulb.view.WindowPath;
import ulb.view.windows.CreateTeamWindow;

import java.util.ArrayList;
import java.util.List;

public class TeamController implements CreateTeamWindow.ViewListener {
	private final Player player;
	private final Listener listener;
	private final Stage stage;
	private final BugemonService bugemonService;

	private CreateTeamWindow view;

	public TeamController(Stage stage, Listener listener, Player player) {
		this.stage = stage;
		this.listener = listener;
		this.player = player;
		this.bugemonService = ServiceLoader.getBugemonService();
	}

	public void show() throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.CREATE_TEAM));
		loader.load();
		view = loader.getController();
		view.setViewListener(this);
		view.displayAvailableBugemons(getAvailableBugemons());

		Parent root = loader.getRoot();
		if (stage.getScene() == null) {
			stage.setScene(new Scene(root));
		} else {
			stage.getScene().setRoot(root);
		}
		this.stage.show();
	}

	private List<CreateTeamBugemonDTO> getAvailableBugemons() {
		List<CreateTeamBugemonDTO> availableBugemons = new ArrayList<>();
		for (BugemonSpecies bugemonSpecies : bugemonService.getAllSpecies()) {
			availableBugemons.add(new CreateTeamBugemonDTO(
					bugemonSpecies.getId(),
					bugemonSpecies.getName(),
					bugemonSpecies.getSprite()));
		}
		return availableBugemons;
	}

	public void setTeam(List<String> selectedBugemonIds) {
		List<Bugemon> teamABugemons = new ArrayList<>();
		for (String bugemonId : selectedBugemonIds) {
			teamABugemons.add(new Bugemon(bugemonId));
		}
		Team playerTeam = new Team(teamABugemons);
		player.setTeam(playerTeam);
	}

	@Override
	public void onConfirmTeam(List<String> selectedBugemonIds) {
		setTeam(selectedBugemonIds);
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
