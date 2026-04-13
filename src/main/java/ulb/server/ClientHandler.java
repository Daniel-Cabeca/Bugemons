package ulb.server;

import ulb.communication.Messenger.SocketMessenger;
import ulb.controller.action.Action;
import ulb.controller.action.Run;
import ulb.controller.action.Swap;
import ulb.controller.action.UseAbility;
import ulb.controller.action.UseItem;
import ulb.mapper.ability.AbilityMapper;
import ulb.mapper.bugemon.BugemonMapper;
import ulb.mapper.bugemon.BugemonSpeciesMapper;
import ulb.mapper.item.ItemMapper;
import ulb.mapper.player.PlayerMapper;
import ulb.mapper.reward.RewardMapper;
import ulb.message.ClientToServerMessage;
import ulb.message.clientToServer.*;
import ulb.message.serverToClient.*;
import ulb.message.serverToClient.NextWindowMessage.WindowType;
import ulb.model.Player;
import ulb.model.ability.Ability;
import ulb.model.battle.Battle;
import ulb.model.battle.BattleState;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.BugemonSpecies;
import ulb.model.item.Item;
import ulb.model.reward.Reward;
import ulb.model.reward.RewardType;
import ulb.model.team.OpponentTeamGenerator;
import ulb.model.team.Team;
import ulb.service.*;
import ulb.model.tower.towerManager.TowerManager;
import ulb.service.strategy.AI;
import ulb.service.strategy.StrategyRandom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.bugemon.BugemonSpeciesDTO;
import ulb.DTO.item.ItemDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.DTO.reward.RewardDTO;

public class ClientHandler extends Thread implements ServerMessageHandler{
    private SocketMessenger socketMessenger;
    private boolean stop;

    private Player player;

    private Battle battle;
    private Battle.ParticipantLabel teamLabel;
    private Thread opponentBot;

	private TowerManager towerManager;
	private boolean isGameTower;

	private Bugemon pendingLevelUpBugemon;
	private List<Reward> pendingLevelUpRewards;

	private final AbilityService abilityService;
	private final BugemonService bugemonService;
	private final ItemService itemService;
	private final AccountService accountService;
	private final ChatService chatService;

	private void resetGameSessionState() {
		this.battle = null;
		this.teamLabel = null;
		this.towerManager = null;
		this.isGameTower = false;
		clearPendingLevelUpState();
	}

    public ClientHandler(SocketMessenger messenger,
    		AbilityService abilityService, BugemonService bugemonService, ItemService itemService,
    		AccountService accountService, ChatService chatService) {
        this.socketMessenger = messenger;
        this.stop = false;
		this.abilityService = abilityService;
		this.bugemonService = bugemonService;
		this.itemService = itemService;
		this.accountService = accountService;
		this.chatService = chatService;
    }

	public AbilityService getAbilityService() { return this.abilityService; }
	public BugemonService getBugemonService() { return this.bugemonService; }
	public ItemService getItemService() { return this.itemService; }
	public AccountService getAccountService() { return this.accountService; }
	public ChatService getChatService() { return this.chatService; }

    @Override
    public void run(){
        while (! this.stop){
            handleMessage();
        }
        end();
    }

    public void stopProcess(){
        this.stop = true;
    }

    public ClientToServerMessage receiveMessage(){
        try{
			Serializable received = this.socketMessenger.receiveMessage();

			if (received instanceof ClientToServerMessage message) {
				return message;
			}

            return null;
        } catch (Exception e){
            stopProcess();
        }
        return null;
    }

    public void sendMessage(Serializable message){
        try{
            this.socketMessenger.sendMessage(message);
        } catch (Exception e){
            stopProcess();
        }
    }

    public void sendErrorMessage(String errorMessage){
        sendMessage(new StatusMessage(false, errorMessage));
    }

    public void sendSuccessMessage(){
        sendMessage(new StatusMessage(true));
    }

    public void end(){
        this.socketMessenger.close();
    }

	/**
	 * Go to the next room if the game is in tower mode
	 */
	public void nextTowerRoom(){
		if (isGameTower){
			this.towerManager.nextRoom();
			this.battle = this.towerManager.getCurrentBattle();
		}
	}

	private void clearPendingLevelUpState() {
		this.pendingLevelUpBugemon = null;
		this.pendingLevelUpRewards = null;
	}

