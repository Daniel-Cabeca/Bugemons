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
	private ClientController clientController;
	private CreateTeamWindow view;

	private PlayerDTO opponent = null;

	public TeamController(ClientController clientController) {
		this.clientController = clientController;
	}

	public CreateTeamWindow getView(){return this.view;}
	public Stage getStage() { return this.clientController.getStage(); }
	public PlayerDTO getSelfPlayer() { return this.clientController.getPlayer(); }

	public boolean hasOpponent() { return this.opponent != null; }
	public PlayerDTO getOpponent() { return this.opponent; }
	public void setOpponent(PlayerDTO opponent) { this.opponent = opponent; }

	/**
	 * Displays the create team screen.
	 */
	public void show() {
		FXMLLoader loader = FxmlLoader.load(this, WindowPath.CREATE_TEAM);
		view = loader.getController();
		view.setViewListener(this);
		view.populateAvailableBugemons();

		Parent root = loader.getRoot();
		if (this.getStage().getScene() == null) {
			this.getStage().setScene(new Scene(root));
		} else {
			this.getStage().getScene().setRoot(root);
		}
		view.populateAvailableBugemons();
		this.getStage().show();
	}

	/**
	 * Creates a new Team for the player and adds his selected bugémons.
	 */
	public void setTeam(List<String> selectedBugemonIds) {
		List<BugemonDTO> members = setupTeamMembers(selectedBugemonIds);
		this.getSelfPlayer().setTeam(members);
	}

	/**
	 * Handles team confirmation from the view.
	 *
	 * @param selectedBugemonIds The selected species ids
	 */
	@Override
	public void onConfirmTeam(List<String> selectedBugemonIds) {
		setTeam(selectedBugemonIds);

		if (this.hasOpponent()) {
			this.clientController.confirmTeamMulti(this.getOpponent());
		}
		else {
			this.clientController.setupTeamAndShowModeMenu();
		}
	}

	/**
	 * Handles return action from the team creation screen.
	 */
	public void onReturn() {
		this.clientController.switchToGameModeWindow();
	}

	/**
	 * Handles team loading by opening the load team panel
	 */
	@Override
	public void onLoadTeam() {
		this.clientController.loadTeam();
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
		this.clientController.saveTeam(team);
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
		return this.clientController.getAllSpecies();
	}
}
