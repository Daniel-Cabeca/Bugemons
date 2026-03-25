package ulb.view.windows;

import ulb.controller.GameController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ulb.model.bugemon.BugemonSpecies;
import ulb.utils.Scaling;
import ulb.view.handler.Window;
import ulb.service.BugemonService;
import ulb.service.ServiceLoader;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CreateTeamWindow extends Window {

	@FXML
	private VBox content;

	@FXML
	private GridPane availableBugemonsGrid;

	@FXML
	private GridPane selectedBugemons;

	private final List<String> selected = new ArrayList<>();
	private GameController gameController;
	private final int LIMITED_BUGEMONS_NUMBER =6;


	/**
	 * Initializes the create team menu
	 */
	@FXML
	public void initialize() {
		populateAvailableBugemons();
		Scaling.applyScaling(content);
	}


	/**
	 * Updates the available bugemons grid by adding a box for each bugemon in the list
	 */
	private void populateAvailableBugemons() {
		availableBugemonsGrid.getChildren().clear();

		BugemonService bugemonService = ServiceLoader.getBugemonService();
		int col = 0, row = 0;

		for (BugemonSpecies bugemon : bugemonService.getAllSpecies()) {

			VBox cell = new VBox(8);
			cell.getStyleClass().add("availableBugemons");

			Label name = new Label(bugemon.getName());

			Image image = new Image(bugemon.getSprite());
			ImageView sprite = new ImageView(image);
			sprite.setFitWidth(90);
			sprite.setFitHeight(90);
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

			cell.getChildren().addAll(name, sprite, checkBox);
			availableBugemonsGrid.add(cell, col, row);

			col++;
			if (col == 8) {
				col = 0;
				row++;
			}
		}
	}

	/**
	 * Updates the selected bugemons grid by adding a box for each selected bugemon
	 */
	private void populateSelectedBugemons() {
		selectedBugemons.getChildren().clear();

		int col = 0, row = 0;

		for (String bugemon : selected) {
			VBox cell = new VBox();
			cell.getStyleClass().add("selectedBugemons");

			Label name = new Label(bugemon);
			cell.getChildren().add(name);

			selectedBugemons.add(cell, col, row);

			col++;
			if (col == 8) {
				col = 0;
				row++;
			}
		}
		checkDisableBugemons();
	}

	/**
	 * Disable all bugemons after @LIMITED_BUGEMONS_NUMBER are selected so that no more can be selected
	 */
	private void checkDisableBugemons() {
		if (selected.size() == LIMITED_BUGEMONS_NUMBER) {
			disableNoneSelectedBugemons();
		}
		else {
			enableAllBugemons();
		}
	}

	/**
	 * Disable all none selected bugemon
	 */
	private void disableNoneSelectedBugemons(){
		for (Node node: availableBugemonsGrid.getChildren()) {
			VBox vbox = (VBox) node;
			String bugemon_name = ((Label)(vbox.getChildren().get(0))).getText();
			if (!selected.contains(bugemon_name)) {  // contains name of the bugemon
				vbox.setDisable(true);
			}
		}
	}

	/**
	 * Enables all bugemons if disabled
	 */
	private void enableAllBugemons() {
		for (Node node: availableBugemonsGrid.getChildren()) { // enable all bugemons els
			VBox vbox = (VBox) node;
			vbox.setDisable(false);
		}
	}


	private void onSelectBugemon(String bugemon) {
		if (!selected.contains(bugemon) && selected.size() < LIMITED_BUGEMONS_NUMBER) {
			selected.add(bugemon);
			populateSelectedBugemons();
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
		if (!selected.isEmpty() && selected.size() <= LIMITED_BUGEMONS_NUMBER) {
			gameController.setupTeam(selected);
			gameController.switchToBattleModeWindow(event);
		} else {
			throw new IllegalStateException(
					"Tu dois sélectionner entre 1 et 6 Bugemons pour confirmer ton équipe."
			);
		}
	}

	/**
	 * Returns to the main menu
	 * @param event the action triggered by clicking the return button
	 */
	public void handleReturn(ActionEvent event) throws IOException {
		switchWindow(event, MODE_WINDOW_PATH,gameController);
	}

	public void setGameController(GameController gameController) { this.gameController = gameController;}
}