	// SETUP

	@Override
	public void handle(RegisterMessage message){
		boolean success;
		this.player = PlayerMapper.toEntity(message.getPlayer(), this.itemService);

		if (message.isLogin()) {
			success = this.getAccountService().login(this.player.getName(), this.player.getPassword());
		}
		else {
			success = this.getAccountService().register(this.player.getName(), this.player.getPassword());
		}
		if (success) {
			sendSuccessMessage();
		}
		else {
			sendErrorMessage("Register failed");
		}
	}

	@Override
	public void handle(SetUpTeamMessage message){
		Team team = new Team();
		
		for (BugemonDTO bugemonDTO : message.getTeam()){
			if (!team.add(BugemonMapper.toEntity(bugemonDTO))){
				sendErrorMessage("Invalid Team");
			}
		}

		this.player.setTeam(team);
		sendSuccessMessage();
	}

	@Override
	public void handle(SetUpNormalModeMessage message){
		if (player == null){
			sendErrorMessage("Player not initialized !");
			return;
		}

		if (this.battle != null || this.towerManager != null || this.isGameTower) {
			resetGameSessionState();
		}

		Team teamB;

		try {
			teamB = OpponentTeamGenerator.generateRandomOpponentTeam(player.getTeam(), this.getBugemonService());
		} catch (Exception e){
			sendErrorMessage(e.getMessage());
			return;
		}
		this.battle = new Battle(player.getTeam(), teamB, player, new Player(this.getItemService()));
		this.teamLabel = Battle.ParticipantLabel.TEAM_A;
		this.isGameTower = false;

		this.opponentBot = new AI(battle, new StrategyRandom());
		this.opponentBot.start();
		clearPendingLevelUpState();

		sendSuccessMessage();
	}

	@Override
	public void handle(SetUpTowerModeMessage message){
		if (this.battle != null || this.towerManager != null || this.isGameTower) {
			resetGameSessionState();
		}

		this.towerManager = new TowerManager(player, this.getBugemonService(), this.getItemService());
		this.battle = towerManager.getCurrentBattle();
		this.teamLabel = Battle.ParticipantLabel.TEAM_A;
		this.isGameTower = true;
		clearPendingLevelUpState();

		sendSuccessMessage();
	}

	// GAME INFO

	public void handle(GetPlayerMessage message) {
		if (message.getUsername().equals(this.player.getName())){
			PlayerDTO playerDTO = PlayerMapper.toDTO(this.player);
			System.out.println(playerDTO.getName());
			sendMessage(new PlayerMessage(playerDTO));
		}
		else{
			sendErrorMessage("Wrong Username");
		}
		
	}

	@Override
	public void handle(CheckGameFinishedMessage message){
		sendMessage(new GameFinishedMessage(this.battle.isGameFinished()));
	}

	@Override
	public void handle(GetBattleStateMessage message){
		sendMessage(new BattleStateMessage(this.battle.getState(teamLabel)));
	}

	@Override
	public void handle(GetLogsMessage message){
		int selfHpAfterFirstAction = this.battle.getHpAfterFirstActionSelf(teamLabel);
		int opponentHpAfterFirstAction = this.battle.getHpAfterFirstActionOpponent(teamLabel);
		
		List<String> logs = new ArrayList<String>(this.battle.getLogMsg());
		
		if (message.clearLogs()){
			this.battle.clearLogMsg();
		}

		sendMessage(new LogsMessage(List.of(selfHpAfterFirstAction, opponentHpAfterFirstAction), logs));
	}

	@Override
	public void handle(CheckUsableItemMessage message){
		Map<String, Boolean> usableItems = new HashMap<String, Boolean>();

		for (ItemDTO itemDTO : message.getItems()){
			Item item = ItemMapper.toEntity(itemDTO);
			usableItems.put(itemDTO.getId(), this.battle.checkItem(item, teamLabel));
		}

        sendMessage(new UsableItemsMessage(usableItems));
	}

