package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ulb.utils.Scaling;

public class RegisterWindow extends Window {

    @FXML private VBox content;
    @FXML private Label titleLabel;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;
    @FXML private Button mainButton;
    @FXML private Button toggleButton;

    private boolean isRegisterMode = false;
    private ViewListener viewListener;

    public void setListener(ViewListener viewListener) { this.viewListener = viewListener; }

    @FXML
    public void initialize() {
        Scaling.applyScaling(content);
    }

    @FXML
    private void onToggleMode() {
        isRegisterMode = !isRegisterMode;
        errorLabel.setText("");
        if (isRegisterMode) {
            titleLabel.setText("Créer un compte");
            mainButton.setText("S'inscrire");
            toggleButton.setText("Déjà un compte ? Se connecter");
            confirmPasswordField.setVisible(true);
            confirmPasswordField.setManaged(true);
        } else {
            titleLabel.setText("Connexion");
            mainButton.setText("Se connecter");
            toggleButton.setText("Pas de compte ? S'inscrire");
            confirmPasswordField.setVisible(false);
            confirmPasswordField.setManaged(false);
            confirmPasswordField.clear();
        }
    }

    @FXML
    private void onMainAction() {
        if (isRegisterMode) {
            onSignUp();
        } else {
            onLogin();
        }
    }

    private void onSignUp() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirm = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Remplis tous les champs.");
            return;
        }

        if (!password.equals(confirm)) {
            errorLabel.setText("Les mots de passe ne correspondent pas.");
            return;
        }

        viewListener.onSignUp(username, password);
    }

    private void onLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Remplis tous les champs.");
            return;
        }

        viewListener.onLogin(username, password);
    }

    public void setErrorLabel(String error) {
        errorLabel.setText(error);
    }

    public interface ViewListener {
        void onLogin(String username, String password);
        void onSignUp(String username, String password);
    }
}
