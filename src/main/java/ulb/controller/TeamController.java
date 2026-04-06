package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.bugemon.BugemonSpeciesDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.model.Player;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;
import ulb.view.WindowPath;
import ulb.view.windows.CreateTeamWindow;

import java.util.ArrayList;
import java.util.List;

public class TeamController implements CreateTeamWindow.ViewListener {
	private PlayerDTO player;

	private final Listener listener;
	private CreateTeamWindow view;
	private Stage stage;

	public TeamController(Stage stage, Listener listener, PlayerDTO player) {
		this.stage = stage;
		this.listener = listener;
		this.player = player;
	}

	public void show() throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.CREATE_TEAM));
		loader.load();
		view = loader.getController();
		view.setViewListener(this);

		Parent root = loader.getRoot();
		if (stage.getScene() == null) {
			stage.setScene(new Scene(root));
		} else {
			stage.getScene().setRoot(root);
		}
		view.populateAvailableBugemons();
		this.stage.show();
	}

	public void setTeam(List<String> selectedBugemons){
		List<BugemonDTO> teamABugemons = new ArrayList<BugemonDTO>();
		List<BugemonSpeciesDTO> allSpecies = this.getAllSpecies();
		for (String bugemonName : selectedBugemons) {
			for (BugemonSpeciesDTO species : allSpecies){
				if (bugemonName == species.getName()){
					teamABugemons.add(new BugemonDTO(species));
				}
			}
		}
		player.setTeam(teamABugemons);
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

	@Override
	public List<BugemonSpeciesDTO> getAllSpecies(){
		return listener.getAllSpecies();
	}

	public interface Listener {
		void onTeamConfirmed();
		void onReturn();
		List<BugemonSpeciesDTO> getAllSpecies();
	}
}
