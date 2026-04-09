package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.repository.LoadException;
import ulb.service.ServiceLoader;
import ulb.view.WindowPath;
import ulb.view.windows.RegisterWindow;

public class RegisterController implements RegisterWindow.ViewListener {

    private Listener listener;
    private RegisterWindow view;
    private Stage stage;

    public RegisterController(Stage stage, Listener listener) {
        this.listener = listener;
        this.stage = stage;
    }

    public void show() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.REGISTER));
        loader.load();
        view = loader.getController();
        view.setListener(this);

        Parent root = loader.getRoot();
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        this.stage.show();
    }

    @Override
    public void onLogin(String username, String password) {
        try {
            boolean success = ServiceLoader.getAccountService().login(username, password);
            if (success) {
                listener.onRegister();
            } else {
                view.setErrorLabel("Nom d'utilisateur ou mot de passe incorrect.");
            }
        } catch (LoadException e) {
            view.setErrorLabel("Erreur de connexion à la base de données.");
        }
    }

    @Override
    public void onSignUp(String username, String password) {
        try {
            boolean success = ServiceLoader.getAccountService().register(username, password);
            if (success) {
                listener.onRegister();
            } else {
                view.setErrorLabel("Ce nom d'utilisateur est déjà pris.");
            }
        } catch (LoadException e) {
            view.setErrorLabel("Erreur de connexion à la base de données.");
        }
    }

    public interface Listener {
        void onRegister();
    }

}
