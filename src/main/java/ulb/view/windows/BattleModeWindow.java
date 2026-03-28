package ulb.view.windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import ulb.communication.Message;
import ulb.communication.types.SwitchWindowMessage;
import ulb.controller.GameController;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;
import ulb.utils.Scaling;
import ulb.view.ViewManager;
import ulb.view.WindowPath;

import javax.swing.text.View;


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


    public void handleAutomaticBattle(ActionEvent actionEvent) {
        GameController controller = viewManager.getGameController();
		controller.setupNormalMode();
		controller.switchToBattleWindow(controller.getTeam(), true, actionEvent);
    }

    public void handleControlledBattle(ActionEvent actionEvent) {
        GameController controller = viewManager.getGameController();
        controller.setupNormalMode();
        controller.switchToBattleWindow(controller.getTeam(), false, actionEvent);
    }

	public void handleTowerBattle(ActionEvent actionEvent) {
        GameController controller = viewManager.getGameController();
        controller.setupTowerMode();
		controller.handleTower(controller.getTeam(),actionEvent);
	}

	@FXML
	private void handleReturn(){
        Message switchCreatTeamMenu = new SwitchWindowMessage(WindowPath.CREATE_TEAM);
        viewManager.handleInput(switchCreatTeamMenu);
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
