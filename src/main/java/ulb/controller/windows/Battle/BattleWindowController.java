package ulb.controller.windows.Battle;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import ulb.DTO.player.PlayerDTO;
import ulb.communication.GameMode;
import ulb.controller.ClientController;
import ulb.controller.windows.WindowController;
import ulb.message.request.gameInfo.GetTowerInfoRequest;
import ulb.message.response.gameInfo.TowerInfoResponse;
import ulb.view.WindowPath;
import ulb.view.windows.BattleWindow;

public class BattleWindowController extends WindowController<BattleWindow> implements BattleWindow.ViewListener {
	private final BattleSetupController battleSetupController;
	private final BattleActionController battleActionController;
	
	private PlayerDTO player;
    private GameMode gameMode;
    private int towerFloorNumber;
    private int towerRoomNumber;
	
	/**
     * Creates the floor reward controller.
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

	public void show(){
		setTowerInfo();

		view.initializeContent();
		view.initializeView(gameMode == GameMode.AUTO);

		this.battleSetupController.setView(this.view);
		this.battleActionController.setView(this.view);
        this.battleSetupController.refreshView();
        this.battleSetupController.updateTowerInfo(gameMode, towerFloorNumber, towerRoomNumber);

		super.show();
	}

	public void setTowerInfo(){
		if (this.clientListener.onGetData(new GetTowerInfoRequest()) instanceof TowerInfoResponse towerInfo){
			this.towerFloorNumber = towerInfo.getFloorNumber();
			this.towerRoomNumber = towerInfo.getRoomNumber();
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
        this.clientListener.onShowWindow(WindowName.SOCIAL_PANEL);
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

	/**
     * {@inheritDoc}
     */
    @Override
    public void onAuto(ActionEvent event) {
		this.battleActionController.autoAction(player, event);
	}
}
