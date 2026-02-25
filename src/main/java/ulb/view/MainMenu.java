package ulb.view;

import javafx.fxml.FXML;
import javafx.application.Platform;

public class MainMenu {

    @FXML
    private void handleCreateTeam() {
        System.out.println("Create Team button clicked.");
        // connect to actual team creation later
    }

    @FXML
    private void handleExit() {
        Platform.exit();
        System.exit(0);
    }
}
