package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;



public class ChooseBugemonWindow extends Window{

	private ViewListener viewListener;
    private CheckBox selectedCheckBox;
    private Bugemon selectedBugemon;

	@FXML
	private VBox bugemonList;


	public void setViewListener(ViewListener viewListener) {
		this.viewListener = viewListener;
	}

	/**
     * Displays the player's team with corresponding stats
     *
     * @param team the team to display
     * @param grid the grid in which the bugemons are displayed
     */
    public void populatePlayerBugemons(Team team) {
        bugemonList.getChildren().clear();
		selectedCheckBox = null;
		selectedBugemon = null;

        for (Bugemon bugemon : team.getMembers()) {
            HBox cell = new HBox(10);

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

            cell.getChildren().addAll(name, sprite, stats, selectBox);
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
        void onBugemonChosen(Bugemon bugemon);
		void onReturnFloorRewardWindow();
	}

}
