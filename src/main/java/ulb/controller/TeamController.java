package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.bugemon.BugemonSpeciesDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.DTO.bugemon.CreateTeamBugemonDTO;
import ulb.model.Player;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.BugemonSpecies;
import ulb.model.team.Team;
import ulb.service.BugemonService;
import ulb.view.WindowPath;
import ulb.view.windows.CreateTeamWindow;

import java.util.ArrayList;
import java.util.List;

public class TeamController implements CreateTeamWindow.ViewListener {
	private PlayerDTO player;

	private final Listener listener;
	private final Stage stage;

	private CreateTeamWindow view;

	public TeamController(Stage stage, Listener listener, PlayerDTO player, BugemonService bugemonService) {
		this.stage = stage;
		this.listener = listener;
		this.player = player;
		this.bugemonService = bugemonService;
	}

	public BugemonService getBugemonService() { return this.bugemonService; }

	public void show() throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.CREATE_TEAM));
		loader.load();
		view = loader.getController();
		view.setViewListener(this);
		view.displayAvailableBugemons(listener.getAllSpecies());

		Parent root = loader.getRoot();
		if (stage.getScene() == null) {
			stage.setScene(new Scene(root));
		} else {
			stage.getScene().setRoot(root);
		}
		view.populateAvailableBugemons();
		this.stage.show();
	}

	private List<CreateTeamBugemonDTO> getAvailableBugemons() {
		List<CreateTeamBugemonDTO> availableBugemons = new ArrayList<>();
		for (BugemonSpecies bugemonSpecies : this.getBugemonService().getAllSpecies()) {
			availableBugemons.add(new CreateTeamBugemonDTO(
					bugemonSpecies.getId(),
					bugemonSpecies.getName(),
					bugemonSpecies.getSprite()));
		}
		return availableBugemons;
	}

	public void setTeam(List<String> selectedBugemonIds, BugemonService bugemonService) {
		List<BugemonDTO> teamABugemons = new ArrayList<BugemonDTO>();
		List<BugemonSpeciesDTO> allSpecies = this.getAllSpecies();
		for (String bugemonId : selectedBugemonsId) {
			for (BugemonSpeciesDTO species : allSpecies){
				if (bugemonId.equals(species.getId())){
					teamABugemons.add(new BugemonDTO(species));
				}
			}
		}
		player.setTeam(teamABugemons);
	}

	@Override
	public void onConfirmTeam(List<String> selectedBugemonIds) {
		setTeam(selectedBugemonIds, this.getBugemonService());
		listener.onTeamConfirmed();
	}

	@Override
	public void onReturn() {
		listener.onReturnToMode();
	}

	@Override
	public List<BugemonSpeciesDTO> getAllSpecies(){
		return listener.getAllSpecies();
	}

	public interface Listener {
		void onTeamConfirmed();
		void onReturnToMode();
		List<BugemonSpeciesDTO> getAllSpecies();
	}
}
