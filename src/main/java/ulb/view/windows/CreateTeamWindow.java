package ulb.view.windows;

import ulb.controller.BattleController;
import ulb.controller.GameController;
import ulb.model.battle.Battle;
import ulb.model.team.OpponentTeamGenerator;
import ulb.model.team.Team;
import ulb.controller.strategy.StrategyRandom;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ulb.model.Player;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.BugemonSpecies;
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

	private static final double BASE_WIDTH = 1000;
	private static final double BASE_HEIGHT = 700;



	/**
	 * Initializes the create team menu
	 */
	@FXML
	public void initialize() {
		populateAvailableBugemons();

		content.sceneProperty().addListener((obs, oldScene, scene) -> {
			if (scene != null) {

				scene.widthProperty().addListener((o, oldVal, newVal) -> scale(scene));
				scene.heightProperty().addListener((o, oldVal, newVal) -> scale(scene));

				scale(scene);
			}
		});
	}

	private void scale(javafx.scene.Scene scene) {
		double scaleX = scene.getWidth() / BASE_WIDTH;
		double scaleY = scene.getHeight() / BASE_HEIGHT;

		double scale = Math.min(scaleX, scaleY);

		content.setScaleX(scale);
		content.setScaleY(scale);
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
	}

	private void onSelectBugemon(String bugemon) {
		if (!selected.contains(bugemon) && selected.size() < 6) {
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
		if (!selected.isEmpty() && selected.size() <= 6) {
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