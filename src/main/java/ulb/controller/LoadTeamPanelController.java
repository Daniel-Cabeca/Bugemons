package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ulb.DTO.team.TeamDTO;
import ulb.view.FxmlLoader;
import ulb.exceptions.ViewLoadException;
import ulb.view.WindowPath;
import ulb.view.windows.LoadTeamPanel;

import java.util.List;

public class LoadTeamPanelController implements LoadTeamPanel.ViewListener {

    private final Stage stage;
    private Stage popupStage;
    private LoadTeamPanel view;
    private final Listener listener;

    public LoadTeamPanelController(Stage stage, Listener listener) {
        this.stage = stage;
        this.listener = listener;
    }

    /**
     * Displays the Load Team panel screen.
     */
    public void show() throws ViewLoadException {
        FXMLLoader loader = FxmlLoader.load(this, WindowPath.LOAD_TEAM_PANEL);
        view = loader.getController();
        view.setViewListener(this);

        view.populateSavedTeams(listener.getSavedTeams());

        popupStage = new Stage();
        popupStage.initStyle(StageStyle.UNDECORATED);
        popupStage.initOwner(stage);

        Scene scene = new Scene(loader.getRoot());
        scene.getStylesheets().add(getClass().getResource("/styles/global.css").toExternalForm());
        popupStage.setScene(scene);
        popupStage.show();
        popupStage.centerOnScreen();
    }

    @Override
    public void onLoadTeam(TeamDTO selectedTeam) {
        listener.onTeamLoaded(selectedTeam);
        popupStage.close();
    }

    @Override
    public void onReturn() {
        popupStage.close();
    }

    public interface Listener {
        List<TeamDTO> getSavedTeams();
        void onTeamLoaded(TeamDTO selectedTeam);
    }
}
