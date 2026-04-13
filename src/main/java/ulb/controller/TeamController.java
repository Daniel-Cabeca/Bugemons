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
import ulb.view.WindowPath;
import ulb.view.windows.CreateTeamWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for creating and validating the player's starting team.
 */
public class TeamController implements CreateTeamWindow.ViewListener {
	private final Player player;
	private final Listener listener;
	private final Stage stage;
	private final BugemonService bugemonService;

	private CreateTeamWindow view;

	/**
	 * Creates the team controller.
	 *
	 * @param stage The application stage
	 * @param listener The listener notified when team flow events occur
	 * @param player The player owning the team
	 * @param bugemonService The service used to retrieve and spawn bugemons
	 */
	public TeamController(Stage stage, Listener listener, Player player, BugemonService bugemonService) {
		this.stage = stage;
		this.listener = listener;
		this.player = player;
		this.bugemonService = bugemonService;
	}

	/**
	 * Returns the bugemon service used by this controller.
	 *
	 * @return The bugemon service
	 */
	public BugemonService getBugemonService() { return this.bugemonService; }

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
		view.displayAvailableBugemons(getAvailableBugemons());

		Parent root = loader.getRoot();
		if (stage.getScene() == null) {
			stage.setScene(new Scene(root));
		} else {
			stage.getScene().setRoot(root);
		}
		this.stage.show();
	}

	/**
	 * Builds the list of available bugemon species for team creation.
	 *
	 * @return The list of available bugemon DTOs
	 */
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

	/**
	 * Creates and assigns a team from selected species identifiers.
	 *
	 * @param selectedBugemonIds The selected species ids
	 * @param bugemonService The service used to spawn bugemons
	 */
	public void setTeam(List<String> selectedBugemonIds, BugemonService bugemonService) {
		List<Bugemon> teamABugemons = new ArrayList<>();
		for (String bugemonId : selectedBugemonIds) {
			Bugemon bugemon = bugemonService.spawnBugemon(bugemonId);
			teamABugemons.add(bugemon);
		}
		Team playerTeam = new Team(teamABugemons);
		player.setTeam(playerTeam);
	}

	/**
	 * Handles team confirmation from the view.
	 *
	 * @param selectedBugemonIds The selected species ids
	 */
	@Override
	public void onConfirmTeam(List<String> selectedBugemonIds) {
		setTeam(selectedBugemonIds, this.getBugemonService());
		listener.onTeamConfirmed();
	}

	/**
	 * Handles return action from the team creation screen.
	 */
	@Override
	public void onReturn() {
		listener.onReturn();
	}

	/**
	 * Listener for team creation flow events.
	 */
	public interface Listener {
		/** Called when team selection is confirmed. */
		void onTeamConfirmed();
		/** Called when user returns to previous screen. */
		void onReturn();
	}
}