	@Override
	public void handle(GetAbilityEffectivenessMessage message){
		Map<AbilityDTO, String> effectiveness = new HashMap<AbilityDTO, String>();
		Bugemon bugemonTarger = BugemonMapper.toEntity(message.getBugemonTarget());
		
		for (AbilityDTO abilityDTO : message.getAbilities()){
			Ability ability = AbilityMapper.toEntity(abilityDTO);
			String effectivenessMessage = ability.getEffectivenessMessage(bugemonTarger);
			effectiveness.put(abilityDTO, effectivenessMessage);
		}

		sendMessage(new AbilityEffectivenessMessage(effectiveness));
	}

	@Override
	public void handle(GetActiveBugemonsMessage message){
		if (battle == null){
			sendErrorMessage("The battle has not been created");
			return;
		}
		Bugemon selfActive = this.battle.getActiveBugemon(teamLabel);
		Bugemon opponentActive = this.battle.getActiveBugemon(this.battle.getOpponentTeamLabel(teamLabel)); 
		
		sendMessage(new ActiveBugemonsMessage(BugemonMapper.toDTO(selfActive), BugemonMapper.toDTO(opponentActive)));
	}

	@Override
	public void handle(GetTowerInfoMessage message){
		if (!isGameTower){
			sendErrorMessage("The game isn't in tower mode");
			return;
		}
		int towerFloorNumber = this.towerManager.getFloorNumber();
		int towerRoomNumber = this.towerManager.getCurrentRoomIndex();

		sendMessage(new TowerInfoMessage(towerFloorNumber, towerRoomNumber));
	}

	@Override
	public void handle(GetNextWindowMessage message){
		WindowType nextWindow = WindowType.MAIN_MENU;

		if (this.player != null && this.player.getTeam().getLevelUpBugemonNumber() > 0){
			nextWindow = WindowType.LEVEL_UP;
			sendMessage(new NextWindowMessage(nextWindow));
			return;
		}

		if (this.isGameTower){
			if (this.battle != null && this.battle.isGameFinished()) {
				boolean won = this.battle.getState(teamLabel) == BattleState.WON;

				if (won) {
					this.towerManager.getCurrentRoomManager().setRoomCompleted(true);
					this.battle.resetFightStats();
					nextWindow = WindowType.NEXT_ROOM;
					nextTowerRoom();
				} else {
					nextWindow = WindowType.MAIN_MENU;
				}

				sendMessage(new NextWindowMessage(nextWindow));
				return;
			}

			switch (towerManager.getCurrentRoomType()) {
				case BATTLE:
				case BOSS:
					nextWindow = WindowType.GAME;
					break;

				case REWARD:
					nextWindow = WindowType.REWARD;
					break;

				default:
					nextWindow = WindowType.MAIN_MENU;
					break;
			}

			sendMessage(new NextWindowMessage(nextWindow));
			return;
		}

		if (this.battle == null){
			sendMessage(new NextWindowMessage(nextWindow));
			return;
		}

		nextWindow = this.battle.isGameFinished() ? WindowType.MAIN_MENU : WindowType.GAME;
		sendMessage(new NextWindowMessage(nextWindow));
	}

	@Override
	public void handle(GetBattleEndInfoMessage message){
		boolean isWin = this.battle != null && this.battle.getState(teamLabel) == BattleState.WON;
		int gainedXp = 0;

		if (isWin && this.battle != null){
			gainedXp = this.battle.computeTotalXP(this.battle.getTeam(this.battle.getOpponentTeamLabel(teamLabel)));
		}

		if (this.battle != null && this.battle.isGameFinished()){
			this.battle = null;
		}
		clearPendingLevelUpState();

		sendMessage(new BattleEndInfoMessage(isWin, gainedXp));
	}

	@Override
	public void handle(GetLevelUpInfoMessage message){
		if (this.battle == null || this.player == null || this.player.getTeam() == null) {
			sendErrorMessage("No pending level up information available");
			return;
		}

		Bugemon currentBugemon = this.player.getTeam().getFirstLevelUpBugemon();
		if (currentBugemon == null) {
			clearPendingLevelUpState();
			sendErrorMessage("No bugemon requires a level up reward");
			return;
		}

		if (this.pendingLevelUpBugemon == null
				|| this.pendingLevelUpRewards == null
				|| !this.pendingLevelUpBugemon.getId().equals(currentBugemon.getId())) {
			this.pendingLevelUpBugemon = currentBugemon;
			this.pendingLevelUpRewards = new ArrayList<>(this.battle.computeRewards(currentBugemon));
		}

		List<RewardDTO> rewardDTOs = new ArrayList<>();
		for (Reward reward : this.pendingLevelUpRewards) {
			rewardDTOs.add(RewardMapper.toDTO(reward));
		}

		sendMessage(new LevelUpInfoMessage(BugemonMapper.toDTO(currentBugemon), rewardDTOs));
	}

