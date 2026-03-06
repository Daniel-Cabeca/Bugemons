package ulb.view;

import ulb.controller.BattleController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ulb.model.Player;
import ulb.model.bugemon.BugemonSpecies;
import ulb.model.bugemon.BugemonDatabase;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CreateTeamMenu {

	@FXML
	private GridPane availableBugemonsGrid;

	@FXML
	private ScrollPane availableBugemonsScroll;

	@FXML
	private GridPane selectedBugemons;

	private Player player;
	private final List<String> selected = new ArrayList<>();
	private BattleController battleController;

	public void setPlayer(Player player) {
		this.player = player;
		this.battleController = new BattleController(player);
	}

	/**
	* Initializes the create team menu
	*/

	@FXML
	public void initialize() {
		populateAvailableBugemons();
	}

	/**
	* Updates the available bugemons grid by adding a box for each bugemon in the list
	*/
	private void populateAvailableBugemons() {
		int col = 0, row = 0;

		for (BugemonSpecies bugemon : BugemonDatabase.getInstance()) {
			VBox cell = new VBox(8);
			cell.getStyleClass().add("availableBugemons"); // add a css class

			Label name = new Label(bugemon.getName());

			Image image = new Image(bugemon.getSprite());
			ImageView sprite = new ImageView(image);
			sprite.setFitWidth(75);
			sprite.setFitHeight(75);
			sprite.setPreserveRatio(true);

			CheckBox checkBox = new CheckBox();
			checkBox.setSelected(selected.contains(bugemon.getName()));

			checkBox.setOnAction(e -> {
				if (checkBox.isSelected()) {
					onSelectBugemon(bugemon.getName());
				} else {
					onDeselectBugemon(bugemon.getName());
				}
			});

			cell.getChildren().addAll(name,sprite, checkBox);
			availableBugemonsGrid.add(cell, col, row);

			col++;
			if (col == 3) { col = 0; row++; }
		}
	}

	/**
	* Updates the selected bugemons grid by adding a box for each selected bugemon
	*/
	private void populateSelectedBugemons() {
		// Clears the grid to avoid placing cells on top of each other
		selectedBugemons.getChildren().clear();
		int col = 0, row = 0;

		for (String bugemon : selected) {
			VBox cell = new VBox();
			cell.getStyleClass().add("selectedBugemons"); // add a css class

			Label name = new Label(bugemon);
			cell.getChildren().add(name);
			selectedBugemons.add(cell, col, row);

			// Ensure there are 3 columns
			col++;
			if (col == 3) { col = 0; row++; }
		}
	}

	private void onSelectBugemon(String bugemon) {
		if (!selected.contains(bugemon) && selected.size() < 6) {
			selected.add(bugemon);
			populateSelectedBugemons();
		} else {
			populateAvailableBugemons();
		}
	}

	private void onDeselectBugemon(String bugemon) {
		selected.remove(bugemon);
		populateSelectedBugemons();
	}

	/**
	 * Confirms the selected team and asks the controller to switch to the battle window
	 * then, closes the current window
	 * @param event the action triggered by clicking the confirm button
	 * @throws IllegalStateException if the team is empty or has more than 6 bugemons
	 */
	public void handleConfirmTeam(ActionEvent event) {
		if (!selected.isEmpty() && selected.size() <= 6) {
			battleController.switchToBattleMenu(selected, event);
		} else {
			throw new IllegalStateException("You must select between 1 and 6 bugemons to confirm your team.");
		}
	}

	/**
	* Returns to the main menu
	* @param event the action triggered by clicking the return button
	*/
	public void handleReturn(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/view/MainMenu.fxml"));
		Parent root = loader.load();
		MainMenu controller = loader.getController();
		controller.setPlayer(player);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.getScene().setRoot(root);
	}
}
