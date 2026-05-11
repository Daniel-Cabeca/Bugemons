package ulb.controller.windows.Battle;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.item.ItemDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.communication.GameMode;
import ulb.controller.ClientController;
import ulb.message.request.gameActions.ChooseRandomActionRequest;
import ulb.message.request.gameActions.RunRequest;
import ulb.message.request.gameActions.SwapBugemonRequest;
import ulb.message.request.gameActions.UseAbilityRequest;
import ulb.message.request.gameActions.UseItemRequest;
import ulb.message.request.gameInfo.CheckGameFinishedRequest;
import ulb.message.request.gameInfo.GetLogsRequest;
import ulb.message.response.StatusResponse;
import ulb.message.response.gameInfo.GameFinishedResponse;
import ulb.message.response.gameInfo.LogsResponse;
import ulb.model.battle.BattleState;
import ulb.view.windows.BattleWindow;
import ulb.view.windows.BattleWindow.BattleSnapshot;

public class BattleActionController {
	private final ClientController clientController;
	private final BattleSetupController battleSetupController;
	private BattleWindow view;
	protected final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	private boolean waitingForOpponentAction = false;

	BattleActionController(ClientController clientController, BattleSetupController battleSetupController){
		this.clientController = clientController;
		this.battleSetupController = battleSetupController;
	}
 
	public void setView(BattleWindow view) { this.view = view; }

	/**
     * Finds an inventory item by its id.
     *
     * @param itemId The id of the item to find
     * @return The matching item DTO, or null if not found
     */
    private ItemDTO findInventoryItemById(String itemId, PlayerDTO player) {
        if (itemId == null) {
            return null;
        }

        Map<ItemDTO, Integer> inventory = player.getInventory();
        if (inventory == null) {
            return null;
        }

        for (ItemDTO item : inventory.keySet()) {
            if (itemId.equals(item.id())) {
                return item;
            }
        }
        return null;
    }

    /**
     * Finds a Bugemon by its id.
     *
     * @param bugemonId The id of the Bugemon to find
     * @return The matching Bugemon DTO, or null if not found
     */
    private BugemonDTO findTeamBugemonById(String bugemonId, PlayerDTO player) {
        if (bugemonId == null) {
            return null;
        }

        List<BugemonDTO> team = player.getTeam();
        if (team == null) {
            return null;
        }

        for (BugemonDTO bugemon : team) {
            if (bugemonId.equals(bugemon.getId())) {
                return bugemon;
            }
        }
        return null;
    }

	/**
     * Finds the active Bugemon's ability by its id.
     *
     * @param abilityId The id of the ability to find
     * @return The matching ability DTO, or null if not found
     */
    private AbilityDTO findActiveAbilityById(String abilityId) {
        BugemonDTO activeBugemon = this.battleSetupController.getActiveBugemons().get(0);
        if (activeBugemon == null){
            return null;
        }
		return activeBugemon.findActiveAbilityById(abilityId);
    }

	public boolean isGameFinished(){
		Serializable message = this.clientController.getData(new CheckGameFinishedRequest());
		if (message instanceof GameFinishedResponse gameFinished){
			return gameFinished.isGameFinished();
		} else if (message instanceof StatusResponse errorMessage && errorMessage.isFailure()){
			LOGGER.log(Level.WARNING, "Failed to check if game is finished: " + errorMessage.getMessage());
		}
		return true;
	}

	public List<Integer> getHpAfterFirstAction(){
		Serializable message = this.clientController.getData(new GetLogsRequest(false));
		if (message instanceof LogsResponse logs){
			return logs.getHpsAfterFirstAction();
		} else if (message instanceof StatusResponse errorMessage && errorMessage.isFailure()){
			LOGGER.log(Level.WARNING, "Failed to get HP after first action: " + errorMessage.getMessage());
		}
		return null;
	}

