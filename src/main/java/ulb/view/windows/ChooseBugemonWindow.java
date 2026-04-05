package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ulb.controller.GameController;
import ulb.controller.towerManager.TowerManager;
import ulb.model.Player;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;
import ulb.view.WindowPath;



public class ChooseBugemonWindow extends Window{
	//TODO: Add Listener !

	private ViewListener viewListener;


	@FXML
	private VBox bugemonList;


	public void setViewListener(ViewListener viewListener) {
		this.viewListener = viewListener;
	}

	// @FXML
	// public void initialize() {
	// 	populatePlayerBugemons(gameController.getTeam());
	// }

	// /**
 //     * Displays the player's team with corresponding stats
 //     *
 //     * @param team the team to display
 //     * @param grid the grid in which the bugemons are displayed
 //     */
 //    public void populatePlayerBugemons(Team team) {
 //        bugemonList.getChildren().clear();

 //        for (Bugemon bugemon : team.getMembers()) {
 //            HBox cell = new HBox();

 //            Image image = new Image(bugemon.getSprite());
 //            ImageView sprite = new ImageView(image);
 //            sprite.setFitWidth(50);
 //            sprite.setFitHeight(50);
 //            sprite.setPreserveRatio(true);

 //            Label name = new Label(bugemon.getName() + " (Niveau: " + bugemon.getLevel() + ")");
 //            name.setStyle("-fx-font-weight: bold;");

 //            Label stats = new Label("PV: " + bugemon.getFightStats().getHp() +
 //                    " ATK: " + bugemon.getFightStats().getAttack() + " DEF: " +
 //                    bugemon.getFightStats().getDefense() + " INIT: " + bugemon.getFightStats().getInitiative());

 //            cell.getChildren().addAll(name, sprite, stats);
 //            bugemonList.getChildren().add(cell);
 //        }
 //    }


	@FXML
	private void apply() {
		// Add the status to Bugemon
		// Or add ability to Bugemon
		switchWindow(WindowPath.NEXT_ROOM); // REMOVE: after adding listener
	}

	@FXML
	private void returnFloorRewardWindow() {
		//viewListener.onReturnFloorRewardWindow();
		switchWindow(WindowPath.FLOOR_REWARD); // REMOVE: after adding listener
	}

	public interface ViewListener {
		void onReturnFloorRewardWindow();
	}

}
