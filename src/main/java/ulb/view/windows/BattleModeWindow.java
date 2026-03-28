package ulb.view.windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import ulb.communication.Message;
import ulb.communication.types.GameMode;
import ulb.communication.types.SetupGameModeMessage;
import ulb.communication.types.SwitchWindowMessage;
import ulb.controller.GameController;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;
import ulb.utils.Scaling;
import ulb.view.WindowPath;


public class BattleModeWindow extends Window {

    @FXML
    private GridPane playerTeamGrid;

    @FXML
    private VBox content;

    @FXML
    public void initialize() {
        Scaling.applyScaling(content);
    }

    /**
     * Called after the window is created and the controller is set
     */
    @Override
    public void onLoad() {
        displayTeam();
    }


    public void handleAutomaticBattle() {
        SetupGameModeMessage setupMessage = new SetupGameModeMessage(GameMode.AUTO);
        viewManager.handleMessage(setupMessage);
        sendSwitchWindowMessage(WindowPath.BATTLE);
    }

    public void handleControlledBattle() {
        SetupGameModeMessage setupMessage = new SetupGameModeMessage(GameMode.CONTROLLED);
        viewManager.handleMessage(setupMessage);
        sendSwitchWindowMessage(WindowPath.BATTLE);
    }

	public void handleTowerBattle() {
        SetupGameModeMessage setupMessage = new SetupGameModeMessage(GameMode.TOWER);
        viewManager.handleMessage(setupMessage);
        sendSwitchWindowMessage(WindowPath.BATTLE);
	}

	@FXML
	private void handleReturn(){
        sendSwitchWindowMessage(WindowPath.CREATE_TEAM);
	}

    public void displayTeam() {
        displayTeamWithStats(viewManager.getGameController().getTeam(), playerTeamGrid);
    }

    /**
     * Displays the player's team with corresponding stats
     *
     * @param team the team to display
     * @param grid the grid in which the bugemons are displayed
     */
    public void displayTeamWithStats(Team team, GridPane grid) {
        grid.getChildren().clear();
        int row = 0;
        int col = 0;
        int count =0;

        for (Bugemon bugemon : team.getMembers()) {
            VBox cell = new VBox();

            Image image = new Image(bugemon.getSprite());
            ImageView sprite = new ImageView(image);
            sprite.setFitWidth(50);
            sprite.setFitHeight(50);
            sprite.setPreserveRatio(true);

            Label name = new Label(bugemon.getName() + " (Niveau: " + bugemon.getLevel() + ")");
            name.setStyle("-fx-font-weight: bold;");

            Label stats = new Label("PV: " + bugemon.getFightStats().getHp() +
                    " ATK: " + bugemon.getFightStats().getAttack() + " DEF: " +
                    bugemon.getFightStats().getDefense() + " INIT: " + bugemon.getFightStats().getInitiative());

            cell.getChildren().addAll(name, sprite, stats);
            grid.add(cell, col, row++);

            count++;
            if (count == 3){ // put 3 bugemons in each col
            	col++;
             	row=0;
            }

        }

    }
}
