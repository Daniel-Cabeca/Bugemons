package ulb.controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.view.FxmlLoader;
import ulb.exceptions.ViewLoadException;
import ulb.view.WindowPath;
import ulb.view.windows.AttackReplacementWindow;

/**
 * Controller handling the attack replacement flow after learning a new ability.
 */
public class AttackReplacementController implements AttackReplacementWindow.ViewListener {
    private final Stage stage;
    private final Listener listener;
    private AttackReplacementWindow view;
    private BugemonDTO currentBugemon;
    private AbilityDTO currentNewAbility;

    /**
     * Creates the controller with its stage and callback listener.
     *
     * @param stage The application stage
     * @param listener The listener notified of user actions
     */
    public AttackReplacementController(Stage stage, Listener listener) {
        this.stage = stage;
        this.listener = listener;
    }

    /**
     * Displays the attack replacement screen for the given bugemon and new ability.
     *
     * @param bugemon The bugemon learning a new ability
     * @param newAbility The new ability to be learned
     */
    public void show(BugemonDTO bugemon, AbilityDTO newAbility) throws ViewLoadException {
        currentBugemon = bugemon;
        currentNewAbility = newAbility;
        FXMLLoader loader = FxmlLoader.load(this, WindowPath.ATTACK_REPLACEMENT);
        view = loader.getController();
        view.setViewListener(this);
        view.initializeReplacement(bugemon, newAbility);
        Parent root = loader.getRoot();
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        stage.show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onReplaceAbility(AbilityDTO oldAbility) {
        listener.onAttackReplaced(currentBugemon, currentNewAbility, oldAbility);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onReturnToChooseBugemon() {
        listener.onReturnToChooseBugemon();
    }

    /**
     * Listener for attack replacement events.
     */
    public interface Listener {
        /** Handles the replacement of an old ability by a new one for a given bugemon. */
        void onAttackReplaced(BugemonDTO bugemon, AbilityDTO newAbility, AbilityDTO oldAbility);
        /** Handles returning to the choose bugemon screen. */
        void onReturnToChooseBugemon();
    }
}