package ulb.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateTeamMenu {

    @FXML
    private GridPane availableBugemons;
    @FXML
    private GridPane selectedBugemons;

    private final List<String> selected = new ArrayList<>();

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
        // Placeholder list with bugemon names -> to be replaced with actual bugemons and their sprites
        String[] bugemons = {"Florachu", "Moussil", "Verdurion",
                "Exceflam", "Pyricore", "Inferlin",
                "Commitide", "Mergeau", "Refaquix"};

        int col = 0, row = 0;

        for (String bugemon : bugemons) {
            VBox cell = new VBox(5);
            cell.setStyle("-fx-border-color: gray; -fx-padding: 10; -fx-alignment: center;");

            Label name = new Label(bugemon);
            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(selected.contains(bugemon));

            checkBox.setOnAction(e -> {
                if (checkBox.isSelected()) {
                    onSelectBugemon(bugemon);
                } else {
                    onDeselectBugemon(bugemon);
                }
            });

            cell.getChildren().addAll(name, checkBox);
            availableBugemons.add(cell, col, row);

            // Ensures there are 3 columns in the grid
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
            VBox cell = new VBox(5);
            cell.setStyle("-fx-border-color: gray; -fx-padding: 10; -fx-alignment: center;");

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


    public void handleConfirmTeam(ActionEvent event) {
        if (!selected.isEmpty() && selected.size() <= 6) {}
        System.out.println("Team confirmed");
        // connect to battle later
    }

    /**
     * Returns to the main menu
     * @param event the action triggered by clicking the return button
     */
    public void handleReturn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ulb/view/MainMenu.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }
}
