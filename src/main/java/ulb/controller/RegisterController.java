package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.DTO.player.PlayerDTO;
import ulb.repository.LoadException;
import ulb.service.AccountService;
import ulb.view.WindowPath;
import ulb.view.windows.RegisterWindow;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisterController implements RegisterWindow.ViewListener {

    private Listener listener;
    private RegisterWindow view;
    private Stage stage;
	private final AccountService accountService;

    public RegisterController(Stage stage, Listener listener, AccountService accountService) {
        this.stage = stage;
        this.listener = listener;
		this.accountService = accountService;
    }

	AccountService getAccountService() { return this.accountService; }

    public void show() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.REGISTER));
        loader.load();
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

    @Override
    public void onLogin(String username, String password) {
        listener.onLogin(username,password);
    }

    @Override
    public void onSignUp(String username, String password) {
        this.listener.onSignUp(username, password);
    }


    public interface Listener{
        void onLogin(String username, String password);
        void onSignUp(String username, String password);
    }
}
