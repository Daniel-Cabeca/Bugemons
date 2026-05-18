package ulb.controller.windows;

import javafx.event.EventHandler;
import javafx.stage.Stage;
import ulb.DTO.battle.MultiBattleStatusDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.bugemon.BugemonSpeciesDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.DTO.team.TeamDTO;
import ulb.controller.ClientController;
import ulb.message.request.gameActions.QuitMultiBattleRequest;
import ulb.message.request.gameActions.StartMultiBattleRequest;
import ulb.message.request.gameData.GetAllBugemonSpeciesRequest;
import ulb.message.request.setup.ConfirmTeamMultiRequest;
import ulb.message.request.social.GetMultiBattleStatusRequest;
import ulb.message.request.teamSave.SaveTeamRequest;
import ulb.message.response.gameData.BugemonSpeciesResponse;
import ulb.message.response.social.MultiBattleStatusResponse;
import ulb.view.WindowPath;
import ulb.view.windows.CreateTeamWindow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.naming.CommunicationException;

/**
 * Controller for creating and validating the player's starting team.
 */
public class TeamController extends WindowController<CreateTeamWindow> implements CreateTeamWindow.ViewListener {

	private PlayerDTO opponent = null;

	public TeamController(Stage stage, ClientController clientController) {
		super(stage, WindowPath.CREATE_TEAM, clientController);
		this.view.setViewListener(this);
	}


	public PlayerDTO getSelfPlayer() { return this.clientController.getPlayer(); }
	public boolean hasOpponent() { return this.opponent != null; }
	public PlayerDTO getOpponent() { return this.opponent; }
	public void setOpponent(PlayerDTO opponent) { this.opponent = opponent; }
	public void resetOpponent() { this.opponent = null; }


	@Override
	public void show(){
		this.view.populateAvailableBugemons();
		super.show();
	}

	/**
	 * Creates a new Team for the player and adds his selected bugémons.
	 */
	public void setTeam(List<String> selectedBugemonIds) {
		PlayerDTO selfPlayer = this.getSelfPlayer();

		if (selfPlayer == null) {
			throw new IllegalStateException("Impossible de confirmer l'équipe : aucun joueur connecté côté client.");
		}

		List<BugemonDTO> members = setupTeamMembers(selectedBugemonIds);
		selfPlayer.setTeam(members);
	}

	/**
	 * Handles team confirmation from the view.
	 *
	 * @param selectedBugemonIds The selected species ids
	 */
	@Override
	public void onConfirmTeam(List<String> selectedBugemonIds) {
		this.setTeam(selectedBugemonIds);

		if (this.hasOpponent()) {
			this.confirmTeamMulti(this.getOpponent());
		}
		else {
			List<BugemonDTO> team = this.clientController.getPlayer().getTeam();
			this.clientController.setupTeamAndShowConfirmTeam(team);
		}
	}

	/**
	 * Confirms the team for a multiplayer battle and waits for the battle to start.
	 *
	 * @param opponent The opponent
	 */
	public void confirmTeamMulti(PlayerDTO opponent) {
		PlayerDTO player = this.clientController.getPlayer();
		this.clientController.postData(new ConfirmTeamMultiRequest(this.opponent, player.getTeam()));

		this.openWaitWindow(e -> {
			this.waitForOpponentTeam(opponent);
		});
	}

	/**
	 * Opens a waiting window.
	 *
	 * @param waitCycle The event handler to play in a loop
	 */
	public void openWaitWindow(EventHandler waitCycle) {
		this.clientController.setNewTimeLine(waitCycle);
		this.clientController.showWindow(WindowName.WAIT);
	}
	/**
	 * Called in a loop while waiting for the opponent to pick his team.
	 *
	 * @param opponent The opponent
	 */
	private void waitForOpponentTeam(PlayerDTO opponent) {
		PlayerDTO self = this.clientController.getPlayer();

		MultiBattleStatusDTO status = null;
		try{
			status = this.getMultiBattleStatus(self.getUserId(), opponent.getUserId());
		} catch(CommunicationException e){
			this.clientController.stopWaitWindow();
			this.clientController.showWindow(WindowName.MAIN_MENU);
		}

		switch (status.status()) {
			case BATTLE:
				this.clientController.stopWaitWindow();
				this.startMultiBattle(opponent);
				break;

			case PICKING_TEAMS:
				break;

			default:
				this.clientController.stopWaitWindow();
				this.clientController.showWindow(WindowName.MAIN_MENU);
		}
	}
	/**
	 * Returns the current multiplayer battle status between two players.
	 *
	 * @param userId1 The first player's id
	 * @param userId2 The second player's id
	 * @return The multiplayer battle status DTO
	 */
	public MultiBattleStatusDTO getMultiBattleStatus(int userId1, int userId2) throws CommunicationException {
		if (this.clientController.getData(new GetMultiBattleStatusRequest(userId1, userId2)) instanceof MultiBattleStatusResponse msg)
			return msg.getStatus();
		LOGGER.warning("Failed to obtain multiplayer battle status.");
		throw new CommunicationException();

	}
	/**
	 * Starts a battle once both teams have been picked.
	 *
	 * @param opponent The opponent
	 */
	private void startMultiBattle(PlayerDTO opponent) {
		this.clientController.postData(new StartMultiBattleRequest(opponent));
		this.clientController.showWindow(WindowName.BATTLE);
	}

	/**
	 * Handles return action from the team creation screen.
	 */
	public void onReturn() {
		if (this.hasOpponent()) {
			this.clientController.getData(new QuitMultiBattleRequest(this.opponent));
		}

		this.clientController.showWindow(WindowName.GAME_MODE);
	}

	/**
	 * Handles team loading by opening the load team panel
	 */
	@Override
	public void onLoadTeam() {
		this.clientController.showWindow(WindowName.LOAD_TEAM_PANEL);
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
		this.saveTeam(team);
	}
	/**
	 * Saves the team to the database.
	 *
	 * @param teamDTO The DTO of the team to be saved
	 */
	public void saveTeam(TeamDTO teamDTO) {
		boolean success = this.clientController.postData(new SaveTeamRequest(teamDTO));
		if (!success) {
			this.view.showInvalidSaveAlert("Tu as déjà une équipe avec ce nom!");
		}
	}

    /**
	 * Transforms the list of bugemon ids to a list of BugemonDTO objects
	 *
     * @param selectedBugemonIds the list of bugemon ids
     * @return the list of BugemonDTO
     */
	private List<BugemonDTO> setupTeamMembers(List<String> selectedBugemonIds) {
		List<BugemonDTO> members = new ArrayList<>();
		List<BugemonSpeciesDTO> allSpecies = this.getAllSpecies();
		for (String bugemonId : selectedBugemonIds) {
			for (BugemonSpeciesDTO species : allSpecies){
				if (bugemonId.equals(species.id())){
					members.add(new BugemonDTO(species));
				}
			}
		}
		return members;
	}

	/**
	 * Returns the list of all the Bugemon species.
	 *
	 * @return A list of all the species of Bugemon
	 */
	@Override
	public List<BugemonSpeciesDTO> getAllSpecies(){
		Serializable message = this.clientController.getData(new GetAllBugemonSpeciesRequest());

		if (message instanceof BugemonSpeciesResponse speciesMessage){
			return speciesMessage.getSpecies();
		}
		return null;
	}
}
