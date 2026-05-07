package ulb.controller.windows;

import javafx.stage.Stage;
import java.util.logging.Level;
import java.util.logging.Logger;

import ulb.DTO.player.PlayerDTO;
import ulb.DTO.player.PlayerRegisterDTO;
import ulb.exceptions.LoadException;
import ulb.view.windows.RegisterWindow;

public class RegisterController extends WindowController<RegisterWindow> implements RegisterWindow.ViewListener {
    private final Listener listener;
    private static final Logger LOGGER = Logger.getLogger(RegisterController.class.getName());


    public RegisterController(Stage stage, String windowPath, Listener listener){
        super(stage, windowPath);
        this.view.setViewListener(this);
        this.listener = listener;

    }

    /**
     * Handles login form submission.
     *
     * @param userName The entered username
     * @param password The entered password
     */
    @Override
    public void onLogin(String userName, String password){
        try {
            PlayerRegisterDTO playerDTO = new PlayerRegisterDTO(userName, password);
            boolean success = this.listener.onLogin(playerDTO);
            if (success){
                if (this.listener.onGetPlayer(userName) == null) {
                    throw new RuntimeException("Player is null after login"); // TODO
                }
                try {
                    this.listener.showModeWindow();
                }catch (Exception e){
                    LOGGER.log(Level.WARNING, "Impossible d'afficher l'écran de sélection du mode après connexion.", e);
                }
            } else {
                this.view.setErrorLabel("Nom d'utilisateur ou mot de passe incorrect.");
            }
        } catch (LoadException e) {
            this.view.setErrorLabel("Erreur de connexion à la base de données.");
        }
    }

    /**
     * Handles sign-up form submission.
     *
     * @param userName The entered username
     * @param password The entered password
     */
    @Override
    public void onSignUp(String userName, String password){
        try {
            PlayerRegisterDTO playerDTO = new PlayerRegisterDTO(userName, password);
            boolean success = this.listener.onSignUp(playerDTO);
            if (success) {
                if (this.listener.onGetPlayer(userName) == null) {
                    throw new RuntimeException("Player is null after login"); // TODO:
                }
                try {
                    this.listener.showModeWindow();
                }catch (Exception e){
                    LOGGER.log(Level.WARNING, "Impossible d'afficher l'écran de sélection du mode après inscription.", e);
                }
            } else {
                this.view.setErrorLabel("Ce nom d'utilisateur est déjà pris.");
            }
        } catch (LoadException e) {
            this.view.setErrorLabel("Erreur de connexion à la base de données.");
        }
    }

    public interface Listener{
        boolean onLogin(PlayerRegisterDTO playerRegisterDTO);
        void showModeWindow();
        PlayerDTO onGetPlayer(String userName);
        boolean onSignUp(PlayerRegisterDTO playerDTO);
    }
}
