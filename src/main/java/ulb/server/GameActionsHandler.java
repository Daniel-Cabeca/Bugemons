package ulb.server;

import java.util.List;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.reward.RewardDTO;
import ulb.mapper.ability.AbilityMapper;
import ulb.mapper.item.ItemMapper;
import ulb.mapper.reward.RewardMapper;
import ulb.message.clientToServer.AbandonTowerMessage;
import ulb.message.clientToServer.ChooseAbilityRewardMessage;
import ulb.message.clientToServer.ChooseItemRewardMessage;
import ulb.message.clientToServer.ChooseLevelUpRewardMessage;
import ulb.message.clientToServer.ChooseStatRewardMessage;
import ulb.message.clientToServer.PickRandomActionMessage;
import ulb.message.clientToServer.RunMessage;
import ulb.message.clientToServer.SwapBugemonMessage;
import ulb.message.clientToServer.UseAbilityMessage;
import ulb.message.clientToServer.UseItemMessage;
import ulb.model.Player;
import ulb.model.ability.Ability;
import ulb.model.action.Action;
import ulb.model.action.Run;
import ulb.model.action.Swap;
import ulb.model.action.UseAbility;
import ulb.model.action.UseItem;
import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.bugemon.Bugemon;
import ulb.model.item.Item;
import ulb.model.reward.Reward;
import ulb.model.reward.RewardType;
import ulb.model.tower.towerManager.TowerManager;
import ulb.service.InventoryService;
import ulb.service.strategy.StrategyRandom;

public class GameActionsHandler extends Thread{
    ClientHandler clientHandler;
    InventoryService inventoryService;

    public GameActionsHandler(ClientHandler clientHandler, InventoryService inventoryService) {
        this.clientHandler = clientHandler;
        this.inventoryService = inventoryService;
    }

	public void handle(PickRandomActionMessage message){
        Battle battle = clientHandler.getBattle();
        ParticipantLabel teamLabel = clientHandler.getTeamLabel();

		StrategyRandom strategyRandom = new StrategyRandom();
		Action randomAction = strategyRandom.pickAction(battle, teamLabel);
		battle.chooseAction(randomAction, teamLabel);

		clientHandler.sendSuccessMessage();
	}

	// public void handle(RunMessage message){
    //     Player player = clientHandler.getPlayer();
    //     Battle battle = clientHandler.getBattle();
    //     ParticipantLabel teamLabel = clientHandler.getTeamLabel();
    //     boolean isGameTower = clientHandler.isGameTower();
    //     TowerManager towerManager = clientHandler.getTowerManager();

	// 	battle.chooseAction(new Run(), teamLabel);

	// 	if (isGameTower) {
	// 		for (Bugemon bugemon : player.getTeam().getMembers()) {
	// 			bugemon.getFightStats().setHp(bugemon.getBaseStats().getHp());
	// 		}
	// 		towerManager.getCurrentFloorManager().rewindRoom();
	// 		battle = towerManager.getCurrentBattle();
	// 	} else {
    //         clientHandler.setBattle(null);
	// 		clientHandler.clearPendingLevelUpState();
	// 	}

    //     clientHandler.sendSuccessMessage();
	// }

