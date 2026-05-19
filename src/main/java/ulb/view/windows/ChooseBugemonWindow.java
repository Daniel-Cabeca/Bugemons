package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ulb.DTO.bugemon.BugemonDTO;

import java.util.List;

public class ChooseBugemonWindow {

	private ViewListener viewListener;
	private CheckBox selectedCheckBox;
	private BugemonDTO selectedBugemon;

	@FXML
	private VBox bugemonList;

	public void setViewListener(ViewListener viewListener) {
		this.viewListener = viewListener;
	}

	/**
	 * Displays the player's team with corresponding stats
	 *
	 * @param team the team to display
	 */
	public void populatePlayerBugemons(List<BugemonDTO> team) {
		bugemonList.getChildren().clear();
		selectedCheckBox = null;
		selectedBugemon = null;

		for (BugemonDTO bugemon : team) {
			HBox cell = new HBox(10);

			// Setting Image of the Bugemon
			Image image = new Image(bugemon.getSpritePath());
			ImageView sprite = new ImageView(image);
			sprite.setFitWidth(60);
			sprite.setFitHeight(60);
			sprite.setPreserveRatio(true);

			// Setting infos of the Bugemon
			Label name = new Label(bugemon.getName() + " (Niveau: " + bugemon.level() + ")");
			name.setStyle("-fx-font-weight: bold; -fx-font-size: 30px");

			// Setting stats of the Bugemon
			Label stats =
					new Label("PV: " + bugemon.getHp() + " ATK: " + bugemon.getAttack() + " DEF: " + bugemon.getDefense() + " INIT: " + bugemon.getInitiative());
			stats.setStyle("-fx-font-size: 30px");

			CheckBox selectBox = new CheckBox();
			selectBox.setOnAction(event -> {
				// Set the current Bugemon as the selected one
				if (selectBox.isSelected()) {
					if (selectedCheckBox != null && selectedCheckBox != selectBox) {
						selectedCheckBox.setSelected(false);
					}
					selectedCheckBox = selectBox;
					selectedBugemon = bugemon;
				} else if (selectedCheckBox == selectBox) {
					selectedCheckBox = null;
					selectedBugemon = null;
				}
			});

			cell.getChildren().addAll(sprite, name, stats, selectBox);
			bugemonList.getChildren().add(cell);
		}
	}

	/**
	 * Choose the last Bugemon selected
	 */
	@FXML
	private void apply() {
		if (viewListener != null && selectedBugemon != null) {
			viewListener.onBugemonChosen(selectedBugemon);
		}
	}

	/**
	 * Return to the previous Menu (Floor Reward Window)
	 */
	@FXML
	private void returnFloorRewardWindow() {
		if (viewListener != null) {
			viewListener.onReturnFloorRewardWindow();
		}
	}

	public interface ViewListener {
		void onBugemonChosen(BugemonDTO bugemon);
		void onReturnFloorRewardWindow();
	}

}
