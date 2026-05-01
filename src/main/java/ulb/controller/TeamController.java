package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.bugemon.BugemonSpeciesDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.DTO.team.TeamDTO;
import ulb.view.FxmlLoader;
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

	public CreateTeamWindow getView(){return this.view;}

	/**
	 * Displays the create team screen.
	 */
	public void show() {
		FXMLLoader loader = FxmlLoader.load(this, WindowPath.CREATE_TEAM);
		view = loader.getController();
		view.setViewListener(this);
		view.populateAvailableBugemons();

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
		List<BugemonDTO> members = setupTeamMembers(selectedBugemonIds);
		player.setTeam(members);
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

    /**
	 * Handles saving the team to the database
     * @param selectedBugemonIds the list of the ids of the team members
     * @param teamName the name of the team
     */
	@Override
	public void onSaveTeam(List<String> selectedBugemonIds, String teamName) {
		List<BugemonDTO> members = setupTeamMembers(selectedBugemonIds);
		TeamDTO team = new TeamDTO(-1, teamName, members);
		listener.onTeamSaved(team);
	}

    /**
	 * Transforms the list of bugemon ids to a list of BugemonDTO objects
	 *
     * @param selectedBugemonIds the list of bugemon ids
     * @return the list of BugemonDTO
     */
	private List<BugemonDTO> setupTeamMembers(List<String> selectedBugemonIds) {
		List<BugemonDTO> members = new ArrayList<BugemonDTO>();
		List<BugemonSpeciesDTO> allSpecies = this.getAllSpecies();
		for (String bugemonId : selectedBugemonIds) {
			for (BugemonSpeciesDTO species : allSpecies){
				if (bugemonId.equals(species.getId())){
					members.add(new BugemonDTO(species));
				}
			}
		}
		return members;
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
		void onTeamSaved(TeamDTO teamDTO);
		void onReturnToMode();
		void onLoadTeam();
		List<BugemonSpeciesDTO> getAllSpecies();
	}
}
