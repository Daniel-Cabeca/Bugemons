package ulb.controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ulb.DTO.bugemon.BugemonSpeciesDTO;
import ulb.communication.SocketClient;
import ulb.communication.types.GameMode;
import ulb.message.ClientToServerMessage;
import ulb.message.clientToServer.*;
import ulb.message.serverToClient.*;
import ulb.message.serverToClient.NextWindowMessage.WindowType;
import ulb.model.battle.BattleState;
import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.DTO.item.ItemDTO;
import ulb.DTO.reward.RewardDTO;
import ulb.mapper.bugemon.BugemonMapper;
import ulb.mapper.reward.RewardMapper;
import ulb.model.bugemon.Bugemon;
import ulb.model.reward.Reward;
import ulb.repository.LoadException;
import ulb.model.chat.ChatMessage;
import ulb.view.WindowPath;
import ulb.view.windows.SocialPanel;


public class ClientController extends Application implements RegisterController.Listener, ModeController.Listener,
BattleModeController.Listener,BattleEndController.Listener, BattleWindowController.Listener, NextRoomController.Listener, 
FloorRewardController.Listener, AttackReplacementController.Listener, TeamController.Listener, LevelUpController.Listener {
    SocketClient client;
    Stage stage;

	PlayerDTO player;
	GameMode gameMode;
	BattleController battleController;

	RegisterController registerController;
    ModeController modeController;
	TeamController teamController;
	BattleModeController battleModeController;
	BattleWindowController battleWindowController;

	BattleEndController battleEndController;
	LevelUpController levelUpController;
	NextRoomController nextRoomController;
	FloorRewardController floorRewardController;
	ChooseBugemonController chooseBugemonController;
	AttackReplacementController attackReplacementController;

	FloorRewardController.RewardChoice pendingFloorRewardChoice;
	BugemonDTO pendingLevelUpBugemon;
	List<RewardDTO> pendingLevelUpRewards;

    @Override
    public void init(){
        List<String> params = getParameters().getRaw();

        String serverIp = params.get(0);
        Integer serverPort = Integer.parseInt(params.get(1));
        
        this.client = new SocketClient(serverIp, serverPort);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
		
		this.stage = primaryStage;

        Font.loadFont(getClass().getResourceAsStream("/fonts/pokemon-emerald-pro.otf"), 14);
		primaryStage.setTitle("INFO-F307 Groupe 10");
		primaryStage.setFullScreen(true);
		primaryStage.setFullScreenExitHint("");

		this.registerController = new RegisterController(this.stage, this);
		this.registerController.show();

		if (primaryStage.getScene() != null) {
			String stylesheet = getClass().getResource("/styles/global.css").toExternalForm();
			if (!primaryStage.getScene().getStylesheets().contains(stylesheet)) {
				primaryStage.getScene().getStylesheets().add(stylesheet);
			}
		}

		primaryStage.show();
    }

	public boolean postData(ClientToServerMessage message){
		client.sendMessage(message);
		if (client.receiveMessage() instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			System.err.println(errorMessage.getMessage());
			return false;
		}
		return true;
	}

	public Serializable getData(ClientToServerMessage message){
		client.sendMessage(message);
		return client.receiveMessage();
	}

	public PlayerDTO getPlayer() { return this.player; }

	public boolean logIn(PlayerDTO player){
		return postData(new RegisterMessage(player, true));
	}

	public boolean sendFriendRequest(String receiver) {
		return postData(new SendFriendRequestMessage(player.getName(), receiver));
	}

	public List<String> getFriendRequests() {
		if (getData(new GetFriendRequestsMessage(player.getName())) instanceof FriendRequestsMessage msg)
			return msg.getRequests();
		return List.of();
	}

	public boolean acceptFriendRequest(String sender) {
		return postData(new AcceptFriendRequestMessage(player.getName(), sender));
	}

	public boolean declineFriendRequest(String sender) {
		return postData(new DeclineFriendRequestMessage(player.getName(), sender));
	}

	public void sendChatMessage(String receiver, String content) {
		postData(new SendChatMessageMessage(player.getName(), receiver, content));
	}

	public List<ChatMessage> getChatMessages(String friend) {
		if (getData(new GetChatMessagesMessage(player.getName(), friend)) instanceof ChatMessagesMessage msg)
			return msg.getMessages();
		return List.of();
	}

	public List<String> getFriendsList() {
		if (getData(new GetFriendsListMessage(player.getName())) instanceof FriendsListMessage msg)
			return msg.getFriends();
		return List.of();
	}

	public boolean signUp(PlayerDTO player){
		return postData(new RegisterMessage(player, false));
	}

	// Register Controller :

	@Override
	public void onLogin(String username, String password){
		try {
			this.player = new PlayerDTO(username, password, new ArrayList<>(), new HashMap<>());
			boolean success = logIn(this.player);
			if (success) {
				this.modeController = new ModeController(this.stage, this);
				try {
					this.modeController.show();
				}catch (Exception e){
					e.printStackTrace();
				}
			} else {
				this.registerController.getView().setErrorLabel("Nom d'utilisateur ou mot de passe incorrect.");
			}
		} catch (LoadException e) {
			this.registerController.getView().setErrorLabel("Erreur de connexion à la base de données.");
		}
	}

	@Override
	public void onSignUp(String username, String password){
		try {
			this.player = new PlayerDTO(username, password, new ArrayList<>(), new HashMap<>());
			boolean success = this.signUp(this.player);
			if (success) {
				this.modeController = new ModeController(this.stage, this);
				try {
					this.modeController.show();
				}catch (Exception e){
					e.printStackTrace();
				}
			} else {
				this.registerController.getView().setErrorLabel("Nom d'utilisateur ou mot de passe incorrect.");
			}
		} catch (LoadException e) {
			this.registerController.getView().setErrorLabel("Nom d'utilisateur ou mot de passe incorrect.");
		}
	}


	// Mode Controller Listener :

	@Override
	public void onOpenSocial() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.SOCIAL_PANEL));
			Parent root = loader.load();
			SocialPanel panel = loader.getController();
			Stage popup = new Stage();
			popup.initStyle(StageStyle.UNDECORATED);
			popup.initOwner(stage);
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/styles/global.css").toExternalForm());
			popup.setScene(scene);
			panel.setStage(popup);
			popup.setX(stage.getX());
			popup.setY(stage.getY());
			popup.show();
			panel.setClientController(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onSolo() {
		this.teamController = new TeamController(this.stage, this, this.player);
		try {
			this.teamController.show();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void onMultiplayer() {

	}

	// Team Controller Listener :


	@Override
	public void onReturnToMode() {
		try {
			modeController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<BugemonSpeciesDTO> getAllSpecies(){
		Serializable message = this.getData(new GetAllBugemonSpeciesMessage());

		if (message instanceof BugemonSpeciesMessage speciesMessage){
			return speciesMessage.getSpecies();
		}
		return null;
	}

	@Override
	public void onTeamConfirmed() {
		List<BugemonDTO> team = player.getTeam();

		if (!this.postData(new SetUpTeamMessage(team))){
			return;
		}

		this.battleModeController = new BattleModeController(this.stage, this, player.getTeam());
		try {
			battleModeController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// BattleEndController

	@Override
	public void onHandleReturn() {
		switchToModeWindow();
	}


	// Battle Mode Controller Listener : 

	// private BattleController getCurrentBattleController() {
	// 	if (gameMode == GameMode.TOWER && towerModeTowerManager != null) {
	// 		return towerModeTowerManager.getCurrentBattleController();
	// 	}
	// 	return normalModeBattleController;
	// }

	private void switchToModeWindow(){
		try {
			this.modeController.show();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private void switchToNextRoomWindow(boolean hasFled){
		this.nextRoomController = new NextRoomController(stage, this);
		try{
			this.nextRoomController.show(hasFled);
		} catch (Exception e){
			System.err.println(e);
		}
		
	}

	private void switchToBattleWindow() {
		int towerFloorNumber = 0, towerRoomNumber = 0;
		if (this.gameMode == GameMode.TOWER){
			List<Integer> towerInfo;
			if ((towerInfo = this.getTowerInfo()) != null){
				towerFloorNumber = towerInfo.get(0);
				towerRoomNumber = towerInfo.get(1);
			}
		}
		battleWindowController = new BattleWindowController(
				this.stage,
				this,
				player,
				gameMode,
				towerFloorNumber,
				towerRoomNumber
		);

		try {
			battleWindowController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void switchToBattleEndWindow(){
		Serializable message = getData(new GetBattleEndInfoMessage());
		boolean victory = false;
		int totaleXp = 0;
		if (message instanceof BattleEndInfoMessage battleInfo){
			victory = battleInfo.isVictory();
			totaleXp = battleInfo.getTotalXp();
		} else {
			return;
		}

		this.battleEndController = new BattleEndController(stage, this);
		try {
			battleEndController.show(victory, totaleXp);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	private void switchToLevelUpWindow(){
		Serializable message = getData(new GetLevelUpInfoMessage());
		if (!(message instanceof LevelUpInfoMessage levelUpInfo)) {
			return;
		}

		this.pendingLevelUpBugemon = levelUpInfo.getBugemon();
		this.pendingLevelUpRewards = levelUpInfo.getRewards();
		this.levelUpController = new LevelUpController(stage, this);
		try {
			this.levelUpController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void switchToTowerRewardWindow(){
		this.floorRewardController = new FloorRewardController(stage, this);
		try{
			floorRewardController.show();
		} catch (Exception e){
			System.err.println(e);
		}
	}


	public void nextRoom(){
		WindowType nextWindow = this.getWindowType();
		switch (nextWindow) {
			case NEXT_ROOM:
				switchToNextRoomWindow(false);
				break;

			case GAME:
				switchToBattleWindow();
				break;
			
			case LEVEL_UP:
				switchToLevelUpWindow();
				break;
			
			case REWARD:
				switchToTowerRewardWindow();
				break;
			
			case MAIN_MENU:
				switchToBattleEndWindow();
				break;

			default:
				break;
		}
		
	}

	@Override
	public void onAutoBattle() {
		this.gameMode = GameMode.AUTO;
		if (this.postData(new SetUpNormalModeMessage())){
			switchToBattleWindow();
		}
	}

	@Override
	public void onControlledBattle() {
		this.gameMode = GameMode.CONTROLLED;
		if (this.postData(new SetUpNormalModeMessage())){
			switchToBattleWindow();
		}
	}

	@Override
	public void onTowerMode() {
		this.gameMode = GameMode.TOWER;
		if (this.postData(new SetUpTowerModeMessage())){
			switchToBattleWindow();
		}
	}

	@Override
	public void onReturnToCreateTeamWindow() {
		try {
			teamController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Battle Window Controller Listener : 

	@Override
	public void onBattleStateChecked(BattleState state, ActionEvent event) {
		//CLIENT
		if (state != BattleState.WON && state != BattleState.LOST){
			return;
		}

		nextRoom();
		//handleBattleEndCheckMessage(new BattleEndCheckMessage(state, event))
	}
	
	@Override
	public List<BugemonDTO> getActiveBugemons(){
		Serializable message = getData(new GetActiveBugemonsMessage());
		if (message instanceof ActiveBugemonsMessage activeBugemons){
			return List.of(activeBugemons.getSelfActiveBugemon(), activeBugemons.getOpponentActiveBugemon());
			
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			System.err.println(errorMessage.getMessage());
		}
		return null;
	}

	@Override
	public Map<AbilityDTO, String> getAbilityEffectiveness(List<AbilityDTO> abilities, BugemonDTO bugemonTarget){
		Serializable message = getData(new GetAbilityEffectivenessMessage(abilities, bugemonTarget));

		if (message instanceof AbilityEffectivenessMessage effectivenessMessage){
			return effectivenessMessage.getEffectiveness();
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			System.err.println(errorMessage.getMessage());
		}
		return null;
	}

	@Override
	public List<Integer> getHpAfterFirstAction(){
		Serializable message = getData(new GetLogsMessage(false));

		if (message instanceof LogsMessage logs){
			return logs.getHpsAfterFirstAction();
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			System.err.println(errorMessage.getMessage());
		}
		return null;
	}

	@Override
	public BattleState getState(){
		Serializable message = getData(new GetBattleStateMessage());

		if (message instanceof BattleStateMessage battleState){
			System.out.println("State  = " + battleState.getBattleState());
			return battleState.getBattleState();
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			System.err.println(errorMessage.getMessage());
		}
		return null;

	}

	@Override
	public List<String> getLogs(){
		Serializable message = getData(new GetLogsMessage(true));

		if (message instanceof LogsMessage logs){
			return logs.getLogs();
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			System.err.println(errorMessage.getMessage());
		}
		return null;
	}

	@Override
	public Map<ItemDTO, Boolean> checkItems(List<ItemDTO> items){
		Serializable message = getData(new CheckUsableItemMessage(items));

		if (message instanceof UsableItemsMessage usableItems){
			return usableItems.getItemMap();
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			System.err.println(errorMessage.getMessage());
		}
		return null;
	}

	@Override
	public boolean isGameFinished(){
		Serializable message = getData(new CheckGameFinishedMessage());

		if (message instanceof GameFinishedMessage gameFinished){
			return gameFinished.isGameFinished();
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			System.err.println(errorMessage.getMessage());
		}
		return true;
	}

	@Override
	public BattleState onAutoTurn() {
		// CLIENT
		if (!postData(new PickRandomActionMessage())){
			return null;
		}
		return getState();
	}

	@Override
	public BattleState onUseItem(ItemDTO item) {
		if (!postData(new UseItemMessage(item))){
			return null;
		}
		return getState();
	}

	@Override
	public BattleState onSwapBugemon(BugemonDTO bugemon) {
		if (!postData(new SwapBugemonMessage(bugemon))){
			return null;
		}
		return getState();
	}

	@Override
	public BattleState onUseAbility(AbilityDTO ability) {
		if (!postData(new UseAbilityMessage(ability))){
			return null;
		}
		return getState();
	}

	@Override
	public void onRun() {
		if (postData(new RunMessage())){
			if (this.gameMode == GameMode.TOWER){
				switchToNextRoomWindow(true);
			} else {
				nextRoom();
			}
		}
	}

	public WindowType getWindowType(){
		Serializable message = getData(new GetNextWindowMessage());

		if (message instanceof NextWindowMessage nextWindow){
			return nextWindow.getNextWindow();
		}

		return null;
	}

	@Override
	public List<RewardDTO> getLevelUpRewards() {

		Serializable message = getData(new GetLevelUpInfoMessage());
		List<RewardDTO> rewards = null;

		if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			System.err.println(errorMessage.getMessage());
			
		} else if (message instanceof LevelUpInfoMessage levelUpInfo){
			rewards = levelUpInfo.getRewards();
		}
		return rewards;
	}

	@Override
	public void onRewardChosen(RewardDTO reward, ActionEvent event) {
		if (postData(new ChooseLevelUpRewardMessage(reward))) {
			nextRoom();
		}
	}

	// Next Room Listener 

	@Override
	public void onContinue() {
		nextRoom();
	}

	@Override
	public void onReturn() {
		try {
			this.modeController.show();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	// Floor Reward Listener

	@Override
	public void onObjectReward(ItemDTO rewardItem) {
		if (postData(new ChooseItemRewardMessage(rewardItem))){
			nextRoom(); // MAYBE
		}
		// player.getInventory().addItem(rewardItem, 1);
		// switchToNextRoomWindow();
	}

	@Override
	public void onChooseBugemonReward(FloorRewardController.RewardChoice rewardChoice) {
		pendingFloorRewardChoice = rewardChoice;
		if (chooseBugemonController == null) {
			chooseBugemonController = new ChooseBugemonController(this.stage, this.floorRewardController, this.player);
		}
		try {
			chooseBugemonController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBugemonChosen(BugemonDTO bugemon) {
		if (pendingFloorRewardChoice == FloorRewardController.RewardChoice.STAT) {
			if (postData(new ChooseStatRewardMessage(bugemon))){
				nextRoom();
			}
			// Reward reward = new Reward(bugemon);
			// reward.configureReward(RewardType.COMBINATION);
			// bugemon.changeBaseStats(reward.getStats());
			// bugemon.changeFightStats(reward.getStats());
			// switchToNextRoomWindow();
			return;
		}
		AbilityDTO newAbility = null;
		Serializable message = getData(new GetRandomAbilityMessage(bugemon));
		if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			System.err.println(errorMessage.getMessage());
			return; 
		}else if (message instanceof RandomAbilityMessage randomAbility){
			newAbility = randomAbility.getAbility();
		}

		// Ability newAbility = ServiceLoader.getAbilityService().getRandomAbility(bugemon.getType(), bugemon.getAbilities());
		if (newAbility == null) {
			nextRoom();
			//switchToNextRoomWindow();
			return;
		}

		if (attackReplacementController == null) {
			attackReplacementController = new AttackReplacementController(stage, this);
		}
		try {
			attackReplacementController.show(bugemon, newAbility);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onReturnFloorRewardWindow() {
		if (floorRewardController == null) {
			floorRewardController = new FloorRewardController(stage, this);
		}
		try {
			floorRewardController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ItemDTO getRandomItem() {
		if (getData(new GetRandomItemMessage()) instanceof RandomItemMessage randomItem){
			return randomItem.getItem();
		}
		return null;
	}

	@Override
	public List<Integer> getTowerInfo() {
		if (getData(new GetTowerInfoMessage()) instanceof TowerInfoMessage towerInfo){
			return List.of(towerInfo.getFloorNumber(), towerInfo.getRoomNumber());
		}
		return null;
	}

	// Attack Replacement Controller Listener

	@Override
	public void onAttackReplaced(BugemonDTO bugemon, AbilityDTO newAbility, AbilityDTO oldAbility) {
		if (postData(new ChooseAbilityRewardMessage(bugemon, oldAbility, newAbility))){
			nextRoom();
		}
		// bugemon.swapAbility(newAbility, oldAbility);
		// switchToNextRoomWindow();
	}

    @Override
	public void onReturnToChooseBugemon() {
		if (chooseBugemonController == null) {
			return;
		}
		try {
			chooseBugemonController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
