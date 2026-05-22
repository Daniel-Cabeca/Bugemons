package ulb.controller.windows.Battle;

import java.util.Optional;

import javafx.stage.Stage;
import ulb.controller.ClientController;
import ulb.controller.windows.WindowController;
import ulb.view.WindowPath;
import ulb.view.windows.BattleEndWindow;

/**
 * Controller for the battle end summary screen.
 */
public class BattleEndController extends WindowController<BattleEndWindow> implements BattleEndWindow.ViewListener {

	/**
	 * Creates the battle end controller.
	 *
	 * @param stage The application stage
	 * @param clientController The client controller
	 */
	public BattleEndController(Stage stage, ClientController clientController) {
		super(stage, WindowPath.BATTLE_END, clientController);
		this.view.setViewListener(this);
	}

	/**
	 * Displays battle result information.
	 *
	 * @param victory Whether the battle was won
	 * @param totalXP Total experience gained
	 * @param opponent the one player fought
	 */
	public void show(boolean victory, int totalXP, Optional<String> opponent) {
		super.show();
		this.view.setResult(victory, totalXP, opponent);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onHandleReturn() { this.clientController.showWindow(WindowName.MAIN_MENU); }

}
