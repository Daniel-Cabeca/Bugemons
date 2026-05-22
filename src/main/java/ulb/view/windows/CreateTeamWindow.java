package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ulb.DTO.bugemon.BugemonSpeciesDTO;
import ulb.utils.Scaling;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CreateTeamWindow {

	private final int MAX_BUGEMONS = 6;
	private final List<String> selectedBugemonIds = new ArrayList<>();
	@FXML
	private VBox content;
	@FXML
	private GridPane availableBugemonsGrid;
	@FXML
	private GridPane selectedBugemonsGrid;
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
	 * Updates the available Bugémons grid by adding a box for each Bugémon in the list.
	 */
	public void populateAvailableBugemons() {
		availableBugemonsGrid.getChildren().clear();

		int col = 0, row = 0;
		for (BugemonSpeciesDTO bugemon : this.viewListener.getAllSpecies()) {

			VBox cell = new VBox(8);
			cell.getStyleClass().add("availableBugemons");

			Label name = new Label(bugemon.name());

			Image image = new Image(bugemon.getSpritePath());
			ImageView sprite = new ImageView(image);
			sprite.setFitWidth(90);
			sprite.setFitHeight(90);
			sprite.setPreserveRatio(true);

			CheckBox checkBox = new CheckBox();
			checkBox.setSelected(selectedBugemonIds.contains(bugemon.id()));

			checkBox.setOnAction(e -> {
				if (checkBox.isSelected()) {
					onSelectBugemon(bugemon.id());
				} else {
					onDeselectBugemon(bugemon.id());
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
			String bugemonName = ((Label) (vbox.getChildren().get(0))).getText();
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
			showWrongBugemonNumberAlert("confirmer");
		}
	}

	/**
	 * Shows an alert when the user tries to save/confirm a team with the wrong Bugemon number.
	 *
	 * @param action the action (confirm or save) that triggered the alert
	 */
	private void showWrongBugemonNumberAlert(String action) {
		Stage owner = (Stage) content.getScene().getWindow();
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.initOwner(owner);
		alert.setTitle("Erreur");
		alert.setHeaderText("Équipe invalide");
		alert.setContentText("Tu dois sélectionner entre 1 et 6 Bugémons pour " + action.toLowerCase() + " ton équipe" + ".");

		alert.showAndWait();
	}

	/**
	 * Displays the load team panel.
	 */
	public void handleLoadTeam() {
		viewListener.onLoadTeam();
	}

	/**
	 * Displays a dialog to input the team's name then handles team saving.
	 */
	public void handleSaveTeam() {

		if (!selectedBugemonIds.isEmpty() && selectedBugemonIds.size() <= MAX_BUGEMONS) {

			Stage owner = (Stage) content.getScene().getWindow();

			TextInputDialog dialog = new TextInputDialog();
			dialog.initOwner(owner);
			dialog.setTitle("Sauvegarder l'équipe");
			dialog.setHeaderText("Donne un nom à ton équipe");

			// changes the default dialog button names to be in French
			dialog.getDialogPane().getButtonTypes().setAll(new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE),
					new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE));

			Optional<String> result = dialog.showAndWait();
			result.ifPresent(teamName -> {
				if (teamName.isBlank()) {
					showInvalidSaveAlert("Tu dois donner un nom à ton équipe!");
					return;
				}
				viewListener.onSaveTeam(selectedBugemonIds, teamName);
			});

		} else {
			showWrongBugemonNumberAlert("sauvegarder");
		}
	}

	/**
	 * Shows an alert when the user tries to save a team with no name or with an already existing name
	 *
	 * @param message the message to display in the alert
	 */
	public void showInvalidSaveAlert(String message) {
		Stage owner = (Stage) content.getScene().getWindow();
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.initOwner(owner);
		alert.setTitle("Erreur");
		alert.setHeaderText("Sauvegarde impossible");
		alert.setContentText(message);

		alert.showAndWait();
	}

	/**
	 * Returns to the main menu.
	 */
	public void handleReturn() {
		viewListener.onReturn();
	}

	public interface ViewListener {
		/**
		 * Handles team confirmation from the view.
		 * @param selectedBugemonIds The selected species ids
		 */
		void onConfirmTeam(List<String> selectedBugemonIds);

		/**
		 * Handles team loading by opening the load team panel
		 */
		void onLoadTeam();

		/**
		 * Handles saving the team to the database
		 * @param selectedBugemonIds The list of the ids of the team members
		 * @param teamName The name of the team
		 */
		void onSaveTeam(List<String> selectedBugemonIds, String teamName);

		/**
		 * Handles return action from the team creation screen.
		 */
		void onReturn();

		/**
		 * Returns the list of all the Bugemon species.
		 * @return A list of all the species of Bugemon
		 */
		List<BugemonSpeciesDTO> getAllSpecies();
	}
}
