package ulb.server;

import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.item.ItemDTO;
import ulb.exceptions.DataAccessException;

import java.util.List;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.DTO.reward.RewardDTO;
import ulb.mapper.ability.AbilityMapper;
import ulb.mapper.item.ItemMapper;
import ulb.mapper.reward.RewardMapper;
import ulb.message.clientToServer.*;
import ulb.model.Player;
import ulb.model.ability.Ability;
import ulb.model.action.Action;
import ulb.model.action.Run;
import ulb.model.action.Swap;
import ulb.model.action.UseAbility;
import ulb.model.action.UseItem;
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

public class GameActionsHandler extends Thread{
    ClientHandler clientHandler;
    private final InventoryService inventoryService;
	private final MultiBattleService multiBattleService;

    public GameActionsHandler(ClientHandler clientHandler, InventoryService inventoryService, MultiBattleService multiBattleService) {
        this.clientHandler = clientHandler;
        this.inventoryService = inventoryService;
		this.multiBattleService = multiBattleService;
    }

	public void abandonTower() throws DataAccessException{
        boolean isGameTower = clientHandler.isGameTower();

		if (!isGameTower){
			clientHandler.sendErrorMessage(getName());
			return;
		}
		clientHandler.finishTower();
		clientHandler.sendSuccessMessage();
	}


	public void chooseAbilityReward(BugemonDTO bugemonDTO, AbilityDTO oldAbilityDTO, AbilityDTO newAbilityDTO) throws DataAccessException{
        Player player = clientHandler.getPlayer();
        TowerManager towerManager = clientHandler.getTowerManager();

		Bugemon chosenBugemon = player.getTeam().getBugemonById(bugemonDTO.getId());

		if (chosenBugemon == null){
			clientHandler.sendErrorMessage("Bugemon not present in the Team");
			return;
		}

		Ability oldAbility = chosenBugemon.getAbilities().getAbilityById(oldAbilityDTO.id());
		if (oldAbility == null){
			clientHandler.sendErrorMessage("Ability not learned");
			return;
		}
		Ability newAbility = AbilityMapper.toEntity(newAbilityDTO);

		chosenBugemon.swapAbility(newAbility, oldAbility);

		towerManager.setCurrentRoomCompleted(true);
		clientHandler.sendSuccessMessage();
	}

	public void chooseItemReward(ItemDTO itemDTO) throws DataAccessException{
        Player player = clientHandler.getPlayer();
        TowerManager towerManager = clientHandler.getTowerManager();

		Item item = ItemMapper.toEntity(itemDTO);
		player.getInventory().addItem(item, 1);

		inventoryService.insertItem(item, 1, player);

		towerManager.setCurrentRoomCompleted(true);

		clientHandler.sendSuccessMessage();
	}

	public void chooseLevelUpReward(RewardDTO rewardDTO) throws DataAccessException{
        List<Reward> pendingLevelUpRewards = clientHandler.getPendingLevelUpRewards();
        
		if (pendingLevelUpRewards == null || pendingLevelUpRewards.isEmpty()) {
			clientHandler.sendErrorMessage("No pending level up reward to apply");
			return;
		}

		if (rewardDTO == null) {
			clientHandler.sendErrorMessage("Invalid reward");
			return;
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
			clientHandler.sendErrorMessage("Reward does not match the current level up choices");
			return;
		}

		clientHandler.clearPendingLevelUpState();
		clientHandler.sendSuccessMessage();
	}

	public void chooseStatReward(BugemonDTO bugemonDTO) throws DataAccessException{
        Player player = clientHandler.getPlayer();
        TowerManager towerManager = clientHandler.getTowerManager();

		Bugemon chosenBugemon = player.getTeam().getBugemonById(bugemonDTO.getId());

		if (chosenBugemon == null){
			clientHandler.sendErrorMessage("Bugemon not present in the Team");
			return;
		}

		Reward reward = new Reward(chosenBugemon);
		reward.configureReward(RewardType.COMBINATION);

		chosenBugemon.changeBaseStats(reward.getStats());
		chosenBugemon.changeFightStats(reward.getStats());

		towerManager.setCurrentRoomCompleted(true);
		clientHandler.sendSuccessMessage();
	}

