package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ulb.view.WindowPath;

public class NameChoiceWindow extends Window {

    @FXML private TextField nameField;
    @FXML private Label errorLabel;

    @FXML
    private void handleConfirm() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            errorLabel.setText("Entre un prénom.");
            return;
        }
        gameController.setPendingPlayerName(name);
        switchWindow(WindowPath.INTRO);
    }
}
