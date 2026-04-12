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

    @FXML
    private VBox content;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label errorLabel;

    private ViewListener viewListener;

    public void setViewListener(ViewListener viewListener) { this.viewListener = viewListener; }

    @FXML
    public void initialize() {
        Scaling.applyScaling(content);
    }

    @FXML
    private void onSignUp() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            setErrorLabel("Remplis tous les champs.");
            return;
        }

        viewListener.onSignUp(username, password);
    }

    public void setErrorLabel(String error) {
        errorLabel.setText(error);
    }


    @FXML
    private void onLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            setErrorLabel("Remplis tous les champs.");
            return;
        }

        viewListener.onLogin(username, password);
    }

    public interface ViewListener {
        void onLogin(String username, String password);
        void onSignUp(String username, String password);
    }
}