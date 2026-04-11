package ulb.view.windows;

import javafx.fxml.FXML;
import ulb.view.WindowPath;

public class GenderChoiceWindow extends Window {

    @FXML
    private void handleMale() {
        gameController.setPendingGender("male");
        switchWindow(WindowPath.INTRO);
    }

    @FXML
    private void handleFemale() {
        gameController.setPendingGender("female");
        switchWindow(WindowPath.INTRO);
    }
}
