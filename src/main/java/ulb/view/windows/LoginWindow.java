package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ulb.repository.LoadException;
import ulb.utils.Scaling;
import ulb.view.WindowPath;
/*
public class LoginWindow extends Window {

    @FXML private VBox content;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        Scaling.applyScaling(content);
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Remplis tous les champs.");
            return;
        }

        try {
            boolean success = viewManager.getAccountService().login(username, password);
            if (success) {
                viewManager.setLoggedInUsername(username);
                sendSwitchWindowMessage(WindowPath.MODE);
            } else {
                errorLabel.setText("Nom d'utilisateur ou mot de passe incorrect.");
            }
        } catch (LoadException e) {
            errorLabel.setText("Erreur de connexion à la base de données.");
        }
    }

    @FXML
    private void goToRegister() {
        sendSwitchWindowMessage(WindowPath.REGISTER);
    }
}
*/