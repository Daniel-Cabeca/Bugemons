package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.repository.LoadException;
import ulb.service.AccountService;
import ulb.view.WindowPath;
import ulb.view.windows.RegisterWindow;

/**
 * Controller for login and registration actions.
 */
public class RegisterController implements RegisterWindow.ViewListener {

    private Listener listener;
    private RegisterWindow view;
    private Stage stage;
	private final AccountService accountService;

    /**
     * Creates the register controller.
     *
     * @param stage The application stage
     * @param listener The listener notified after successful authentication
     * @param accountService The account service used for authentication and registration
     */
    public RegisterController(Stage stage, Listener listener, AccountService accountService) {
        this.listener = listener;
        this.stage = stage;
		this.accountService = accountService;
    }

	/**
	 * Returns the account service used by this controller.
	 *
	 * @return The account service
	 */
	AccountService getAccountService() { return this.accountService; }

    /**
     * Displays the register/login view.
     *
     * @throws Exception If the FXML cannot be loaded
     */
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

    /**
     * Handles login form submission.
     *
     * @param username The entered username
     * @param password The entered password
     */
    @Override
    public void onLogin(String username, String password) {
        try {
            boolean success = this.getAccountService().login(username, password);
            if (success) {
                listener.onRegister();
            } else {
                view.setErrorLabel("Nom d'utilisateur ou mot de passe incorrect.");
            }
        } catch (LoadException e) {
            view.setErrorLabel("Erreur de connexion à la base de données.");
        }
    }

    /**
     * Handles sign-up form submission.
     *
     * @param username The entered username
     * @param password The entered password
     */
    @Override
    public void onSignUp(String username, String password) {
        try {
            boolean success = this.getAccountService().register(username, password);
            if (success) {
                listener.onRegister();
            } else {
                view.setErrorLabel("Ce nom d'utilisateur est déjà pris.");
            }
        } catch (LoadException e) {
            view.setErrorLabel("Erreur de connexion à la base de données.");
        }
    }

    /**
     * Listener for authentication completion events.
     */
    public interface Listener {
        /** Called after successful login or registration. */
        void onRegister();
    }

}
