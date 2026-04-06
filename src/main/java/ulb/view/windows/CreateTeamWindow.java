package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

import ulb.model.bugemon.BugemonSpecies;
import ulb.utils.Scaling;
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
	private GridPane selectedBugemonsGrid;

	private final List<String> selectedBugemons = new ArrayList<>();
	private final int MAX_BUGEMONS =6;

	private ViewListener viewListener;

	/**
	 * Initializes the create team menu
	 */
	@FXML
	public void initialize() {
		populateAvailableBugemons();
		Scaling.applyScaling(content);
	}

	public void setViewListener(ViewListener viewListener) {
		this.viewListener = viewListener;
	}

	/**
	 * Updates the available bugemons grid by adding a box for each bugemon in the list
	 */
	private void populateAvailableBugemons() {
		availableBugemonsGrid.getChildren().clear();

		// TO DO : remove
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
			checkBox.setSelected(selectedBugemons.contains(bugemon.getName()));

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
	 * Updates the selectedBugemons bugemons grid by adding a box for each selectedBugemons bugemon
	 */
	private void populateSelectedBugemons() {
		selectedBugemonsGrid.getChildren().clear();

		int col = 0, row = 0;

		for (String bugemon : selectedBugemons) {
			VBox cell = new VBox();
			cell.getStyleClass().add("selectedBugemonsGrid");

			Label name = new Label(bugemon);
			cell.getChildren().add(name);

			selectedBugemonsGrid.add(cell, col, row);

			col++;
			if (col == 8) {
				col = 0;
				row++;
			}
		}
		checkDisableBugemons();
	}

	/**
	 * Disable all bugemons after @MAX_BUGEMONS are selected so that no more can be selected
	 */
	private void checkDisableBugemons() {
		if (selectedBugemons.size() == MAX_BUGEMONS) {
			disableAllBugemons();
		}
		else {
			enableAllBugemons();
		}
	}

	/**
	 * Disable all not selected bugemons
	 */
	private void disableAllBugemons(){
		for (Node node: availableBugemonsGrid.getChildren()) {
			VBox vbox = (VBox) node;
			String bugemon_name = ((Label)(vbox.getChildren().get(0))).getText();
			if (!selectedBugemons.contains(bugemon_name)) {  // contains name of the bugemon
				vbox.setDisable(true);
			}
		}
	}

	/**
	 * Enables all bugemons if they were disabled
	 */
	private void enableAllBugemons() {
		for (Node node: availableBugemonsGrid.getChildren()) { // enable all bugemons els
			VBox vbox = (VBox) node;
			vbox.setDisable(false);
		}
	}

	/**
	 * Adds the selected bugemon to the selectedBugemons list when clicking the checkbox
	 *
	 * @param bugemon the name of the bugemon that was selected
	 */
	private void onSelectBugemon(String bugemon) {
		if (!selectedBugemons.contains(bugemon) && selectedBugemons.size() < MAX_BUGEMONS) {
			selectedBugemons.add(bugemon);
			populateSelectedBugemons();
		}
	}

	/**
	 * Removes the selected bugemon from the selectedBugemons list when clicking the checkbox
	 *
	 * @param bugemon the name of the bugemon that was deselected
	 */
	private void onDeselectBugemon(String bugemon) {
		selectedBugemons.remove(bugemon);
		populateSelectedBugemons();
	}

	/**
	 * Confirms the selectedBugemons team and asks the controller to switch to the battle window
	 * then, closes the current window
	 * @throws IllegalStateException if the team is empty or has more than 6 bugemons
	 */
	public void handleConfirmTeam() {
		if (!selectedBugemons.isEmpty() && selectedBugemons.size() <= MAX_BUGEMONS) {
			viewListener.onConfirmTeam(selectedBugemons);
		} else {
			throw new IllegalStateException("Tu dois sélectionner entre 1 et 6 Bugémons pour confirmer ton équipe.");
		}
	}

	/**
	 * Returns to the main menu
	 */
	public void handleReturn() {
		viewListener.onReturn();
	}

	public interface ViewListener {
		void onConfirmTeam(List<String> selectedBugemons);
		void onReturn();
	}

}
