package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ulb.repository.LoadException;
import ulb.utils.Scaling;
import ulb.view.WindowPath;

public class RegisterWindow extends Window {

    @FXML private VBox content;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        Scaling.applyScaling(content);
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            errorLabel.setText("Remplis tous les champs.");
            return;
        }

        if (!password.equals(confirm)) {
            errorLabel.setText("Les mots de passe ne correspondent pas.");
            return;
        }

        try {
            boolean success = viewManager.getAccountService().register(username, password);
            if (success) {
                viewManager.setLoggedInUsername(username);
                sendSwitchWindowMessage(WindowPath.MODE);
            } else {
                errorLabel.setText("Ce nom d'utilisateur est déjà pris.");
            }
        } catch (LoadException e) {
            errorLabel.setText("Erreur de connexion à la base de données.");
        }
    }

    @FXML
    private void goToLogin() {
        sendSwitchWindowMessage(WindowPath.LOGIN);
    }
}
