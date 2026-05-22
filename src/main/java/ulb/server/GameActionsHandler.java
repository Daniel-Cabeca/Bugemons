package ulb.server;

import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.item.ItemDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.DTO.reward.RewardDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.UserFacingException;
import ulb.mapper.ability.AbilityMapper;
import ulb.mapper.item.ItemMapper;
import ulb.mapper.reward.RewardMapper;
import ulb.model.Player;
import ulb.model.ability.Ability;
import ulb.model.action.*;
import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.battle.MultiBattleParticipant;
import ulb.model.battle.MultiBattleSession;
import ulb.model.bugemon.Bugemon;
import ulb.model.item.Item;
import ulb.model.reward.Reward;
import ulb.model.reward.RewardType;
import ulb.model.tower.towerManager.TowerManager;
import ulb.service.InventoryService;
import ulb.service.MultiBattleService;
import ulb.service.strategy.StrategyRandom;

import java.util.List;

/**
 * Handles all in-game actions that a player can perform during a battle or tower run,
 * including choosing actions, rewards, and managing multiplayer sessions.
 */
public class GameActionsHandler extends Thread {
	private final InventoryService inventoryService;
	private final MultiBattleService multiBattleService;
	ClientHandler clientHandler;

	/**
	 * Creates a game actions handler for the given client and services.
	 *
	 * @param clientHandler the client handler owning this session
	 * @param inventoryService service for player inventory
	 * @param multiBattleService service for multiplayer battle sessions
	 */
	public GameActionsHandler(ClientHandler clientHandler, InventoryService inventoryService,
							  MultiBattleService multiBattleService) {
		this.clientHandler = clientHandler;
		this.inventoryService = inventoryService;
		this.multiBattleService = multiBattleService;
	}

	/**
	 * Abandons the current tower run and ends the tower game mode.
	 *
	 * @throws DataAccessException if the game is not currently in tower mode
	 */
	public void abandonTower() throws DataAccessException {
		boolean isGameTower = clientHandler.isGameTower();

		if (!isGameTower) {
			throw new DataAccessException("Cannot abandon the Tower if the game isn't in Tower mode");
		}
		clientHandler.finishTower();
		clientHandler.sendSuccessMessage();
	}

	/**
	 * Applies an ability reward by swapping an existing ability on a bugemon with a new one,
	 * then marks the current tower room as completed.
	 *
	 * @param bugemonDTO the bugemon that will receive the new ability
	 * @param oldAbilityDTO the ability to be replaced
	 * @param newAbilityDTO the new ability to learn
	 * @throws UserFacingException if the bugemon or the old ability cannot be found
	 * @throws DataAccessException if the room cannot be marked as completed
	 */
	public void chooseAbilityReward(BugemonDTO bugemonDTO, AbilityDTO oldAbilityDTO, AbilityDTO newAbilityDTO) throws UserFacingException, DataAccessException {
		Player player = clientHandler.getPlayer();
		TowerManager towerManager = clientHandler.getTowerManager();

		Bugemon chosenBugemon;
		try {
			chosenBugemon = player.getTeam().getBugemonById(bugemonDTO.getId());
		} catch (EntityNotFoundException e) {
			throw new UserFacingException("The bugemon chosen for the ability reward isn't present in the Team");
		}

		Ability oldAbility;
		try {
			oldAbility = chosenBugemon.getAbilities().getAbilityById(oldAbilityDTO.id());
		} catch (EntityNotFoundException e) {
			throw new UserFacingException("The ability to be swapped isn't learned by the Bugemon");
		}

		Ability newAbility = AbilityMapper.toEntity(newAbilityDTO);

		chosenBugemon.swapAbility(newAbility, oldAbility);

		this.completeCurrentRoom(towerManager);
	}

	/**
	 * Marks the current tower room as completed and notifies the client of success.
	 *
	 * @param towerManager the tower manager
	 * @throws DataAccessException if the room cannot be marked as completed
	 */
	private void completeCurrentRoom(TowerManager towerManager) throws DataAccessException {
		try {
			towerManager.setCurrentRoomCompleted(true);
		} catch (Exception e) {
			throw new DataAccessException("The room cannot be completed");
		}

		clientHandler.sendSuccessMessage();
	}

	/**
	 * Applies an item reward by adding the chosen item to the player's inventory, then marking the current.
	 * tower room as completed
	 *
	 * @param itemDTO the item to add to the player's inventory
	 * @throws DataAccessException if the room cannot be marked as completed
	 */
	public void chooseItemReward(ItemDTO itemDTO) throws DataAccessException {
		Player player = clientHandler.getPlayer();
		TowerManager towerManager = clientHandler.getTowerManager();

		Item item = ItemMapper.toEntity(itemDTO);
		player.getInventory().addItem(item, 1);

		inventoryService.insertItem(item, 1, player);

		this.completeCurrentRoom(towerManager);
	}

