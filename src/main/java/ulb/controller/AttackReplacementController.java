package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
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

    public void show(BugemonDTO bugemon, AbilityDTO newAbility) throws Exception {
        currentBugemon = bugemon;
        currentNewAbility = newAbility;

        FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.ATTACK_REPLACEMENT));
        loader.load();
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
     * Handles the selection of the ability to replace.
     *
     * @param oldAbility The ability chosen for replacement
     */
    @Override
    public void onReplaceAbility(AbilityDTO oldAbility) {
        listener.onAttackReplaced(currentBugemon, currentNewAbility, oldAbility);
    }

    /**
     * Handles returning to the choose bugemon screen.
     */
    @Override
    public void onReturnToChooseBugemon() {
        listener.onReturnToChooseBugemon();
    }

    /**
     * Listener for attack replacement events.
     */
    public interface Listener {
        void onAttackReplaced(BugemonDTO bugemon, AbilityDTO newAbility, AbilityDTO oldAbility);
        void onReturnToChooseBugemon();
    }
}
