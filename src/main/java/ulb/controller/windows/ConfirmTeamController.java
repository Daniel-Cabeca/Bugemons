package ulb.controller.windows;

import javafx.stage.Stage;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.controller.ClientController;
import ulb.message.request.setup.SetUpNormalModeRequest;
import ulb.message.request.setup.SetUpTowerModeRequest;
import ulb.model.GameMode;
import ulb.view.WindowPath;
import ulb.view.windows.ConfirmTeamWindow;

import java.util.List;

/**
 * Controller for the view asking to confirm the selected team before gameplay.
 */
public class ConfirmTeamController extends WindowController<ConfirmTeamWindow> implements ConfirmTeamWindow.ViewListener {
	/**
	 * The selected team of Bugemon.
	 */
	private List<BugemonDTO> playerTeam;

	/**
	 * The game mode to initiate after confirmation.
	 */
	private GameMode gameMode;

	/**
	 * Creates the team confirmation controller.
	 *
	 * @param stage The application stage
	 * @param clientController the clientController
	 */
	public ConfirmTeamController(Stage stage, ClientController clientController) {
		super(stage, WindowPath.CONFIRM_TEAM, clientController);
		this.view.setViewListener(this);
	}

	public void setGameMode(GameMode gameMode) { this.gameMode = gameMode; }

	public void setPlayerTeam(List<BugemonDTO> playerTeam) { this.playerTeam = playerTeam; }

	/**
	 * Displays the team confirmation window.
	 */
	@Override
	public void show() {
		view.setGameModeLabel(gameMode);
		view.displayTeam(playerTeam);
		super.show();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onConfirm() {
		switchToNextWindow();
	}

	/**
	 * Switch to the next window depending on the current game mode
	 */
	private void switchToNextWindow() {
		switch (this.gameMode) {
			case AUTO, CONTROLLED:
				switchToBattle();
				break;
			case TOWER:
				switchToTower();
				break;
			default:
				this.clientController.showWindow(WindowName.MAIN_MENU);
				break;
		}
	}

	/**
	 * Tell the server to setup the Battle and switch to battle window if the setup is succeded
	 */
	private void switchToBattle() {
		if (this.clientController.postData(new SetUpNormalModeRequest())) {
			this.clientController.showWindow(WindowName.BATTLE);
		}
	}

	/**
	 * Tell the server to setup the Tower and switch to floor window if the setup is succeded
	 */
	private void switchToTower() {
		if (this.clientController.postData(new SetUpTowerModeRequest(true))) {
			this.clientController.showWindow(WindowName.FLOOR);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onReturn() {
		this.clientController.showWindow(WindowName.TEAM);
	}
}