	/**
     * Waits asynchronously for the opponent action to complete.
     *
     * @param event The action event tied to the current interaction
     */
    private void waitingForOpponentAction(PlayerDTO player, ActionEvent event) {
        if (waitingForOpponentAction) {
            return;
        }

        waitingForOpponentAction = true;
        view.setBattleInputsDisabled(true);

        Thread waitingThread = new Thread(() -> {
            try {
                while (this.battleSetupController.getState() == BattleState.WAITING && !this.isGameFinished()) {
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            Platform.runLater(() -> {
                waitingForOpponentAction = false;
                view.setBattleInputsDisabled(false);

                BattleState currentState = this.battleSetupController.getState();
                displayActionSequence(currentState, player, event, () -> {
                    if (currentState == BattleState.INGAME) {
                        view.showMainMenu();
                    }
                });
            });
        });

        waitingThread.setDaemon(true);
        waitingThread.start();
    }

	/**
     * Displays action logs sequentially, then handles resulting state changes.
     *
     * @param stateAfter The battle state after the selected action
     * @param event The triggering action event
     * @param afterDisplay Callback executed when no state-specific transition occurs
     */
    private void displayActionSequence(BattleState stateAfter, PlayerDTO player, ActionEvent event, Runnable afterDisplay) {
        List<String> logs = this.battleSetupController.consumeLogMessages();

        List<Integer> hpAfterFirstAction = this.getHpAfterFirstAction();
        Integer firstActionSelfHp = null, firstActionOpponentHp = null;

        if (logs.contains(null) && hpAfterFirstAction.get(0) != null && hpAfterFirstAction.get(1) != null){
            firstActionSelfHp = hpAfterFirstAction.get(0);
            firstActionOpponentHp = hpAfterFirstAction.get(1);
        }

        BattleSnapshot finalSnapshot = this.battleSetupController.buildBattleSnapshot();

        view.displayMessagesSequentially(logs, firstActionSelfHp, firstActionOpponentHp, finalSnapshot, () -> {
            this.battleSetupController.refreshView();
            boolean stateHandled = this.battleSetupController.handleBattleState(stateAfter, player, event);
            if (!stateHandled) {
                afterDisplay.run();
            }
            if (stateAfter == BattleState.WAITING) {
                waitingForOpponentAction(player, event);
            }
        });
    }

	/**
     * Returns the provided state or the current controller state when null.
     *
     * @param state The candidate state
     * @return The resolved battle state
     */
    private BattleState stateOrCurrent(BattleState state) {
        if (state != null) {
            return state;
        }
        return this.battleSetupController.getState();
    }

	public void useItem(String itemId, PlayerDTO player, ActionEvent event){
		ItemDTO item = findInventoryItemById(itemId, player);

		if (!this.clientController.postData(new UseItemRequest(item))){
			return;
		}

        BattleState stateAfter = stateOrCurrent(this.battleSetupController.getState());

        displayActionSequence(stateAfter, player, event, () -> {
            if (stateAfter == BattleState.WAITING || stateAfter == BattleState.INGAME) {
                view.showMainMenu();
            } else {
                view.showInventoryMenu(this.battleSetupController.buildInventoryEntries(player));
            }
        });
	}

	public void useAbility(String abilityId, PlayerDTO player, ActionEvent event){
		AbilityDTO ability = findActiveAbilityById(abilityId);

		if (!this.clientController.postData(new UseAbilityRequest(ability))){
			return;
		}

        BattleState stateAfter = stateOrCurrent(this.battleSetupController.getState());

        displayActionSequence(stateAfter, player, event, () -> {
            if (stateAfter == BattleState.WAITING || stateAfter == BattleState.INGAME) {
                view.showMainMenu();
            }
        });
	}

	public void swapBugemon(String bugemonId, PlayerDTO player, GameMode gameMode, ActionEvent event){
		BugemonDTO bugemon = findTeamBugemonById(bugemonId, player);
        BattleSnapshot snapshotBeforeAction = this.battleSetupController.buildBattleSnapshot();
        if (snapshotBeforeAction != null) {
            view.setCurrentSnapshot(snapshotBeforeAction);
        }

		if (!this.clientController.postData(new SwapBugemonRequest(bugemon))){
			return;
		}

        BattleState stateAfter = stateOrCurrent(this.battleSetupController.getState());

        displayActionSequence(stateAfter, player, event, () -> {
            if (stateAfter == BattleState.WAITING || stateAfter == BattleState.INGAME) {
                view.showMainMenu();
                // reset the auto button after forced swap
                if (gameMode == GameMode.AUTO) {
                    view.setAutoButtonVisible(true);
                }
            } else {
                view.showBugemonsMenu(this.battleSetupController.buildBugemonEntries(player));
            }
        });
	}

	public void autoAction(PlayerDTO player, ActionEvent event){
        view.setAutoButtonVisible(false);

		if (!this.clientController.postData(new ChooseRandomActionRequest())){
			return;
		}

        BattleState stateAfter = stateOrCurrent(this.battleSetupController.getState());

        displayActionSequence(stateAfter, player, event, () -> {
            view.showMainMenu();
            view.setAutoButtonVisible(true);
        });
	}

	public void run(){
		if (this.clientController.postData(new RunRequest())){
			this.clientController.nextRoom();
		}
	}
}
