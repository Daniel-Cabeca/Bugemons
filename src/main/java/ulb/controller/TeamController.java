package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.bugemon.BugemonSpeciesDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.view.WindowPath;
import ulb.view.windows.CreateTeamWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for creating and validating the player's starting team.
 */
public class TeamController implements CreateTeamWindow.ViewListener {
	private PlayerDTO player;

	private final Listener listener;
	private final Stage stage;

	private CreateTeamWindow view;

	public TeamController(Stage stage, Listener listener, PlayerDTO player) {
		this.stage = stage;
		this.listener = listener;
		this.player = player;
	}

	/**
	 * Displays the create team screen.
	 *
	 * @throws Exception If the FXML cannot be loaded
	 */
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

	/**
	 * Creates a new Team for the player and adds his selected bugémons.
	 */
	public void setTeam(List<String> selectedBugemonIds) {
		List<BugemonDTO> teamABugemons = new ArrayList<BugemonDTO>();
		List<BugemonSpeciesDTO> allSpecies = this.getAllSpecies();
		for (String bugemonId : selectedBugemonIds) {
			for (BugemonSpeciesDTO species : allSpecies){
				if (bugemonId.equals(species.getId())){
					teamABugemons.add(new BugemonDTO(species));
				}
			}
		}
		player.setTeam(teamABugemons);
	}

	/**
	 * Handles team confirmation from the view.
	 *
	 * @param selectedBugemonIds The selected species ids
	 */
	@Override
	public void onConfirmTeam(List<String> selectedBugemonIds) {
		setTeam(selectedBugemonIds);
		listener.onTeamConfirmed();
	}

	/**
	 * Handles return action from the team creation screen.
	 */
	@Override
	public void onReturn() {
		listener.onReturnToMode();
	}

	/**
	 * Handles team loading by opening the load team panel
	 */
	@Override
	public void onLoadTeam() {
		listener.onLoadTeam();
	}

	@Override
	public void onSaveTeam(List<String> selectedBugemonIds, String teamName) {
		listener.onTeamSaved(selectedBugemonIds, teamName);
	}

	@Override
	public List<BugemonSpeciesDTO> getAllSpecies(){
		return listener.getAllSpecies();
	}

	/**
	 * Listener for team creation flow events.
	 */
	public interface Listener {
		/** Called when team selection is confirmed. */
		void onTeamConfirmed();
		void onTeamSaved(List<String> selectedBugemonIds, String teamName);
		void onReturnToMode();
		void onLoadTeam();
		List<BugemonSpeciesDTO> getAllSpecies();
	}
}
