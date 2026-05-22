package ulb.controller.windows;

import javafx.event.EventHandler;
import javafx.stage.Stage;
import ulb.DTO.battle.MultiBattleStatusDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.bugemon.BugemonSpeciesDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.DTO.team.TeamDTO;
import ulb.controller.ClientController;
import ulb.exceptions.UnknownServerResponse;
import ulb.message.request.gameActions.QuitMultiBattleRequest;
import ulb.message.request.gameActions.StartMultiBattleRequest;
import ulb.message.request.gameData.GetAllBugemonSpeciesRequest;
import ulb.message.request.setup.ConfirmTeamMultiRequest;
import ulb.message.request.teamSave.SaveTeamRequest;
import ulb.message.response.gameData.BugemonSpeciesResponse;
import ulb.view.WindowPath;
import ulb.view.windows.CreateTeamWindow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller for creating and validating the player's starting team.
 */
public class TeamController extends WindowController<CreateTeamWindow> implements CreateTeamWindow.ViewListener {
	/**
	 * Information on the opponent for a multiplayer battle.
	 * Set to null if playing in singleplayer.
	 */
	private Optional<PlayerDTO> opponent = Optional.empty();

	public TeamController(Stage stage, ClientController clientController) {
		super(stage, WindowPath.CREATE_TEAM, clientController);
		this.view.setViewListener(this);
	}

	/**
	 * Removes the opponent information, reverting to singleplayer.
	 */
	public void resetOpponent() { this.opponent = Optional.empty(); }

	/**
	 * Display this controller's associated view.
	 */
	@Override
	public void show() {
		this.view.populateAvailableBugemons();
		super.show();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onConfirmTeam(List<String> selectedBugemonIds) {
		List<BugemonDTO> playerTeam;
		try {
			playerTeam = setupTeamMembers(selectedBugemonIds);
		} catch (Exception e) {
			LOGGER.warning("Impossible de charger les bugemons");
			return;
		}

		if (this.hasOpponent()) {
			this.confirmTeamMulti(playerTeam, this.getOpponent().get());
		} else {
			this.clientController.setupTeamAndShowConfirmTeam(playerTeam);
		}
	}

	/**
	 * Creates a new Team for the player and adds his selected bugémons.
	 */
	public void setTeam(List<BugemonDTO> team) {
		Optional<PlayerDTO> selfPlayer = getSelfPlayer();
		if (selfPlayer.isEmpty()) {
			LOGGER.info("Impossible de confirmer l'équipe : le joueur n'est pas connecté");
			return;
		}

		selfPlayer.get().setTeam(team);
	}

	public boolean hasOpponent() { return this.opponent.isPresent(); }

	/**
	 * Confirms the team for a multiplayer battle and waits for the battle to start.
	 *
	 * @param opponent The opponent
	 */
	public void confirmTeamMulti(List<BugemonDTO> playerTeam, PlayerDTO opponent) {
		if (this.clientController.postData(new ConfirmTeamMultiRequest(opponent, playerTeam))){
			this.setTeam(playerTeam);
			this.openWaitWindow(e -> {
				this.waitForOpponentTeam(opponent);
			});
		}
	}

	public Optional<PlayerDTO> getOpponent() { return this.opponent; }

	public Optional<PlayerDTO> getSelfPlayer() { return this.clientController.getPlayer(); }

	public Optional<String> getOpponentUsername() {
		if (opponent.isPresent()){
			return Optional.of(this.opponent.get().getUsername());
		}
		return Optional.empty();
	}

	/**
	 * Transforms the list of bugemon ids to a list of BugemonDTO objects
	 *
	 * @param selectedBugemonIds the list of bugemon ids
	 * @return the list of BugemonDTO
	 * @throws Exception when an error occures for the communication with the server
	 */
	private List<BugemonDTO> setupTeamMembers(List<String> selectedBugemonIds) throws Exception {
		List<BugemonDTO> members = new ArrayList<>();
		List<BugemonSpeciesDTO> allSpecies = this.getAllSpecies();
		for (String bugemonId : selectedBugemonIds) {
			for (BugemonSpeciesDTO species : allSpecies) {
				if (bugemonId.equals(species.id())) {
					members.add(new BugemonDTO(species));
				}
			}
		}
		return members;
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
		Optional<PlayerDTO> self = getSelfPlayer();
		if (self.isEmpty()) {
			LOGGER.info("Vous n'êtes pas connecté");
			return;
		}

		MultiBattleStatusDTO status;
		try {
			status = this.clientController.getMultiBattleStatus(self.get().getUserId(), opponent.getUserId());
		} catch (Exception e) {
			this.clientController.stopWaitWindow();
			this.clientController.showWindow(WindowName.MAIN_MENU);
			return;
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
	 * Starts a battle once both teams have been picked.
	 *
	 * @param opponent The opponent
	 */
	private void startMultiBattle(PlayerDTO opponent) {
		this.clientController.postData(new StartMultiBattleRequest(opponent));
		this.clientController.showWindow(WindowName.BATTLE);
	}

	public void setOpponent(PlayerDTO opponent) { this.opponent = Optional.of(opponent); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onLoadTeam() {
		this.clientController.showWindow(WindowName.LOAD_TEAM_PANEL);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSaveTeam(List<String> selectedBugemonIds, String teamName) {
		List<BugemonDTO> members;
		try {
			members = setupTeamMembers(selectedBugemonIds);
		} catch (Exception e) {
			LOGGER.warning("Impossible de charger les bugemons");
			return;
		}
		TeamDTO team = new TeamDTO(-1, teamName, members);
		this.saveTeam(team);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onReturn() {
		
		try {
			if (this.hasOpponent()) {
				this.clientController.getData(new QuitMultiBattleRequest(this.getOpponent().get()));
			}
		} catch (Exception e) {
			LOGGER.warning("Error while trying to quit multi battle");
			return;
		}

		this.clientController.showWindow(WindowName.GAME_MODE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<BugemonSpeciesDTO> getAllSpecies() throws Exception {
		Serializable message = this.clientController.getData(new GetAllBugemonSpeciesRequest());
		if (message instanceof BugemonSpeciesResponse speciesMessage) {
			return speciesMessage.getSpecies();
		}
		throw new UnknownServerResponse("getAllBugemonSpecies");		
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
}
