package ulb.controller.windows.Battle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.item.ItemDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.communication.GameMode;
import ulb.controller.ClientController;
import ulb.message.request.gameInfo.CheckUsableItemRequest;
import ulb.message.request.gameInfo.GetAbilityEffectivenessRequest;
import ulb.message.request.gameInfo.GetActiveBugemonsRequest;
import ulb.message.request.gameInfo.GetBattleStateRequest;
import ulb.message.request.gameInfo.GetLogsRequest;
import ulb.message.request.playerInfo.GetPlayerInventoryRequest;
import ulb.message.response.StatusResponse;
import ulb.message.response.gameInfo.AbilityEffectivenessResponse;
import ulb.message.response.gameInfo.ActiveBugemonsResponse;
import ulb.message.response.gameInfo.BattleStateResponse;
import ulb.message.response.gameInfo.LogsResponse;
import ulb.message.response.gameInfo.UsableItemsResponse;
import ulb.message.response.playerInfo.PlayerInventoryResponse;
import ulb.model.battle.BattleState;
import ulb.view.windows.BattleWindow;
import ulb.view.windows.BattleWindow.AbilityEntry;
import ulb.view.windows.BattleWindow.BattleSnapshot;
import ulb.view.windows.BattleWindow.BugemonDisplay;
import ulb.view.windows.BattleWindow.BugemonEntry;
import ulb.view.windows.BattleWindow.InventoryEntry;

public class BattleSetupController {
	private final ClientController clientController;
	private BattleWindow view;
	protected final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	BattleSetupController(ClientController clientController){
		this.clientController = clientController;
	}

	public void setView(BattleWindow view) { this.view = view; }

	public void updateInventory(PlayerDTO player){
		Serializable message = this.clientController.getData(new GetPlayerInventoryRequest(player.getUsername()));
		if (message instanceof PlayerInventoryResponse playerInventory){
			player.setInventory(playerInventory.getInventory());
		} else if (message instanceof StatusResponse errorMessage && errorMessage.isFailure()){
			LOGGER.log(Level.WARNING, "Failed to update player inventory: " + errorMessage.getMessage());
		}
	}

	public Map<String, Boolean> checkItems(List<ItemDTO> items){
		Serializable message = this.clientController.getData(new CheckUsableItemRequest(items));
		if (message instanceof UsableItemsResponse usableItems){
			return usableItems.getItemMap();
		} else if (message instanceof StatusResponse errorMessage && errorMessage.isFailure()){
			LOGGER.log(Level.WARNING, "Failed to check usable items: " + errorMessage.getMessage());
		}
		return null;
	}

	public List<BugemonDTO> getActiveBugemons(){
		Serializable message = clientController.getData(new GetActiveBugemonsRequest());
		if (message instanceof ActiveBugemonsResponse activeBugemons){
			return List.of(activeBugemons.getSelfActiveBugemon(), activeBugemons.getOpponentActiveBugemon());
		} else if (message instanceof StatusResponse errorMessage && errorMessage.isFailure()){
			LOGGER.log(Level.WARNING, "Failed to get active bugemons: " + errorMessage.getMessage());
		}
		return null;
	}

	public BattleState getState(){
		Serializable message = this.clientController.getData(new GetBattleStateRequest());
		if (message instanceof BattleStateResponse battleState){
			return battleState.getBattleState();
		} else if (message instanceof StatusResponse errorMessage && errorMessage.isFailure()){
			LOGGER.log(Level.WARNING, "Failed to get battle state: " + errorMessage.getMessage());
		}
		return null;
	}

	public Map<AbilityDTO, String> getAbilityEffectiveness(List<AbilityDTO> abilities, BugemonDTO bugemonTarget){
		Serializable message = this.clientController.getData(new GetAbilityEffectivenessRequest(abilities, bugemonTarget));
		if (message instanceof AbilityEffectivenessResponse effectivenessMessage){
			return effectivenessMessage.getEffectiveness();
		} else if (message instanceof StatusResponse errorMessage && errorMessage.isFailure()){
			LOGGER.log(Level.WARNING, "Failed to get ability effectiveness: " + errorMessage.getMessage());
		}
		return null;
	}

	/**
     * Builds inventory entries for the inventory menu.
     *
     * @return The list of inventory entries
     */
    public List<InventoryEntry> buildInventoryEntries(PlayerDTO player) {
        this.updateInventory(player);
        Map<ItemDTO, Integer> inventory = player.getInventory();
        if (inventory == null) {
            return List.of();
        }

        List<InventoryEntry> entries = new ArrayList<>();
        Map<String, Boolean> itemMap = this.checkItems(new ArrayList<ItemDTO>(inventory.keySet()));

        for (Map.Entry<ItemDTO, Integer> entry : inventory.entrySet()) {
            ItemDTO item = entry.getKey();
            boolean usable = itemMap.get(item.id());
            entries.add(new InventoryEntry(
                    item.id(),
                    item.name(),
                    item.description(),
                    item.sprite(),
                    entry.getValue(),
                    usable
            ));
        }
        return entries;
    }

    /**
     * Builds bugemon entries for the bugemon selection menu.
     *
     * @return The list of bugemon entries
     */
    public List<BugemonEntry> buildBugemonEntries(PlayerDTO player) {
        List<BugemonDTO> playerTeam = player.getTeam();
        if (playerTeam == null) {
            return List.of();
        }

        BugemonDTO activeBugemon = this.getActiveBugemons().get(0);
        List<BugemonEntry> entries = new ArrayList<>();
        for (BugemonDTO bugemon : playerTeam) {
            boolean active = bugemon.getId().equals(activeBugemon.getId());
            boolean selectable = !active && bugemon.getHp() > 0;
            entries.add(new BugemonEntry(
                    bugemon.getId(),
                    bugemon.getName(),
                    bugemon.getSpritePath(),
                    bugemon.getHp() <= 0,
                    active,
                    selectable
            ));
        }
        return entries;
    }

