package ulb.controller.windows;
import javafx.stage.Stage;
import java.util.logging.Level;
import java.util.logging.Logger;
import ulb.DTO.player.PlayerDTO;
import ulb.DTO.player.PlayerRegisterDTO;
import ulb.exceptions.LoadException;
import ulb.exceptions.ViewLoadException;
import ulb.view.windows.RegisterWindow;

/**
 * Controller for the register and login screen.
 */
public class RegisterController extends WindowController<RegisterWindow> implements RegisterWindow.ViewListener {
    private final Listener listener;
    private static final Logger LOGGER = Logger.getLogger(RegisterController.class.getName());

    /**
     * Creates the register controller and attaches it to the view.
     *
     * @param stage The application stage
     * @param windowPath The path to the register window
     * @param listener The listener notified of user actions
     * @throws ViewLoadException
     */
    public RegisterController(Stage stage, String windowPath, Listener listener) throws ViewLoadException {
        super(stage, windowPath);
        this.view.setViewListener(this);
        this.listener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onLogin(String userName, String password){
        try {
            PlayerRegisterDTO playerDTO = new PlayerRegisterDTO(userName, password);
            boolean success = this.listener.onLogin(playerDTO);
            if (success){
                if (this.listener.onGetPlayer(userName) == null) {
                    LOGGER.warning("Player is null after login for username: " + userName);
                    this.view.setErrorLabel("Connexion réussie, mais profil joueur introuvable.");
                    return;
                }
                this.listener.showModeWindow();
            } else {
                this.view.setErrorLabel("Nom d'utilisateur ou mot de passe incorrect.");
            }
        } catch (LoadException e) {
            this.view.setErrorLabel("Erreur de connexion à la base de données.");
        } catch (ViewLoadException e) {
            LOGGER.log(Level.WARNING, "Impossible d'afficher l'écran de sélection du mode après connexion.", e);
            this.view.setErrorLabel("Impossible d'afficher l'écran suivant.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSignUp(String userName, String password){
        try {
            PlayerRegisterDTO playerDTO = new PlayerRegisterDTO(userName, password);
            boolean success = this.listener.onSignUp(playerDTO);
            if (success) {
                if (this.listener.onGetPlayer(userName) == null) {
                    LOGGER.warning("Player is null after sign-up for username: " + userName);
                    this.view.setErrorLabel("Inscription réussie, mais profil joueur introuvable.");
                    return;
                }
                this.listener.showModeWindow();
            } else {
                this.view.setErrorLabel("Ce nom d'utilisateur est déjà pris.");
            }
        } catch (ViewLoadException e) {
            LOGGER.log(Level.WARNING, "Impossible d'afficher l'écran de sélection du mode après inscription.", e);
            this.view.setErrorLabel("Impossible d'afficher l'écran suivant.");
        }
    }

    /**
     * Listener for register and login actions.
     */
    public interface Listener{
        /** Handles the login attempt with the given credentials. */
        boolean onLogin(PlayerRegisterDTO playerRegisterDTO) throws LoadException;
        /** Handles switch to the mode selection window. */
        void showModeWindow() throws ViewLoadException;
        /** Retrieves the player DTO for the given username. */
        PlayerDTO onGetPlayer(String userName);
        /** Handles the sign-up attempt with the given credentials. */
        boolean onSignUp(PlayerRegisterDTO playerDTO);
    }
}