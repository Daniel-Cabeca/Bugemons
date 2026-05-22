package ulb.controller.windows.Battle;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import ulb.DTO.player.PlayerDTO;
import ulb.controller.ClientController;
import ulb.controller.windows.WindowController;
import ulb.exceptions.UnknownServerResponse;
import ulb.message.request.gameInfo.GetTowerInfoRequest;
import ulb.message.response.gameInfo.TowerInfoResponse;
import ulb.model.GameMode;
import ulb.view.WindowPath;
import ulb.view.windows.BattleWindow;

/**
 * Main controller for ongoing battles.
 */
public class BattleWindowController extends WindowController<BattleWindow> implements BattleWindow.ViewListener {
	/**
	 * Battle controller responsible for fetching information.
	 */
	private final BattleSetupController battleSetupController;

	/**
	 * Battle controller handling turn actions.
	 */
	private final BattleActionController battleActionController;

	/**
	 * The current player.
	 */
	private PlayerDTO player;

	/**
	 * The current game mode.
	 */
	private GameMode gameMode;

	/**
	 * The current tower floor number.
	 */
	private int towerFloorNumber;

	/**
	 * The current room number for the current floor.
	 */
	private int towerRoomNumber;

	/**
	 * Creates the battle controller.
	 *
	 * @param stage The application stage
	 * @param clientController The client controller
	 */
	public BattleWindowController(Stage stage, ClientController clientController) {
		super(stage, WindowPath.BATTLE, clientController);
		this.view.setViewListener(this);
		battleSetupController = new BattleSetupController(clientController);
		battleActionController = new BattleActionController(clientController, battleSetupController);
	}

	/**
	 * Display the associated view.
	 */
	@Override
	public void show() {
		loadTowerInfoIfNeeded();

		view.initializeContent();
		view.initializeView(gameMode == GameMode.AUTO);

		this.battleSetupController.setView(this.view);
		this.battleActionController.setView(this.view);
		this.battleSetupController.refreshView();
		this.battleSetupController.updateTowerInfo(gameMode, towerFloorNumber, towerRoomNumber);

		super.show();
	}

	/**
	 * Loads information pertaining to tower mode.
	 * (floor and room numbers)
	 */
	private void loadTowerInfoIfNeeded() {
		if (this.gameMode != GameMode.TOWER) {
			this.towerFloorNumber = 0;
			this.towerRoomNumber = 0;
			return;
		}

		try {
			if (this.clientController.getData(new GetTowerInfoRequest()) instanceof TowerInfoResponse towerInfo) {
				this.towerFloorNumber = towerInfo.getFloorNumber();
				this.towerRoomNumber = towerInfo.getRoomNumber();
			}
			throw new UnknownServerResponse("getTowerInfoRequest");
		} catch (Exception e) {
			LOGGER.warning("Impossible de récupérer les informations de la tour.");
		}
		
	}

	public void setPlayer(PlayerDTO player) { this.player = player; }

	public void setGameMode(GameMode gameMode) { this.gameMode = gameMode; }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onItemMenu() {
		this.view.showInventoryMenu(this.battleSetupController.buildInventoryEntries(player));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onBugemonsMenu() {
		this.view.showBugemonsMenu(this.battleSetupController.buildBugemonEntries(player));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onAuto(ActionEvent event) {
		this.battleActionController.autoAction(player, event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onAttack() {
		this.view.showAbilitiesMenu(this.battleSetupController.buildAbilityEntries());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onBackToMenu() {
		this.view.showMainMenu();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onReturn(ActionEvent event) {
		this.battleActionController.run();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onOpenSocial() {
		this.clientController.showWindow(WindowName.SOCIAL_PANEL);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onUseItem(String itemId, ActionEvent event) {
		this.battleActionController.useItem(itemId, player, event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSwapBugemon(String bugemonId, ActionEvent event) {
		this.battleActionController.swapBugemon(bugemonId, player, gameMode, event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onUseAbility(String abilityId, ActionEvent event) {
		this.battleActionController.useAbility(abilityId, player, event);
	}
}