    /**
     * Builds ability entries for the abilities menu.
     *
     * @return The list of ability entries
     */
    public List<AbilityEntry> buildAbilityEntries() {
        List<BugemonDTO> activeBugemons = this.getActiveBugemons();
        BugemonDTO ownActiveBugemon = activeBugemons.get(0);
        BugemonDTO opponentActiveBugemon = activeBugemons.get(1);
        if (ownActiveBugemon == null){
            return List.of();
        }

        List<AbilityEntry> entries = new ArrayList<>();
        Map<AbilityDTO, String> abilitiesEffectiveness = this.getAbilityEffectiveness(ownActiveBugemon.getAbilities(), opponentActiveBugemon);

        for (AbilityDTO ability : ownActiveBugemon.getAbilities()) {
            if (ability != null) {
                // Cannot use map.get(ability) because DTO instances are different after serialization.
                // the abilities are matched using their ID instead of object reference
                String effectiveness = null;
                if (abilitiesEffectiveness != null) {
                    for (Map.Entry<AbilityDTO, String> entry : abilitiesEffectiveness.entrySet()) {
                        if (entry.getKey() != null && entry.getKey().id().equals(ability.id())) {
                            effectiveness = entry.getValue();
                            break;
                        }
                    }
                }

                entries.add(new AbilityEntry(
                        ability.id(),
                        ability.name(),
                        ability.description() + "\n Puissance:  " + ability.power(),
                        ability.type(),
                        effectiveness
                ));
            }
        }
        return entries;
    }

	/**
     * Converts a bugemon DTO into a display model for the battle view.
     *
     * @param bugemon The bugemon to convert
     * @return The corresponding DTO
     */
    private BugemonDisplay toBugemonDisplay(BugemonDTO bugemon) {
        return new BugemonDisplay(
                bugemon.getName(),
                bugemon.getSpritePath(),
                bugemon.getType(),
                bugemon.getLevel(),
                bugemon.getHp(),
                bugemon.getBaseStats().hp()
        );
    }

	/**
     * Builds a snapshot model for rendering the battle view.
     *
     * @return The battle snapshot, or null if unavailable
     */
    public Optional<BattleSnapshot> buildBattleSnapshot() {
        List<BugemonDTO> activeBugemons = this.getActiveBugemons();
        BugemonDTO playerBugemon = activeBugemons.get(0);
        BugemonDTO opponentBugemon = activeBugemons.get(1);
        
		if (playerBugemon == null || opponentBugemon == null) {
            return Optional.empty();
        }
		BattleSnapshot snapshot = new BattleSnapshot(toBugemonDisplay(playerBugemon), toBugemonDisplay(opponentBugemon));
        return Optional.of(snapshot);
    }

	/**
     * Applies UI transitions for a given battle state.
     *
     * @param state The battle state to handle
     * @param event The triggering action event
     * @return True if the state transition was handled
     */
    public boolean handleBattleState(BattleState state, PlayerDTO player, ActionEvent event) {
		if (state == BattleState.WON || state == BattleState.LOST){
			this.clientController.nextRoom();
		}
        view.setForcedSwitch(state == BattleState.SWAPPING);
        if (state == BattleState.SWAPPING) {
            view.showMainMenu();
            view.showBugemonsMenu(buildBugemonEntries(player));
            return true;
        }
        return state == BattleState.WON || state == BattleState.LOST;
    }

	/**
     * Retrieves and clears accumulated battle log messages.
     *
     * @return The current list of log messages
     */
    public List<String> consumeLogMessages() {
        Serializable message = this.clientController.getData(new GetLogsRequest(true));
		if (message instanceof LogsResponse logs){
			return logs.getLogs();
		} else if (message instanceof StatusResponse errorMessage && errorMessage.isFailure()){
			LOGGER.log(Level.WARNING, "Failed to get logs: " + errorMessage.getMessage());
		}
		return null;
    }

	/**
     * Indicates whether the player is currently forced to switch bugemon.
     *
     * @return True if switch is mandatory
     */
    private boolean isForcedSwitch() {
        return this.getState() == BattleState.SWAPPING;
    }

	/**
     * Refreshes battle UI content from current battle state.
     */
    public void refreshView() {
        if (view == null) {
            return;
        }
        
        boolean isForcedToSwitch = this.isForcedSwitch();
        if(isForcedToSwitch){
            clientController.updateTeam();
        }
        view.setForcedSwitch(isForcedToSwitch);
        Optional<BattleSnapshot> snapshot = buildBattleSnapshot();
        
		if (snapshot.isPresent()) {
            view.renderBattle(snapshot.get());
        }
        view.showLogMessages(consumeLogMessages());
    }

    /**
     * Updates tower information banner when in tower mode.
     */
    public void updateTowerInfo(GameMode gameMode, int towerFloorNumber, int towerRoomNumber) {
		if (view == null) {
            return;
        }
		
		if (gameMode == GameMode.TOWER) {
            view.setTowerInfo(towerFloorNumber, towerRoomNumber);
        } else {
            view.clearTowerInfo();
        }
    }
}
