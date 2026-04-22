package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ulb.utils.Scaling;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ulb.DTO.bugemon.BugemonSpeciesDTO;

public class CreateTeamWindow extends Window {

	@FXML
	private VBox content;
	@FXML
	private GridPane availableBugemonsGrid;
	@FXML
	private GridPane selectedBugemonsGrid;

	private final int MAX_BUGEMONS = 6;

	private final List<String> selectedBugemonIds = new ArrayList<>();
	private List<BugemonSpeciesDTO> availableBugemons = List.of();
	private ViewListener viewListener;

	/**
	 * Initializes the create team menu.
	 */
	@FXML
	public void initialize() {
		Scaling.applyScaling(content);
	}

	public void setViewListener(ViewListener viewListener) {
		this.viewListener = viewListener;
	}

	/**
	 * Displays the available Bugémons in the selection grid.
	 *
	 * @param availableBugemons the Bugémons that can be selected
	 */
	public void displayAvailableBugemons(List<BugemonSpeciesDTO> availableBugemons) {
		this.availableBugemons = List.copyOf(availableBugemons);
		populateAvailableBugemons();
	}

	/**
	 * Updates the available Bugémons grid by adding a box for each Bugémon in the list.
	 */
	public void populateAvailableBugemons() {
		availableBugemonsGrid.getChildren().clear();

		int col = 0, row = 0;
		for (BugemonSpeciesDTO bugemon : this.viewListener.getAllSpecies()) {

			VBox cell = new VBox(8);
			cell.getStyleClass().add("availableBugemons");

			Label name = new Label(bugemon.getName());

			Image image = new Image(bugemon.getSpritePath());
			ImageView sprite = new ImageView(image);
			sprite.setFitWidth(90);
			sprite.setFitHeight(90);
			sprite.setPreserveRatio(true);

			CheckBox checkBox = new CheckBox();
			checkBox.setSelected(selectedBugemonIds.contains(bugemon.getId()));

			checkBox.setOnAction(e -> {
				if (checkBox.isSelected()) {
					onSelectBugemon(bugemon.getId());
				} else {
					onDeselectBugemon(bugemon.getId());
				}
			});

			cell.getChildren().addAll(name, sprite, checkBox);
			availableBugemonsGrid.add(cell, col, row);
			
			col++; // Switch to the next Bugemon to show
			if (col == 8) {
				col = 0;
				row++;
			}
		}
	}

	/**
	 * Updates the selected Bugémons grid by adding a box for each selected Bugémon.
	 */
	private void populateSelectedBugemons() {
		selectedBugemonsGrid.getChildren().clear();

		int col = 0;
		int row = 0;

		for (String bugemonId : selectedBugemonIds) {
			VBox cell = new VBox();
			cell.getStyleClass().add("selectedBugemonsGrid");

			Label name = new Label(bugemonId);
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
	 * Disable all Bugémons after MAX_BUGEMONS are selected so that no more can be selected.
	 */
	private void checkDisableBugemons() {
		if (selectedBugemonIds.size() == MAX_BUGEMONS) {
			disableAllBugemons();
		} else {
			enableAllBugemons();
		}
	}

	/**
	 * Disable all not selected Bugémons.
	 */
	private void disableAllBugemons() {
		for (Node node : availableBugemonsGrid.getChildren()) {
			VBox vbox = (VBox) node;
			String bugemonName = ((Label)(vbox.getChildren().get(0))).getText();
			if (!selectedBugemonIds.contains(bugemonName)) {  // contains name of the bugemon
				vbox.setDisable(true);
			}
		}
	}

	/**
	 * Enables all Bugémons if they were disabled.
	 */
	private void enableAllBugemons() {
		for (Node node : availableBugemonsGrid.getChildren()) {
			VBox vbox = (VBox) node;
			vbox.setDisable(false);
		}
	}

	/**
	 * Adds the selected Bugémon to the selection list when clicking the checkbox.
	 *
	 * @param bugemonName the id of the Bugémon that was selected
	 */
	private void onSelectBugemon(String bugemonName) {
		if (!selectedBugemonIds.contains(bugemonName) && selectedBugemonIds.size() < MAX_BUGEMONS) {
			selectedBugemonIds.add(bugemonName);
			populateSelectedBugemons();
		}
	}

	/**
	 * Removes the selected Bugémon from the selection list when clicking the checkbox.
	 *
	 * @param bugemonName the id of the Bugémon that was deselected
	 */
	private void onDeselectBugemon(String bugemonName) {
		selectedBugemonIds.remove(bugemonName);
		populateSelectedBugemons();
	}

	/**
	 * Confirms the selected team and asks the controller to switch to the battle window.
	 *
	 * @throws IllegalStateException if the team is empty or has more than 6 Bugémons
	 */
	public void handleConfirmTeam() {
		if (!selectedBugemonIds.isEmpty() && selectedBugemonIds.size() <= MAX_BUGEMONS) {
			viewListener.onConfirmTeam(selectedBugemonIds);
		} else {
			showAlert("confirmer");
		}
	}

	/**
	 * Displays the load team panel
	 */
	public void handleLoadTeam() {
		viewListener.onLoadTeam();
	}


    /**
     * Displays a dialog to input the team's name then handles team saving
     */
	public void handleSaveTeam() {

		if (!selectedBugemonIds.isEmpty() && selectedBugemonIds.size() <= MAX_BUGEMONS) {

			Stage owner = (Stage) content.getScene().getWindow();

			TextInputDialog dialog = new TextInputDialog();
			dialog.initOwner(owner);
			dialog.setTitle("Sauvegarder l'équipe");
			dialog.setHeaderText("Donne un nom à ton équipe");

			// changes the default dialog button names to be in French
			dialog.getDialogPane().getButtonTypes().setAll(
					new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE),
					new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE)
			);

			Optional<String> result = dialog.showAndWait();
			result.ifPresent(teamName -> viewListener.onSaveTeam(selectedBugemonIds, teamName));

		}  else {
			showAlert("sauvegarder");
		}
	}

    /**
	 * Shows an alert when the user tries to save/confirm a team with the wrong Bugemon number
	 *
     * @param action the action (confirm or save) that triggered the alert
     */
	private void showAlert(String action) {
		Stage owner = (Stage) content.getScene().getWindow();
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.initOwner(owner);
		alert.setTitle("Erreur");
		alert.setHeaderText("Équipe invalide");
		alert.setContentText("Tu dois sélectionner entre 1 et 6 Bugémons pour " + action.toLowerCase() + " ton équipe.");

		alert.showAndWait();
	}

	/**
	 * Returns to the main menu.
	 */
	public void handleReturn() {
		viewListener.onReturn();
	}

	public interface ViewListener {
		void onConfirmTeam(List<String> selectedBugemonIds);
		void onLoadTeam();
		void onSaveTeam(List<String> selectedBugemonIds, String teamName);
		void onReturn();
		List<BugemonSpeciesDTO> getAllSpecies();
	}
}
