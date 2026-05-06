package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.util.List;

import ulb.communication.GameMode;
import ulb.utils.Scaling;

import ulb.DTO.bugemon.BugemonDTO;

/**
 * Window responsible for selecting a game mode (Classic, Automatic, Tower).
 * Displays the player's team and provides options to start different game modes
 */
public class ConfirmTeamWindow extends Window {

    @FXML
    private GridPane playerTeamGrid;
    @FXML
    private VBox content;
    @FXML
    private Label gameModeLabel;

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

    public void setGameModeLabel(GameMode gameMode) {
        String message = "";
        switch (gameMode) {
            case AUTO:
                message = "Mode de jeu : automatique";
                break;
            case CONTROLLED:
                message = "Mode de jeu : classique";
                break;
            case TOWER:
                message = "Mode de jeu : Tour NO";
                break;
            default:
                message = "Mode de jeu inconnu";
        }
        gameModeLabel.setText(message);
    }

    /**
     * Goes back to the Create Team Window
     */
	@FXML
	private void handleReturn(){
        viewListener.onReturn();
	}

    @FXML
    private void handleConfirm(){ viewListener.onConfirm(); }

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
        void onConfirm();
        void onReturn();
    }
}
