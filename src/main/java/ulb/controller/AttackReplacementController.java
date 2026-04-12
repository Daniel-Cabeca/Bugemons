package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.view.WindowPath;
import ulb.view.windows.AttackReplacementWindow;

public class AttackReplacementController implements AttackReplacementWindow.ViewListener {

    private final Stage stage;
    private final Listener listener;

    private AttackReplacementWindow view;
    private BugemonDTO currentBugemon;
    private AbilityDTO currentNewAbility;

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

    @Override
    public void onReplaceAbility(AbilityDTO oldAbility) {
        listener.onAttackReplaced(currentBugemon, currentNewAbility, oldAbility);
    }

    @Override
    public void onReturnToChooseBugemon() {
        listener.onReturnToChooseBugemon();
    }

    public interface Listener {
        void onAttackReplaced(BugemonDTO bugemon, AbilityDTO newAbility, AbilityDTO oldAbility);
        void onReturnToChooseBugemon();
    }
}
