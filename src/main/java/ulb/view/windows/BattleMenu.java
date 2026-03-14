package ulb.view.windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import ulb.controller.BattleController;
import ulb.controller.GameController;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;

import java.util.List;

public class BattleMenu {

	private GameController gameController;

    @FXML
    private GridPane playerTeamGrid;

	public void setGameController(GameController gameController) { this.gameController = gameController;}

	public void setupBattle(){
		gameController.setupNormalMode();
	}

    public void handleAutomaticBattle(ActionEvent actionEvent) {
		setupBattle();
		gameController.switchToBattleWindow(gameController.getTeam(), true, actionEvent);
    }

    public void handleControlledBattle(ActionEvent actionEvent) {
		setupBattle();
		gameController.switchToBattleWindow(gameController.getTeam(), false, actionEvent);
    }

    public void displayTeam() {
        displayTeamWithStats(gameController.getTeam(), playerTeamGrid);

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
        for (Bugemon bugemon : team.getMembers()) {
            VBox cell = new VBox();

            Image image = new Image(bugemon.getSprite());
            ImageView sprite = new ImageView(image);
            sprite.setFitWidth(50);
            sprite.setFitHeight(50);
            sprite.setPreserveRatio(true);

            Label name = new Label(bugemon.getName() + " (Level: " + bugemon.getLevel() + ")");
            name.setStyle("-fx-font-weight: bold;");

            Label stats = new Label("HP: " + bugemon.getFightStats().getHp() +
                    " ATK: " + bugemon.getFightStats().getAttack() + " DEF: " +
                    bugemon.getFightStats().getDefense() + " INIT: " + bugemon.getFightStats().getInitiative());

            cell.getChildren().addAll(name, sprite, stats);

            grid.add(cell, 0, row++);
        }
    }
}
