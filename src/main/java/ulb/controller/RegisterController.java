package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.view.FxmlLoader;
import ulb.view.WindowPath;
import ulb.view.windows.RegisterWindow;

public class RegisterController implements RegisterWindow.ViewListener {

    private Listener listener;
    private RegisterWindow view;
    private Stage stage;

    public RegisterController(Stage stage, Listener listener) {
        this.stage = stage;
        this.listener = listener;
    }

    /**
     * Displays the register/login view.
     */
    public void show() {
        FXMLLoader loader = FxmlLoader.load(this, WindowPath.REGISTER);
        view = loader.getController();
        view.setViewListener(this);

        Parent root = loader.getRoot();
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        this.stage.show();
    }

    public RegisterWindow getView(){return this.view;}

    /**
     * Handles login form submission.
     *
     * @param username The entered username
     * @param password The entered password
     */
    @Override
    public void onLogin(String username, String password) {
        listener.onLogin(username,password);
    }

    /**
     * Handles sign-up form submission.
     *
     * @param username The entered username
     * @param password The entered password
     */
    @Override
    public void onSignUp(String username, String password) {
        this.listener.onSignUp(username, password);
    }

    public interface Listener{
        void onLogin(String username, String password);
        void onSignUp(String username, String password);
    }
}
