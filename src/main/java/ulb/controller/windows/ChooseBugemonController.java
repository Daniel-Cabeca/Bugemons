package ulb.controller.windows;

import javafx.stage.Stage;
import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.controller.ClientController;
import ulb.message.request.gameActions.ChooseStatRewardRequest;
import ulb.message.request.gameData.GetRandomAbilityRequest;
import ulb.message.response.StatusResponse;
import ulb.message.response.gameData.RandomAbilityResponse;
import ulb.view.WindowPath;
import ulb.view.windows.ChooseBugemonWindow;

import java.io.Serializable;
import java.util.logging.Level;

/**
 * Controller for selecting a bugemon for a reward action.
 */
public class ChooseBugemonController extends WindowController<ChooseBugemonWindow> implements ChooseBugemonWindow.ViewListener {
	/**
	 * Creates the choose bugemon controller.
	 *
	 * @param stage The application stage
	 * @param clientController The application controller
	 */
	public ChooseBugemonController(Stage stage, ClientController clientController) {
		super(stage, WindowPath.CHOOSE_BUGEMON, clientController);
		this.view.setViewListener(this);
	}

	/**
	 * Displays the choose bugemon screen.
	 */
	public void show() {
		PlayerDTO player = this.clientController.getPlayer();
		this.view.populatePlayerBugemons(player.getTeam());
		super.show();
	}

	public void onBugemonChosen(BugemonDTO bugemon) {
		if (this.clientController.getPendingFloorRewardChoice() == RewardChoice.STAT) {
			if (this.clientController.postData(new ChooseStatRewardRequest(bugemon))) {
				this.clientController.showWindow(WindowName.FLOOR);
			}
			return;
		}

		AbilityDTO newAbility = null;
		Serializable message = this.clientController.getData(new GetRandomAbilityRequest(bugemon));

		if (message instanceof StatusResponse errorMessage && errorMessage.isFailure()) {
			LOGGER.log(Level.WARNING, "Failed to get random ability: " + errorMessage.getMessage());
			return;

		} else if (message instanceof RandomAbilityResponse randomAbility) {
			newAbility = randomAbility.getAbility();
		}

		if (newAbility == null) {
			this.clientController.nextRoom();
			return;
		}
		this.clientController.showAttackReplacement(bugemon, newAbility);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onReturnFloorRewardWindow() { this.clientController.showWindow(WindowName.FLOOR_REWARD); }
}