	/**
	 * Applies the chosen level-up reward from the pending reward choices and clears the pending state.
	 *
	 * @param rewardDTO the reward chosen by the player
	 * @throws UserFacingException if there are no pending rewards, the reward is null,
	 *                             or the reward does not match any available choice
	 * @throws DataAccessException if the reward cannot be applied
	 */
	public void chooseLevelUpReward(RewardDTO rewardDTO) throws UserFacingException, DataAccessException {
		List<Reward> pendingLevelUpRewards = clientHandler.getPendingLevelUpRewards();

		if (pendingLevelUpRewards == null || pendingLevelUpRewards.isEmpty()) {
			throw new UserFacingException("No pending level up reward to apply");
		}

		if (rewardDTO == null) {
			throw new UserFacingException("Invalid reward");
		}

		Reward chosenReward = RewardMapper.toEntity(rewardDTO);
		boolean applied = false;
		for (Reward reward : pendingLevelUpRewards) {
			if (reward.getStats().equals(chosenReward.getStats())) {
				reward.applyReward();
				applied = true;
				break;
			}
		}

		if (!applied) {
			throw new UserFacingException("Reward does not match the current level up choices");
		}

		clientHandler.clearPendingLevelUpState();
		clientHandler.sendSuccessMessage();
	}

	/**
	 * Applies a stat reward to the chosen bugemon, then marks the current tower room as completed.
	 *
	 * @param bugemonDTO the bugemon that will receive the stat reward
	 * @throws UserFacingException if the bugemon is not found in the player's team
	 * @throws DataAccessException if the room cannot be marked as completed
	 */
	public void chooseStatReward(BugemonDTO bugemonDTO) throws UserFacingException, DataAccessException {
		Player player = clientHandler.getPlayer();
		TowerManager towerManager = clientHandler.getTowerManager();

		Bugemon chosenBugemon;
		try {
			chosenBugemon = player.getTeam().getBugemonById(bugemonDTO.getId());
		} catch (EntityNotFoundException e) {
			throw new UserFacingException("The bugemon chosen for the ability reward isn't present in the Team");
		}

		Reward reward = new Reward(chosenBugemon);
		reward.configureReward(RewardType.COMBINATION);

		chosenBugemon.changeBaseStats(reward.getStats());
		chosenBugemon.changeFightStats(reward.getStats());

		this.completeCurrentRoom(towerManager);
	}

	/**
	 * Moves the player to the specified room in the current tower floor.
	 *
	 * @param roomId the ID of the room to move to
	 * @throws DataAccessException if the game is not in tower mode, the room cannot be selected,
	 *                             or the move to the room fails
	 */
	public void chooseTowerRoom(int roomId) throws DataAccessException {
		boolean isGameTower = clientHandler.isGameTower();

		if (!isGameTower) {
			throw new DataAccessException("Cannot choose a Tower room if the game isn't in Tower mode");
		}
		try {
			clientHandler.nextTowerRoom(roomId);
		} catch (Exception e) {
			throw new DataAccessException("The room cannot be selected");
		}

		if (clientHandler.isCurrentRoomIdEqual(roomId)) {
			clientHandler.sendSuccessMessage();
		} else {
			throw new DataAccessException("Cannot move to the selected room");
		}
	}

	/**
	 * Picks a random action for the player's team in the current battle.
	 */
	public void chooseRandomAction() {
		Battle battle = clientHandler.getBattle();
		ParticipantLabel teamLabel = clientHandler.getTeamLabel();

		StrategyRandom strategyRandom = new StrategyRandom();
		Action randomAction = strategyRandom.pickAction(battle, teamLabel);
		battle.chooseAction(randomAction, teamLabel);

		clientHandler.sendSuccessMessage();
	}

	public void readyToPlay(){
		ParticipantLabel teamLabel = this.clientHandler.getTeamLabel();
		this.clientHandler.getBattle().readyToPlay(teamLabel);

		this.clientHandler.sendSuccessMessage();
	}

	/**
	 * Registers a run action for the player's team. In tower mode, resets bugemon HP and
	 * rewinds to the previous room (or finishes the tower if on the final floor).
	 * In classic mode, clears any pending level-up state.
	 *
	 * @throws DataAccessException if the previous room cannot be selected in tower mode
	 */
	public void chooseRunAction() throws DataAccessException {
		Player player = clientHandler.getPlayer();
		Battle battle = clientHandler.getBattle();
		ParticipantLabel teamLabel = clientHandler.getTeamLabel();
		boolean isGameTower = clientHandler.isGameTower();
		TowerManager towerManager = clientHandler.getTowerManager();

		battle.chooseAction(new RunAction(), teamLabel);

		if (isGameTower) {
			for (Bugemon bugemon : player.getTeam().getMembers()) {
				bugemon.getFightStats().setHp(bugemon.getBaseStats().getHp());
			}
			if (towerManager.isFinalFloor()) {
				clientHandler.finishTower();
			} else {
				try {
					towerManager.getCurrentFloorManager().rewindRoom();
					towerManager.setFledBattle(true);
				} catch (Exception e) {
					throw new DataAccessException("The previous room cannot be selected");
				}

				clientHandler.setBattle(towerManager.getCurrentBattle());
			}
		} else {
			clientHandler.clearPendingLevelUpState();
		}

		clientHandler.sendSuccessMessage();
	}

