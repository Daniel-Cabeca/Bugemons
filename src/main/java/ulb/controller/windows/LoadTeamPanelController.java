package ulb.controller.windows;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ulb.DTO.team.TeamDTO;
import ulb.controller.ClientController;
import ulb.exceptions.ServerStatusException;
import ulb.exceptions.UnknownServerResponse;
import ulb.message.request.teamSave.GetSavedTeamsRequest;
import ulb.message.response.teamSave.SavedTeamsResponse;
import ulb.view.WindowPath;
import ulb.view.windows.LoadTeamPanel;

import java.util.List;

/**
 * Controller for loading the player's saved Bugemon teams.
 */
public class LoadTeamPanelController extends WindowController<LoadTeamPanel> implements LoadTeamPanel.ViewListener {
	/**
	 * The FXML stage for the saved teams selection.
	 */
	private Stage popupStage;

	public LoadTeamPanelController(Stage stage, ClientController clientController) {
		super(stage, WindowPath.LOAD_TEAM_PANEL, clientController);
		this.view.setViewListener(this);
	}

	/**
	 * Displays the Load Team panel screen.
	 */
	public void show() {
		setSavedTeams();

		if (popupStage == null) {
			popupStage = new Stage();
			popupStage.initStyle(StageStyle.UNDECORATED);
			popupStage.initOwner(stage);

			Scene scene = new Scene(this.loader.getRoot());
			scene.getStylesheets().add(getClass().getResource("/styles/global.css").toExternalForm());
			popupStage.setScene(scene);
		}
		popupStage.show();
		popupStage.centerOnScreen();
	}

	/**
	 * Retrieves the list of the current player's saved Bugemon teams form the server and sends it to the associated view.
	 */
	private void setSavedTeams() {
		List<TeamDTO> teams;
		try {
			teams = getSavedTeams();
		} catch (Exception e) {
			LOGGER.warning("Impossible de récupérer les équipes chargées.");
			return;
		}
		view.populateSavedTeams(teams);
	}

	/**
	 * Retrieves the list of the current player's saved Bugemon teams from the server.
	 *
	 * @return The list of the player's saved teams.
	 * @throws ServerStatusException when the response from the server is a StatusResponse with the failure boolean.
	 * @throws UnknownServerResponse when the response from the server is unknown for the request sent.
	 */
	private List<TeamDTO> getSavedTeams() throws ServerStatusException, UnknownServerResponse {
		if (this.clientController.getData(new GetSavedTeamsRequest()) instanceof SavedTeamsResponse teamsMessage) {
			return teamsMessage.getTeams();
		}
		throw new UnknownServerResponse("getSavedTeams");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onLoadTeam(TeamDTO selectedTeam) {
		this.clientController.setupTeamAndShowConfirmTeam(selectedTeam.members());
		popupStage.close();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onReturn() {
		popupStage.close();
	}
}