	//ACTIONS

	@Override
	public void handle(PickRandomActionMessage message){
		StrategyRandom strategyRandom = new StrategyRandom();
		Action randomAction = strategyRandom.pickAction(battle, teamLabel);
		this.battle.chooseAction(randomAction, teamLabel);

		sendSuccessMessage();
	}

	@Override
	public void handle(RunMessage message){
		this.battle.chooseAction(new Run(), teamLabel);

		if (this.isGameTower) {
			for (Bugemon bugemon : this.player.getTeam().getMembers()) {
				bugemon.getFightStats().setHp(bugemon.getBaseStats().getHp());
			}
			this.towerManager.getCurrentFloorManager().rewindRoom();
			this.battle = this.towerManager.getCurrentBattle();
		} else {
			this.battle = null;
			clearPendingLevelUpState();
		}

        sendSuccessMessage();
	}

	@Override
	public void handle(SwapBugemonMessage message){
		BugemonDTO bugemonDTOToSwap = message.getBugemonToSwap();
		Bugemon bugemonToSwap = this.player.getTeam().getBugemonById(bugemonDTOToSwap.getId());
		this.battle.chooseAction(new Swap(bugemonToSwap), teamLabel);

		sendSuccessMessage();
	}

	@Override
	public void handle(UseAbilityMessage message){
		Ability ability = AbilityMapper.toEntity(message.getAbility());
		this.battle.chooseAction(new UseAbility(ability), teamLabel);

		sendSuccessMessage();
	}

	@Override
	public void handle(UseItemMessage message){
		Item item = ItemMapper.toEntity(message.getItem());
		this.battle.chooseAction(new UseItem(item), teamLabel);

		sendSuccessMessage();
	}

	@Override
	public void handle(ChooseAbilityRewardMessage message){
		BugemonDTO bugemonDTO = message.getBugemon();
		Bugemon chosenBugemon = player.getTeam().getBugemonById(bugemonDTO.getId());

		if (chosenBugemon == null){
			sendErrorMessage("Bugemon not present in the Team");
			return;
		}

		Ability oldAbility = chosenBugemon.getAbilities().getAbilityById(message.getOldAbility().getId());
		if (oldAbility == null){
			sendErrorMessage("Ability not learned");
			return;
		}
		Ability newAbility = AbilityMapper.toEntity(message.getNewAbility());

		chosenBugemon.swapAbility(newAbility, oldAbility);

		towerManager.getCurrentRoomManager().setRoomCompleted(true);
		nextTowerRoom();
		sendSuccessMessage();
	}

	@Override
	public void handle(ChooseItemRewardMessage message){
		Item item = ItemMapper.toEntity(message.getItem());
		player.getInventory().addItem(item, 1);

		towerManager.getCurrentRoomManager().setRoomCompleted(true);

		nextTowerRoom();
		sendSuccessMessage();
	}

	@Override
	public void handle(ChooseStatRewardMessage message){
		BugemonDTO bugemonDTO = message.getBugemon();
		Bugemon chosenBugemon = player.getTeam().getBugemonById(bugemonDTO.getId());

		if (chosenBugemon == null){
			sendErrorMessage("Bugemon not present in the Team");
			return;
		}

		Reward reward = new Reward(chosenBugemon);
		reward.configureReward(RewardType.COMBINATION);

		chosenBugemon.changeBaseStats(reward.getStats());
		chosenBugemon.changeFightStats(reward.getStats());

		towerManager.getCurrentRoomManager().setRoomCompleted(true);
		nextTowerRoom();
		sendSuccessMessage();
	}

