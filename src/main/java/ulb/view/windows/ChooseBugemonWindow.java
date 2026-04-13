package ulb.view.windows;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ulb.DTO.bugemon.BugemonDTO;

public class ChooseBugemonWindow extends Window {

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

            Image image = new Image(bugemon.getSpritePath());
            ImageView sprite = new ImageView(image);
            sprite.setFitWidth(60);
            sprite.setFitHeight(60);
            sprite.setPreserveRatio(true);

            Label name = new Label(bugemon.getName() + " (Niveau: " + bugemon.getLevel() + ")");
            name.setStyle("-fx-font-weight: bold; -fx-font-size: 30px");

            Label stats = new Label(
                    "PV: " + bugemon.getFightStats().getHp()
                            + " ATK: " + bugemon.getFightStats().getAttack()
                            + " DEF: " + bugemon.getFightStats().getDefense()
                            + " INIT: " + bugemon.getFightStats().getInitiative());
            stats.setStyle("-fx-font-size: 30px");

            CheckBox selectBox = new CheckBox();
            selectBox.setOnAction(event -> {
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


	@FXML
	private void apply() {
        if (viewListener != null && selectedBugemon != null) {
            viewListener.onBugemonChosen(selectedBugemon);
        }
	}

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
