package ulb.controller.windows;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.item.ItemDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.controller.ClientController;
import ulb.repository.LoadException;
import ulb.view.WindowPath;
import ulb.view.windows.RegisterWindow;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisterController extends WindowController<RegisterWindow> implements RegisterWindow.ViewListener {

    private ClientController clientController;

    public RegisterController(Stage stage, ClientController clientController) {
        this.stage = stage;
        this.windowPath = WindowPath.REGISTER;
        this.clientController = clientController;

        try {
            this.init();
        } catch (Exception e) {
            System.err.println("Couldn't load the FXML file : " + e);
        }
        this.view.setListener(this);
    }

    @Override
    public void onLogin(String username, String password) {
        try {
            this.clientController.setPlayer(new PlayerDTO(username, password, new ArrayList<>(), new HashMap<>()));
            boolean success = clientController.logIn(this.clientController.getPlayer());
            if (success) {
                this.onRegister();
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
            this.clientController.setPlayer(new PlayerDTO(username, password, new ArrayList<>(), new HashMap<>()));
            boolean success = clientController.signUp(this.clientController.getPlayer());
            if (success) {
                this.onRegister();
            } else {
                view.setErrorLabel("Ce nom d'utilisateur est déjà pris.");
            }
        } catch (LoadException e) {
            view.setErrorLabel("Erreur de connexion à la base de données.");
        }
    }

    public void onRegister() {
        this.clientController.getModeController().show();
    }
}