	@Override
	public void handle(ChooseLevelUpRewardMessage message){
		if (this.pendingLevelUpRewards == null || this.pendingLevelUpRewards.isEmpty()) {
			sendErrorMessage("No pending level up reward to apply");
			return;
		}

		RewardDTO rewardDTO = message.getReward();
		if (rewardDTO == null) {
			sendErrorMessage("Invalid reward");
			return;
		}

		Reward chosenReward = RewardMapper.toEntity(rewardDTO);
		boolean applied = false;
		for (Reward reward : this.pendingLevelUpRewards) {
			if (reward.getStats().equals(chosenReward.getStats())) {
				reward.applyReward();
				applied = true;
				break;
			}
		}

		if (!applied) {
			sendErrorMessage("Reward does not match the current level up choices");
			return;
		}

		clearPendingLevelUpState();
		sendSuccessMessage();
	}
	
	// SPECIAL INFO

	@Override
	public void handle(GetAllBugemonSpeciesMessage message){
		BugemonService bugemonService = this.getBugemonService();
		List<BugemonSpeciesDTO> DTOSpeciesList = new ArrayList<BugemonSpeciesDTO>();

		for (BugemonSpecies species : bugemonService.getAllSpecies()){
			DTOSpeciesList.add(BugemonSpeciesMapper.toDTO(species));
		}
		this.sendMessage(new BugemonSpeciesMessage(DTOSpeciesList));
	}

	@Override
	public void handle(SendFriendRequestMessage message){
		AccountService accountService = this.getAccountService();
		int senderId = accountService.getUserId(message.getSenderUsername());
		int receiverId = accountService.getUserId(message.getReceiverUsername());
		if (senderId == -1 || receiverId == -1){
			sendErrorMessage("Utilisateur introuvable");
			return;
		}
		accountService.sendFriendRequest(senderId, receiverId);
		sendSuccessMessage();
	}

	@Override
	public void handle(GetFriendRequestsMessage message){
		AccountService accountService = this.getAccountService();
		int userId = accountService.getUserId(message.getUsername());
		List<String> requests = accountService.getPendingRequests(userId);
		sendMessage(new FriendRequestsMessage(requests));
	}

	@Override
	public void handle(AcceptFriendRequestMessage message){
		AccountService accountService = this.getAccountService();
		int senderId = accountService.getUserId(message.getSenderUsername());
		int receiverId = accountService.getUserId(message.getReceiverUsername());
		accountService.acceptFriendRequest(senderId, receiverId);
		sendSuccessMessage();
	}

	@Override
	public void handle(DeclineFriendRequestMessage message){
		AccountService accountService = this.getAccountService();
		int senderId = accountService.getUserId(message.getSenderUsername());
		int receiverId = accountService.getUserId(message.getReceiverUsername());
		accountService.declineFriendRequest(senderId, receiverId);
		sendSuccessMessage();
	}

	@Override
	public void handle(GetFriendsListMessage message){
		AccountService accountService = this.getAccountService();
		int userId = accountService.getUserId(message.getUsername());
		List<String> friends = accountService.getFriendsList(userId);
		sendMessage(new FriendsListMessage(friends));
	}

	@Override
	public void handle(SendChatMessageMessage message){
		this.getChatService().sendMessage(message.getSenderUsername(), message.getReceiverUsername(), message.getContent());
		sendSuccessMessage();
	}

	@Override
	public void handle(GetChatMessagesMessage message){
		sendMessage(new ChatMessagesMessage(this.getChatService().getMessages(message.getUsernameA(), message.getUsernameB())));
	}

	@Override
	public void handle(GetRandomAbilityMessage message){
		Bugemon bugemon = BugemonMapper.toEntity(message.getBugemon());

		Ability RandomAbility = this.getAbilityService().getRandomAbility(bugemon.getType(), bugemon.getAbilities());

		sendMessage(new RandomAbilityMessage(AbilityMapper.toDTO(RandomAbility)));
	}

	@Override
	public void handle(GetRandomItemMessage message){
		Item randomItem = this.getItemService().getRandomItem();

		sendMessage(new RandomItemMessage(ItemMapper.toDTO(randomItem)));
	}

	// ACCOUNT

	@Override
	public void handle(GetUserIdFromNameMessage message) {
		String name = message.getName();
		int id = this.getAccountService().getUserId(name);
		UserIdMessage response = new UserIdMessage(id);
		sendMessage(response);
	}

	/**
	 * Reads the socket and handle the received message
	 */
    private void handleMessage(){
        ClientToServerMessage message = receiveMessage();
        
        if (message == null){
            return;
        } 
		
		message.dispatch(this);
    }
}
