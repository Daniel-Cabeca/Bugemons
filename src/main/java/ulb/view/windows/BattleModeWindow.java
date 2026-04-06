package ulb.view.windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;
import ulb.utils.Scaling;


public class BattleModeWindow extends Window {

    @FXML
    private GridPane playerTeamGrid;
    @FXML
    private VBox content;

    private ViewListener viewListener;

    @FXML
    public void initialize() {
        Scaling.applyScaling(content);
    }

    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }


    public void handleAutomaticBattle() {
        viewListener.onAutoBattle();
    }

    public void handleControlledBattle() {
        viewListener.onControlledBattle();
    }

	public void handleTowerBattle(ActionEvent event) {
        viewListener.onTowerMode();
	}

	@FXML
	private void handleReturn(){
        viewListener.onReturn();
	}

    public void displayTeam(Team team) {
        displayTeamWithStats(team, playerTeamGrid);
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

    public interface ViewListener {
        void onAutoBattle();
        void onControlledBattle();
        void onTowerMode();
        void onReturn();
    }
}