	/**
	 * Joins or creates a multiplayer battle session with the specified opponent,
	 * and sets the battle and team label on the client handler.
	 *
	 * @param opponentDTO the opponent player
	 * @throws UserFacingException if either the player or the opponent is not registered
	 */
	public void startMultiBattle(PlayerDTO opponentDTO) throws UserFacingException {
		Player self = this.clientHandler.getPlayer();

		if (self.getUserId().isEmpty() || opponentDTO.getUserId() == -1) {
			throw new UserFacingException("The player or the opponent isn't registred");
		}

		MultiBattleSession session = this.multiBattleService.getMultiBattle(self.getUserId().get(),
				opponentDTO.getUserId());
		Battle battle = session.getBattle();
		MultiBattleParticipant participant = session.getParticipant(self.getUserId().get());

		this.clientHandler.setBattle(battle);
		// this.clientHandler.setMultiBattleSession(session);
		this.clientHandler.setTeamLabel(participant.getParticipantLabel());

		clientHandler.sendSuccessMessage();
	}

	/**
	 * Declines or withdraws from a pending multiplayer battle session with the specified opponent.
	 *
	 * @param opponentDTO the opponent player of the session to quit
	 * @throws UserFacingException if either the player or the opponent is not registered
	 */
	public void quitMultiBattle(PlayerDTO opponentDTO) throws UserFacingException {
		Player self = this.clientHandler.getPlayer();

		if (self.getUserId().isEmpty() || opponentDTO.getUserId() == -1) {
			throw new UserFacingException("The player or the opponent isn't registred");
		}

		MultiBattleSession session = this.multiBattleService.getMultiBattle(self.getUserId().get(),
				opponentDTO.getUserId());
		MultiBattleParticipant participant = session.getParticipant(self.getUserId().get());

		participant.decline();

		clientHandler.sendSuccessMessage();
	}

	/**
	 * Registers a swap action for the player's team.
	 *
	 * @param bugemonDTOToSwap the bugemon to swap in
	 * @throws UserFacingException if the bugemon to swap is not found in the player's team
	 */
	public void chooseSwapBugemonAction(BugemonDTO bugemonDTOToSwap) throws UserFacingException {
		Player player = clientHandler.getPlayer();
		Battle battle = clientHandler.getBattle();
		ParticipantLabel teamLabel = clientHandler.getTeamLabel();

		Bugemon bugemonToSwap;
		try {
			bugemonToSwap = player.getTeam().getBugemonById(bugemonDTOToSwap.getId());
		} catch (EntityNotFoundException e) {
			throw new UserFacingException("The bugemon to swap isn't present in the Team");
		}

		battle.chooseAction(new SwapAction(bugemonToSwap), teamLabel);

		clientHandler.sendSuccessMessage();
	}

	/**
	 * Registers a use ability action for the player's team in the current battle.
	 *
	 * @param abilityDTO the ability to use
	 * @throws DataAccessException if the action cannot be registered
	 */
	public void chooseUseAbilityAction(AbilityDTO abilityDTO) throws DataAccessException {
		Battle battle = clientHandler.getBattle();
		ParticipantLabel teamLabel = clientHandler.getTeamLabel();

		Ability ability = AbilityMapper.toEntity(abilityDTO);
		battle.chooseAction(new UseAbilityAction(ability), teamLabel);

		clientHandler.sendSuccessMessage();
	}

	/**
	 * Registers a use item action for the player's team and removes the item from the inventory.
	 *
	 * @param itemDTO the item to use
	 * @throws DataAccessException if the action cannot be registered or the item cannot be removed
	 */
	public void chooseUseItemAction(ItemDTO itemDTO) throws DataAccessException {
		Player player = clientHandler.getPlayer();
		Battle battle = clientHandler.getBattle();
		ParticipantLabel teamLabel = clientHandler.getTeamLabel();

		Item item = ItemMapper.toEntity(itemDTO);
		battle.chooseAction(new UseItemAction(item), teamLabel);
		inventoryService.deleteItem(item, 1, player);
		clientHandler.sendSuccessMessage();
	}
}
