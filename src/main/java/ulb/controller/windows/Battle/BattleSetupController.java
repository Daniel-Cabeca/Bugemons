package ulb.controller.windows.Battle;

import java.io.Serializable;
import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.item.ItemDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.communication.GameMode;
import ulb.controller.windows.WindowController.ClientListener;
import ulb.message.clientToServer.gameInfo.CheckUsableItemMessage;
import ulb.message.clientToServer.gameInfo.GetAbilityEffectivenessMessage;
import ulb.message.clientToServer.gameInfo.GetActiveBugemonsMessage;
import ulb.message.clientToServer.gameInfo.GetBattleStateMessage;
import ulb.message.clientToServer.gameInfo.GetLogsMessage;
import ulb.message.clientToServer.playerInfo.GetPlayerInventoryMessage;
import ulb.message.serverToClient.StatusMessage;
import ulb.message.serverToClient.gameInfo.AbilityEffectivenessMessage;
import ulb.message.serverToClient.gameInfo.ActiveBugemonsMessage;
import ulb.message.serverToClient.gameInfo.BattleStateMessage;
import ulb.message.serverToClient.gameInfo.LogsMessage;
import ulb.message.serverToClient.gameInfo.UsableItemsMessage;
import ulb.message.serverToClient.playerInfo.PlayerInventoryMessage;
import ulb.model.battle.BattleState;
import ulb.view.windows.BattleWindow;
import ulb.view.windows.BattleWindow.AbilityEntry;
import ulb.view.windows.BattleWindow.BattleSnapshot;
import ulb.view.windows.BattleWindow.BugemonDisplay;
import ulb.view.windows.BattleWindow.BugemonEntry;
import ulb.view.windows.BattleWindow.InventoryEntry;

public class BattleSetupController {
	private final ClientListener clientListener;
	private BattleWindow view;
	protected final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	BattleSetupController(ClientListener clientListener){
		this.clientListener = clientListener;
	}

	public void setView(BattleWindow view) { this.view = view; }

	public void updateInventory(PlayerDTO player){
		Serializable message = this.clientListener.onGetData(new GetPlayerInventoryMessage(player.getUsername()));
		if (message instanceof PlayerInventoryMessage playerInventory){
			player.setInventory(playerInventory.getInventory());
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			LOGGER.log(Level.WARNING, "Failed to update player inventory: " + errorMessage.getMessage());
		}
	}

	public Map<String, Boolean> checkItems(List<ItemDTO> items){
		Serializable message = this.clientListener.onGetData(new CheckUsableItemMessage(items));
		if (message instanceof UsableItemsMessage usableItems){
			return usableItems.getItemMap();
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			LOGGER.log(Level.WARNING, "Failed to check usable items: " + errorMessage.getMessage());
		}
		return null;
	}

	public List<BugemonDTO> getActiveBugemons(){
		Serializable message = clientListener.onGetData(new GetActiveBugemonsMessage());
		if (message instanceof ActiveBugemonsMessage activeBugemons){
			return List.of(activeBugemons.getSelfActiveBugemon(), activeBugemons.getOpponentActiveBugemon());
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			LOGGER.log(Level.WARNING, "Failed to get active bugemons: " + errorMessage.getMessage());
		}
		return null;
	}

	public BattleState getState(){
		Serializable message = this.clientListener.onGetData(new GetBattleStateMessage());
		if (message instanceof BattleStateMessage battleState){
			return battleState.getBattleState();
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			LOGGER.log(Level.WARNING, "Failed to get battle state: " + errorMessage.getMessage());
		}
		return null;
	}

	public Map<AbilityDTO, String> getAbilityEffectiveness(List<AbilityDTO> abilities, BugemonDTO bugemonTarget){
		Serializable message = this.clientListener.onGetData(new GetAbilityEffectivenessMessage(abilities, bugemonTarget));
		if (message instanceof AbilityEffectivenessMessage effectivenessMessage){
			return effectivenessMessage.getEffectiveness();
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
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
    public BattleSnapshot buildBattleSnapshot() {
        List<BugemonDTO> activeBugemons = this.getActiveBugemons();
        BugemonDTO playerBugemon = activeBugemons.get(0);
        BugemonDTO opponentBugemon = activeBugemons.get(1);
        if (playerBugemon == null || opponentBugemon == null) {
            return null;
        }

        return new BattleSnapshot(toBugemonDisplay(playerBugemon), toBugemonDisplay(opponentBugemon));
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
			this.clientListener.onNextRoom();
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
        Serializable message = this.clientListener.onGetData(new GetLogsMessage(true));
		if (message instanceof LogsMessage logs){
			return logs.getLogs();
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
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

        view.setForcedSwitch(isForcedSwitch());
        BattleSnapshot snapshot = buildBattleSnapshot();
        if (snapshot != null) {
            view.renderBattle(snapshot);
        }
        view.showLogMessages(consumeLogMessages());
    }

    /**
     * Updates tower information banner when in tower mode.
     */
    public void updateTowerInfo(GameMode gameMode, int towerFloorNumber, int towerRoomNumber) {
        if (gameMode == GameMode.TOWER) {
            view.setTowerInfo(towerFloorNumber, towerRoomNumber);
        } else {
            view.clearTowerInfo();
        }
    }
}
