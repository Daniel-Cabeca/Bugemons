package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.item.ItemDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.communication.Client;
import ulb.communication.message.clientToServer.RegisterMessage;
import ulb.controller.windows.ModeController;
import ulb.repository.LoadException;
import ulb.service.ServiceLoader;
import ulb.view.WindowPath;
import ulb.view.windows.RegisterWindow;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisterController implements RegisterWindow.ViewListener {

    private RegisterWindow view;
    private Stage stage;
    private ModeController modeController;
    private ClientController clientController;
    private PlayerDTO player;

    public RegisterController(Stage stage) {
        this.stage = stage;
    }

    public void setClientController(ClientController clientController){
        this.clientController = clientController;
    }

    public PlayerDTO getPlayer() {
        return this.player;
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
            player = new PlayerDTO(username, password, new ArrayList<BugemonDTO>(), new HashMap<ItemDTO, Integer>());
            boolean success = clientController.postData(new RegisterMessage(player, true));
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
            player = new PlayerDTO(username, password, new ArrayList<BugemonDTO>(), new HashMap<ItemDTO, Integer>());
            boolean success = clientController.postData(new RegisterMessage(player, false));
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
        modeController = new ModeController(stage);
        this.modeController.setClientController(this.clientController);
        this.modeController.setPlayer(this.player);
        modeController.show();
    }


}
