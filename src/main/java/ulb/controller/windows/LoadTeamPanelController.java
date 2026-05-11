package ulb.controller.windows;

import java.io.Serializable;
import java.util.List;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ulb.DTO.team.TeamDTO;
import ulb.controller.ClientController;
import ulb.message.request.teamSave.*;
import ulb.message.response.teamSave.*;
import ulb.view.WindowPath;
import ulb.view.windows.LoadTeamPanel;

public class LoadTeamPanelController extends WindowController<LoadTeamPanel> implements LoadTeamPanel.ViewListener {
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

		if (popupStage == null){
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

	private List<TeamDTO> getSavedTeams() {
		Serializable message = this.clientController.getData(new GetSavedTeamsRequest());

		if (message instanceof SavedTeamsResponse teamsMessage){
			return teamsMessage.getTeams();
		}
		return null;
	}

	private void setSavedTeams(){
		List<TeamDTO> teams = getSavedTeams();
		view.populateSavedTeams(teams);
	}

	@Override
    public void onLoadTeam(TeamDTO selectedTeam) {
		this.clientController.setupTeamAndShowConfirmTeam(selectedTeam.members());
        popupStage.close();
    }

    @Override
    public void onReturn() {
        popupStage.close();
    }
}
