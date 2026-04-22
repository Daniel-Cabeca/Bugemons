package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ulb.model.team.Team;
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
     *
     * @throws Exception If the FXML cannot be loaded
     */
    public void show() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.LOAD_TEAM_PANEL));
        loader.load();
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
    public void onLoadTeam(Team selectedTeam) {
        listener.onTeamLoaded(selectedTeam);
    }

    @Override
    public void onReturn() {
        popupStage.close();
    }

    public interface Listener {
        List<Team> getSavedTeams();
        void onTeamLoaded(Team selectedTeam);
    }
}
