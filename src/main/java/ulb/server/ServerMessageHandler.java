package ulb.server;

import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.item.ItemDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.DTO.player.PlayerRegisterDTO;
import ulb.DTO.reward.RewardDTO;
import ulb.DTO.team.TeamDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;

import java.util.List;

/**
 * Interface for classes that receive messages on the server side.
 */
 public interface ServerMessageHandler {
	// SETUP 
	 void setupMultiBattle(PlayerDTO opponent, List<BugemonDTO> bugemons) throws UserFacingException, DataAccessException;
	 void registerPlayer(PlayerRegisterDTO playerRegisterDTO, boolean isLogin) throws UserFacingException, DataAccessException;
	 void setupNormalMode() throws UserFacingException, DataAccessException;
	 void setupTeam(List<BugemonDTO> bugemons) throws UserFacingException, DataAccessException;
	 void setupTowerMode(boolean isNewTower) throws UserFacingException, DataAccessException;

	 // PLAYER INFO
	 void getPlayerInfo(String username) throws UserFacingException, DataAccessException;
	 void getPlayerInventory(String username) throws UserFacingException, DataAccessException;
	 void getPlayerTeam() throws UserFacingException, DataAccessException;

	// GAME INFO
	 void checkGameFinished() throws UserFacingException, DataAccessException;
	 void checkUsableItems(List<ItemDTO> items) throws UserFacingException, DataAccessException;
	 void getAbilityEffectiveness(BugemonDTO bugemonDTO, List<AbilityDTO> abilities) throws UserFacingException, DataAccessException;
	 void getActiveBugemons() throws UserFacingException, DataAccessException;
	 void getBattleEndInfo() throws UserFacingException, DataAccessException;
	 void getBattleState() throws UserFacingException, DataAccessException;
	 void getLevelUpInfo() throws UserFacingException, DataAccessException;
	 void getLogs(boolean clearLogs) throws UserFacingException, DataAccessException;
	 void getNextWindow() throws UserFacingException, DataAccessException;
	 void getTowerInfo() throws UserFacingException, DataAccessException;
	 void getTowerSavedInfo() throws UserFacingException, DataAccessException;

	// GAME ACTIONS
	 void abandonTower() throws UserFacingException, DataAccessException;
	 void chooseAbilityReward(BugemonDTO bugemonDTO, AbilityDTO oldAbilityDTO, AbilityDTO newAbilityDTO) throws UserFacingException, DataAccessException;
	 void chooseItemReward(ItemDTO itemDTO) throws UserFacingException, DataAccessException;
	 void chooseLevelUpReward(RewardDTO rewardDTO) throws UserFacingException, DataAccessException;
	 void chooseStatReward(BugemonDTO bugemonDTO) throws UserFacingException, DataAccessException;
	 void chooseTowerRoom(int roomId) throws UserFacingException, DataAccessException;
	 void chooseRandomAction() throws UserFacingException, DataAccessException;
	 void chooseRunAction() throws UserFacingException, DataAccessException;
	 void startMultiBattle(PlayerDTO opponentDTO) throws UserFacingException, DataAccessException;
	 void quitMultiBattle(PlayerDTO opponentDTO) throws UserFacingException, DataAccessException;
	 void chooseSwapBugemonAction(BugemonDTO bugemonDTOToSwap) throws UserFacingException, DataAccessException;
	 void chooseUseAbilityAction(AbilityDTO abilityDTO) throws UserFacingException, DataAccessException;
	 void chooseUseItemAction(ItemDTO itemDTO) throws UserFacingException, DataAccessException;

	// GAME DATA
	 void getAllBugemonSpecies() throws UserFacingException, DataAccessException;
	 void getRandomAbility(BugemonDTO bugemonDTO) throws UserFacingException, DataAccessException;
	 void getRandomItem() throws UserFacingException, DataAccessException;

	// SOCIAL
	 void acceptBattleRequest(String senderUsername, String receiverUsername) throws UserFacingException, DataAccessException;
	 void acceptFriendRequest(String senderUsername, String receiverUsername) throws UserFacingException, DataAccessException;
	 void declineBattleRequest(String senderUsername, String receiverUsername) throws UserFacingException, DataAccessException;
	 void declineFriendRequest(String senderUsername, String receiverUsername) throws UserFacingException, DataAccessException;
	 void getBattleRequests(String username) throws UserFacingException, DataAccessException;
	 void getMultiBattleStatus(int userId1, int userId2) throws UserFacingException, DataAccessException;
	 void getChatMessages(String usernameA, String usernameB) throws UserFacingException, DataAccessException;
	 void getFriendRequests(String username) throws UserFacingException, DataAccessException;
	 void getFriendsList(String username) throws UserFacingException, DataAccessException;
	 void getLeaderboard() throws UserFacingException, DataAccessException;
	 void sendBattleRequest(String senderUsername, String receiverUsername) throws UserFacingException, DataAccessException;
	 void sendChatMessage(String senderUsername, String receiverUsername, String content) throws UserFacingException, DataAccessException;
	 void sendFriendRequest(String senderUsername, String receiverUsername) throws UserFacingException, DataAccessException;

	 // TEAM SAVE
	 void getSavedTeams() throws UserFacingException, DataAccessException;
	 void saveTeam(TeamDTO teamDTO) throws UserFacingException, DataAccessException;
}