	public void handle(RunMessage message){
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
				// this.battle = this.towerManager.getCurrentBattle();
			}
		} else {
			clientHandler.setBattle(null);
			// this.battle = null;
			clientHandler.clearPendingLevelUpState();
		}

        clientHandler.sendSuccessMessage();
	}

	public void handle(AbandonTowerMessage message){
        boolean isGameTower = clientHandler.isGameTower();

		if (!isGameTower){
			clientHandler.sendErrorMessage(getName());
			return;
		}
		clientHandler.finishTower();
		clientHandler.sendSuccessMessage();
	}

	public void handle(SwapBugemonMessage message){
        Player player = clientHandler.getPlayer();
        Battle battle = clientHandler.getBattle();
        ParticipantLabel teamLabel = clientHandler.getTeamLabel();

		BugemonDTO bugemonDTOToSwap = message.getBugemonToSwap();
		Bugemon bugemonToSwap = player.getTeam().getBugemonById(bugemonDTOToSwap.getId());
		battle.chooseAction(new Swap(bugemonToSwap), teamLabel);

		clientHandler.sendSuccessMessage();
	}

	public void handle(UseAbilityMessage message){
        Battle battle = clientHandler.getBattle();
        ParticipantLabel teamLabel = clientHandler.getTeamLabel();

		Ability ability = AbilityMapper.toEntity(message.getAbility());
		battle.chooseAction(new UseAbility(ability), teamLabel);

		clientHandler.sendSuccessMessage();
	}

	public void handle(UseItemMessage message){
        Player player = clientHandler.getPlayer();
        Battle battle = clientHandler.getBattle();
        ParticipantLabel teamLabel = clientHandler.getTeamLabel();

		Item item = ItemMapper.toEntity(message.getItem());
		battle.chooseAction(new UseItem(item), teamLabel);
		inventoryService.deleteItem(item, 1, player.getUsername());
		clientHandler.sendSuccessMessage();
	}

	public void handle(ChooseAbilityRewardMessage message){
        Player player = clientHandler.getPlayer();
        TowerManager towerManager = clientHandler.getTowerManager();

		BugemonDTO bugemonDTO = message.getBugemon();
		Bugemon chosenBugemon = player.getTeam().getBugemonById(bugemonDTO.getId());

		if (chosenBugemon == null){
			clientHandler.sendErrorMessage("Bugemon not present in the Team");
			return;
		}

		Ability oldAbility = chosenBugemon.getAbilities().getAbilityById(message.getOldAbility().id());
		if (oldAbility == null){
			clientHandler.sendErrorMessage("Ability not learned");
			return;
		}
		Ability newAbility = AbilityMapper.toEntity(message.getNewAbility());

		chosenBugemon.swapAbility(newAbility, oldAbility);

		towerManager.getCurrentRoomManager().setRoomCompleted(true);
		// clientHandler.nextTowerRoom();
		clientHandler.sendSuccessMessage();
	}

	public void handle(ChooseItemRewardMessage message){
        Player player = clientHandler.getPlayer();
        TowerManager towerManager = clientHandler.getTowerManager();

		Item item = ItemMapper.toEntity(message.getItem());
		player.getInventory().addItem(item, 1);

		inventoryService.insertItem(item, 1, player.getUsername());

		towerManager.getCurrentRoomManager().setRoomCompleted(true);

		// clientHandler.nextTowerRoom();
		clientHandler.sendSuccessMessage();
	}

	public void handle(ChooseStatRewardMessage message){
        Player player = clientHandler.getPlayer();
        TowerManager towerManager = clientHandler.getTowerManager();

		BugemonDTO bugemonDTO = message.getBugemon();
		Bugemon chosenBugemon = player.getTeam().getBugemonById(bugemonDTO.getId());

		if (chosenBugemon == null){
			clientHandler.sendErrorMessage("Bugemon not present in the Team");
			return;
		}

		Reward reward = new Reward(chosenBugemon);
		reward.configureReward(RewardType.COMBINATION);

		chosenBugemon.changeBaseStats(reward.getStats());
		chosenBugemon.changeFightStats(reward.getStats());

		towerManager.getCurrentRoomManager().setRoomCompleted(true);
		// clientHandler.nextTowerRoom();
		clientHandler.sendSuccessMessage();
	}

	public void handle(ChooseLevelUpRewardMessage message){
        List<Reward> pendingLevelUpRewards = clientHandler.getPendingLevelUpRewards();
        
		if (pendingLevelUpRewards == null || pendingLevelUpRewards.isEmpty()) {
			clientHandler.sendErrorMessage("No pending level up reward to apply");
			return;
		}

		RewardDTO rewardDTO = message.getReward();
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
}
