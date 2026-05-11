package ulb.controller.windows;
import javafx.stage.Stage;
import ulb.DTO.player.PlayerRegisterDTO;
import ulb.controller.ClientController;
import ulb.message.request.setup.RegisterRequest;
import ulb.view.WindowPath;
import ulb.view.windows.RegisterWindow;

/**
 * Controller for the register and login screen.
 */
public class RegisterController extends WindowController<RegisterWindow> implements RegisterWindow.ViewListener {

    /**
     * Creates the register controller and attaches it to the view.
     *
     * @param stage The application stage
     * @param  clientController The client controller
     */
    public RegisterController(Stage stage, ClientController clientController){
        super(stage, WindowPath.REGISTER, clientController);
        this.view.setViewListener(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLogin(String userName, String password){
        PlayerRegisterDTO playerDTO = new PlayerRegisterDTO(userName, password);
        boolean success = this.logIn(playerDTO);
        if (success){
            if (this.clientController.loadPlayer(userName) == null) {
                LOGGER.warning("Player is null after login for username: " + userName);
                this.view.setErrorLabel("Connexion réussie, mais profil joueur introuvable.");
                return;
            }
            this.clientController.showWindow(WindowName.MODE);
        } else {
            this.view.setErrorLabel("Nom d'utilisateur ou mot de passe incorrect.");
        }
    }

    /**
     * Sends a login request
     *
     * @param player Player credentials DTO
     * @return True if accepted by server
     */
    public boolean logIn(PlayerRegisterDTO player){
        return this.clientController.postData(new RegisterRequest(player, true));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onSignUp(String userName, String password){
        PlayerRegisterDTO playerDTO = new PlayerRegisterDTO(userName, password);
        boolean success = this.signUp(playerDTO);
        if (success) {
            if (this.clientController.loadPlayer(userName) == null) {
                LOGGER.warning("Player is null after sign-up for username: " + userName);
                this.view.setErrorLabel("Inscription réussie, mais profil joueur introuvable.");
                return;
            }
            this.clientController.showWindow(WindowName.MODE);
        } else {
            this.view.setErrorLabel("Ce nom d'utilisateur est déjà pris.");
        }
    }

    /**
     * Sends a sign-up request for a player.
     *
     * @param player Player registration DTO
     * @return True if account creation succeeded
     */
    public boolean signUp(PlayerRegisterDTO player){
        return this.clientController.postData(new RegisterRequest(player, false));
    }
}