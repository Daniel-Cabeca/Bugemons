package ulb.controller.windows.Battle;

import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.item.ItemDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.controller.ClientController;
import ulb.exceptions.ServerStatusException;
import ulb.exceptions.UnknownServerResponse;
import ulb.message.request.gameInfo.*;
import ulb.message.request.playerInfo.GetPlayerInventoryRequest;
import ulb.message.request.playerInfo.GetPlayerTeamRequest;
import ulb.message.response.gameInfo.*;
import ulb.message.response.playerInfo.PlayerInventoryResponse;
import ulb.message.response.playerInfo.PlayerTeamResponse;
import ulb.model.GameMode;
import ulb.model.battle.BattleState;
import ulb.view.windows.BattleWindow;
import ulb.view.windows.BattleWindow.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Battle controller responsible for fetching information.
 */
public class BattleSetupController {
	/**
	 * Object used for logging runtime information to the console or to a log file.
	 */
	protected final Logger LOGGER = Logger.getLogger(this.getClass().getName());

	/**
	 * The controller responsible for coordinating server communications and switching to other controllers.
	 */
	private final ClientController clientController;

	/**
	 * The associated view.
	 */
	private BattleWindow view;

	BattleSetupController(ClientController clientController) {
		this.clientController = clientController;
	}

	public void setView(BattleWindow view) { this.view = view; }

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
		
		Map<String, Boolean> itemMap;
		try {
			itemMap = this.checkItems(new ArrayList<ItemDTO>(inventory.keySet()));
		} catch (Exception e) {
			LOGGER.warning("Impossible de vérifier si un objet est utilisable.");
			return List.of();
		}