	public void chooseTowerRoom(int roomId) throws DataAccessException{
		boolean isGameTower = clientHandler.isGameTower();

		if (!isGameTower){
			clientHandler.sendErrorMessage("The game isn't in tower mode");
			return;
		}
		if (clientHandler.nextTowerRoom(roomId)){
			clientHandler.sendSuccessMessage();
		}
		else{
			clientHandler.sendErrorMessage("Cannot move to the selected room");
			return;
		}
	}

	public void chooseRandomAction() throws DataAccessException{
        Battle battle = clientHandler.getBattle();
        ParticipantLabel teamLabel = clientHandler.getTeamLabel();

		StrategyRandom strategyRandom = new StrategyRandom();
		Action randomAction = strategyRandom.pickAction(battle, teamLabel);
		battle.chooseAction(randomAction, teamLabel);

		clientHandler.sendSuccessMessage();
	}

	public void chooseRunAction() throws DataAccessException{
		Player player = clientHandler.getPlayer();
        Battle battle = clientHandler.getBattle();
        ParticipantLabel teamLabel = clientHandler.getTeamLabel();
        boolean isGameTower = clientHandler.isGameTower();
        TowerManager towerManager = clientHandler.getTowerManager();

		battle.chooseAction(new Run(), teamLabel);

		if (isGameTower) {
			for (Bugemon bugemon : player.getTeam().getMembers()) {
				bugemon.getFightStats().setHp(bugemon.getBaseStats().getHp());
			}
			if (towerManager.isFinalFloor()) {
				clientHandler.finishTower();
			} else {
				towerManager.getCurrentFloorManager().rewindRoom();
				clientHandler.setBattle(towerManager.getCurrentBattle());
			}
		} else {
			// clientHandler.setBattle(null);
			clientHandler.clearPendingLevelUpState();
		}

        clientHandler.sendSuccessMessage();
	}

	public void startMultiBattle(PlayerDTO opponentDTO) throws DataAccessException {
		Player self = this.clientHandler.getPlayer();

		MultiBattleSession session = this.multiBattleService.getMultiBattle(self.getUserId(), opponentDTO.getUserId());
		Battle battle = session.getBattle();
		MultiBattleParticipant participant = session.getParticipant(self.getUserId());

		this.clientHandler.setBattle(battle);
		this.clientHandler.setMultiBattleSession(session);
		this.clientHandler.setTeamLabel(participant.getParticipantLabel());

		clientHandler.sendSuccessMessage();
	}

	public void chooseSwapBugemonAction(BugemonDTO bugemonDTOToSwap) throws DataAccessException{
        Player player = clientHandler.getPlayer();
        Battle battle = clientHandler.getBattle();
        ParticipantLabel teamLabel = clientHandler.getTeamLabel();

		Bugemon bugemonToSwap = player.getTeam().getBugemonById(bugemonDTOToSwap.getId());
		battle.chooseAction(new Swap(bugemonToSwap), teamLabel);

		clientHandler.sendSuccessMessage();
	}

	public void chooseUseAbilityAction(AbilityDTO abilityDTO) throws DataAccessException{
        Battle battle = clientHandler.getBattle();
        ParticipantLabel teamLabel = clientHandler.getTeamLabel();

		Ability ability = AbilityMapper.toEntity(abilityDTO);
		battle.chooseAction(new UseAbility(ability), teamLabel);

		clientHandler.sendSuccessMessage();
	}

	public void chooseUseItemAction(ItemDTO itemDTO) throws DataAccessException{
        Player player = clientHandler.getPlayer();
        Battle battle = clientHandler.getBattle();
        ParticipantLabel teamLabel = clientHandler.getTeamLabel();

		Item item = ItemMapper.toEntity(itemDTO);
		battle.chooseAction(new UseItem(item), teamLabel);
		inventoryService.deleteItem(item, 1, player);
		clientHandler.sendSuccessMessage();
	}
}
