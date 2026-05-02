package ulb.view.windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.util.List;
import ulb.utils.Scaling;

import ulb.DTO.bugemon.BugemonDTO;

/**
 * Window responsible for selecting a game mode (Classic, Automatic, Tower).
 * Displays the player's team and provides options to start different game modes
 */
public class BattleModeWindow extends Window {

    @FXML
    private GridPane playerTeamGrid;
    @FXML
    private VBox content;

    private ViewListener viewListener;

    /**
     * Applies scaling when the window is initialized.
     */
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

	public void handleNewTowerBattle(){
		viewListener.onTowerMode(true);
	}
	public void handleContinueTowerBattle(){
		viewListener.onTowerMode(false);
	}

    /**
     * Goes back to the Create Team Window
     */
	@FXML
	private void handleReturn(){
        viewListener.onReturn();
	}

    /**
     * Displays the player's team with corresponding stats
     *
     * @param team the team to display
     */
    public void displayTeam(List<BugemonDTO> team) {
        playerTeamGrid.getChildren().clear();
        int row = 0;
        int col = 0;
        int count =0;

        // for each Bugemon it displays its sprite, name and stats
        for (BugemonDTO bugemon : team) {
            VBox cell = new VBox();

            Image image = new Image(bugemon.getSpritePath());
            ImageView sprite = new ImageView(image);
            sprite.setFitWidth(50);
            sprite.setFitHeight(50);
            sprite.setPreserveRatio(true);

            Label name = new Label(bugemon.getName() + " (Niveau: " + bugemon.getLevel() + ")");
            name.setStyle("-fx-font-weight: bold;");

            Label stats = new Label("PV: " + bugemon.getFightStats().hp() +
                    " ATK: " + bugemon.getFightStats().attack() + " DEF: " +
                    bugemon.getFightStats().defense() + " INIT: " + bugemon.getFightStats().initiative());

            cell.getChildren().addAll(name, sprite, stats);
            playerTeamGrid.add(cell, col, row++);

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
        void onTowerMode(boolean newTower);
        void onReturn();
    }
}
