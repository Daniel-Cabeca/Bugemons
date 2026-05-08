package ulb.controller.windows;
import javafx.stage.Stage;
import java.util.logging.Logger;
import ulb.DTO.player.PlayerRegisterDTO;
import ulb.controller.ClientController;
import ulb.exceptions.ViewLoadException;
import ulb.view.WindowPath;
import ulb.view.windows.RegisterWindow;

/**
 * Controller for the register and login screen.
 */
public class RegisterController extends WindowController<RegisterWindow> implements RegisterWindow.ViewListener {
    private final ClientController clientController;
    private static final Logger LOGGER = Logger.getLogger(RegisterController.class.getName());

    /**
     * Creates the register controller and attaches it to the view.
     *
     * @param stage The application stage
     * @param clientController The application controller
     * @throws ViewLoadException
     */
    public RegisterController(Stage stage, ClientController clientController) throws ViewLoadException {
        super(stage, WindowPath.REGISTER);
        this.view.setViewListener(this);
        this.clientController = clientController;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLogin(String userName, String password){
        PlayerRegisterDTO playerDTO = new PlayerRegisterDTO(userName, password);
        boolean success = this.clientController.logIn(playerDTO);
        if (success){
            if (this.clientController.loadPlayer(userName) == null) {
                LOGGER.warning("Player is null after login for username: " + userName);
                this.view.setErrorLabel("Connexion réussie, mais profil joueur introuvable.");
                return;
            }
            this.clientController.switchToModeWindow();
        } else {
            this.view.setErrorLabel("Nom d'utilisateur ou mot de passe incorrect.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSignUp(String userName, String password){
        PlayerRegisterDTO playerDTO = new PlayerRegisterDTO(userName, password);
        boolean success = this.clientController.signUp(playerDTO);
        if (success) {
            if (this.clientController.loadPlayer(userName) == null) {
                LOGGER.warning("Player is null after sign-up for username: " + userName);
                this.view.setErrorLabel("Inscription réussie, mais profil joueur introuvable.");
                return;
            }
            this.clientController.switchToModeWindow();
        } else {
            this.view.setErrorLabel("Ce nom d'utilisateur est déjà pris.");
        }
    }
}