		for (Map.Entry<ItemDTO, Integer> entry : inventory.entrySet()) {
			ItemDTO item = entry.getKey();
			boolean usable = itemMap.get(item.id());
			entries.add(new InventoryEntry(item.id(), item.name(), item.description(), item.sprite(), entry.getValue()
					, usable));
		}
		return entries;
	}

	/**
	 * Refreshes the list of item in the current player's inventory.
	 *
	 * @param player The current player
	 */
	public void updateInventory(PlayerDTO player) {
		try {
			if (this.clientController.getData(new GetPlayerInventoryRequest(player.getUsername())) instanceof PlayerInventoryResponse playerInventory) {
				player.setInventory(playerInventory.getInventory());
			}
			throw new UnknownServerResponse("getPlayerInventory");
		} catch (Exception e) {
			LOGGER.warning("Impossible de mettre à jour l'inventaire du joueur.");
		}
	}

	/**
	 * Tests whether given items can be used for the active Bugemon.
	 *
	 * @param items The list of items to test
	 * @return A map associating item ids and a boolean set to true if the item is usable on the current player's active Bugemon, false otherwise
	 */
	public Map<String, Boolean> checkItems(List<ItemDTO> items) throws ServerStatusException, UnknownServerResponse {
		if (this.clientController.getData(new CheckUsableItemRequest(items)) instanceof UsableItemsResponse usableItems) {
			return usableItems.getItemMap();
		} 
		throw new UnknownServerResponse("checkUsableItem");
	}

	/**
	 * Builds ability entries for the abilities menu.
	 *
	 * @return The list of ability entries
	 */
	public List<AbilityEntry> buildAbilityEntries() {
		List<BugemonDTO> activeBugemons;
		try {
			activeBugemons = this.getActiveBugemons();
		} catch (Exception e) {
			LOGGER.warning("Impossible de récupérer les bugemons actifs");
			return List.of();
		}
		BugemonDTO ownActiveBugemon = activeBugemons.get(0);
		BugemonDTO opponentActiveBugemon = activeBugemons.get(1);
		if (ownActiveBugemon == null) {
			return List.of();
		}

		List<AbilityEntry> entries = new ArrayList<>();
		Map<AbilityDTO, String> abilitiesEffectiveness;
		try {
			abilitiesEffectiveness = this.getAbilityEffectiveness(ownActiveBugemon.getAbilities(),
					opponentActiveBugemon);
		} catch (Exception e) {
			LOGGER.warning("Impossible de récupérer l'efficacité des compétences.");
			return List.of();
		}

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

				entries.add(new AbilityEntry(ability.id(), ability.name(),
						ability.description() + "\n Puissance:  " + ability.power(), ability.type(), effectiveness));
			}
		}
		return entries;
	}

	/**
	 * Fetches the active Bugemon of each player.
	 *
	 * @return A list holding two Bugemon; in order: the current player's active Bugemon and his opponent's active Bugemon
	 */
	public List<BugemonDTO> getActiveBugemons() throws ServerStatusException, UnknownServerResponse {
		if (clientController.getData(new GetActiveBugemonsRequest()) instanceof ActiveBugemonsResponse activeBugemons) {
			return List.of(activeBugemons.getSelfActiveBugemon(), activeBugemons.getOpponentActiveBugemon());
		} 
		throw new UnknownServerResponse("getActiveBugemons");
	}

	/**
	 * Fetches the type effectiveness of given abilities against a given Bugemon.
	 *
	 * @param abilities The list of abilities whose type effectiveness to test
	 * @param bugemonTarget The Bugemon target to test type effectiveness against
	 * @return A map associating each ability to a message to display to inform the player of its effectiveness
	 */
	public Map<AbilityDTO, String> getAbilityEffectiveness(List<AbilityDTO> abilities, BugemonDTO bugemonTarget) throws ServerStatusException, UnknownServerResponse {
		Serializable message = this.clientController.getData(new GetAbilityEffectivenessRequest(abilities,
				bugemonTarget));
		if (message instanceof AbilityEffectivenessResponse effectivenessMessage) {
			return effectivenessMessage.getEffectiveness();
		} 
		throw new UnknownServerResponse("getAbilityEffectiveness");
	}

	/**
	 * Applies UI transitions for a given battle state.
	 *
	 * @param state The battle state to handle
	 * @return True if the state transition was handled
	 */
	public boolean handleBattleState(BattleState state, PlayerDTO player) {
		if (state == BattleState.WON || state == BattleState.LOST) {
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
	 * Builds bugemon entries for the bugemon selection menu.
	 *
	 * @return The list of bugemon entries
	 */
	public List<BugemonEntry> buildBugemonEntries(PlayerDTO player) {
		this.updateTeam(player);
		List<BugemonDTO> playerTeam = player.getTeam();
		if (playerTeam == null) {
			return List.of();
		}

		BugemonDTO activeBugemon;	
		try {
			activeBugemon = this.getActiveBugemons().get(0);
		} catch (Exception e) {
			LOGGER.warning("Impossible de récupérer les bugemons actifs");
			return List.of();
		}

		List<BugemonEntry> entries = new ArrayList<>();
		for (BugemonDTO bugemon : playerTeam) {
			boolean active = bugemon.getId().equals(activeBugemon.getId());
			boolean selectable = !active && bugemon.getHp() > 0;
			entries.add(new BugemonEntry(bugemon.getId(), bugemon.getName(), bugemon.getSpritePath(),
					bugemon.getHp() <= 0, active, selectable));
		}
		return entries;
	}

	/**
	 * Refreshes the list of Bugemons in the current player's team.
	 *
	 * @param player The current player
	 */
	public void updateTeam(PlayerDTO player) {
		try {
			if (this.clientController.getData(new GetPlayerTeamRequest(player.getUsername())) instanceof PlayerTeamResponse playerTeam) {
				player.setTeam(playerTeam.getBugemons());
				return;
			}
			throw new UnknownServerResponse("getPlayerTeam");
		} catch (Exception e) {
			LOGGER.warning("Impossible de mettre à jour l'équipe du joueur.");
		}
	}

	/**
	 * Refreshes battle UI content from current battle state.
	 */
	public void refreshView() {
		if (view == null) {
			return;
		}

		try {
			boolean isForcedToSwitch = this.isForcedSwitch();
			view.setForcedSwitch(isForcedToSwitch);
			Optional<BattleSnapshot> snapshot = buildBattleSnapshot();

			if (snapshot.isPresent()) {
				view.renderBattle(snapshot.get());
			}
			List<String> logs = consumeLogMessages();
			System.out.println("Consume logs in refresh view : " + logs);
			view.showLogMessages(logs);
		} catch (Exception e) {
			LOGGER.warning("Impossible de rafraichir l'interface.");
			return;
		}
		
	}

	/**
	 * Indicates whether the player is currently forced to switch bugemon.
	 *
	 * @return True if switch is mandatory
	 */
	private boolean isForcedSwitch() throws ServerStatusException, UnknownServerResponse {
		return this.getState() == BattleState.SWAPPING;
	}

	/**
	 * Builds a snapshot model for rendering the battle view.
	 *
	 * @return The battle snapshot, or null if unavailable
	 */
	public Optional<BattleSnapshot> buildBattleSnapshot() {
		List<BugemonDTO> activeBugemons;
		try {
			activeBugemons = this.getActiveBugemons();
		} catch (Exception e) {
			return Optional.empty();
		}
		BugemonDTO playerBugemon = activeBugemons.get(0);
		BugemonDTO opponentBugemon = activeBugemons.get(1);

		if (playerBugemon == null || opponentBugemon == null) {
			return Optional.empty();
		}
		BattleSnapshot snapshot = new BattleSnapshot(toBugemonDisplay(playerBugemon),
				toBugemonDisplay(opponentBugemon));
		return Optional.of(snapshot);
	}

	/**
	 * Retrieves and clears accumulated battle log messages.
	 *
	 * @return The current list of log messages
	 */
	public List<String> consumeLogMessages() throws ServerStatusException, UnknownServerResponse {
		System.out.println("CONSUME LOGS");
		if (this.clientController.getData(new GetLogsRequest(true)) instanceof LogsResponse logs) {
			return logs.getLogs();
		} 
		throw new UnknownServerResponse("getLogs");
	}

	/**
	 * Fetches the battle's state.
	 *
	 * @return The state of the battle
	 */
	public BattleState getState() throws ServerStatusException, UnknownServerResponse {
		if (this.clientController.getData(new GetBattleStateRequest()) instanceof BattleStateResponse battleState) {
			return battleState.getBattleState();
		} 
		throw new UnknownServerResponse("getBattleState");
	}

	/**
	 * Converts a bugemon DTO into a display model for the battle view.
	 *
	 * @param bugemon The bugemon to convert
	 * @return The corresponding DTO
	 */
	private BugemonDisplay toBugemonDisplay(BugemonDTO bugemon) {
		return new BugemonDisplay(bugemon.getName(), bugemon.getSpritePath(), bugemon.getType(), bugemon.level(),
				bugemon.getHp(), bugemon.baseStats().hp());
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
