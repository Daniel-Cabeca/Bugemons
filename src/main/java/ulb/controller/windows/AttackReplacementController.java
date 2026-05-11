package ulb.controller.windows;

import javafx.stage.Stage;
import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.controller.ClientController;
import ulb.message.request.gameActions.ChooseAbilityRewardRequest;
import ulb.view.WindowPath;
import ulb.view.windows.AttackReplacementWindow;

/**
 * Controller handling the attack replacement flow after learning a new ability.
 */
public class AttackReplacementController extends WindowController<AttackReplacementWindow> implements AttackReplacementWindow.ViewListener {
    private BugemonDTO currentBugemon;
    private AbilityDTO currentNewAbility;

    /**
     * Creates the controller with its stage and callback listener.
     *
     * @param stage          The application stage
     * @param clientController The client controller
     */
    public AttackReplacementController(Stage stage, ClientController clientController) {
        super(stage, WindowPath.ATTACK_REPLACEMENT, clientController);
        this.view.setViewListener(this);
    }

    /**
     * Displays the attack replacement screen for the given bugemon and new ability.
     *
     * @param bugemon    The bugemon learning a new ability
     * @param newAbility The new ability to be learned
     */
    public void show(BugemonDTO bugemon, AbilityDTO newAbility) {
        this.currentBugemon = bugemon;
        this.currentNewAbility = newAbility;
        this.view.initializeReplacement(bugemon, newAbility);
        super.show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onReplaceAbility(AbilityDTO oldAbility) {
        if (this.clientListener.onPostData(new ChooseAbilityRewardRequest(this.currentBugemon, oldAbility, this.currentNewAbility))) {
            this.clientListener.onShowWindow(WindowName.FLOOR);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onReturnToChooseBugemon() { this.clientListener.onShowWindow(WindowName.CHOOSE_BUGEMON); }
}