package ulb.server;

import ulb.message.clientToServer.*;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;

/**
 * Interface for classes that receive messages on the server side.
 */
 public interface ServerMessageHandler {
	// SETUP 
	 void handle(ConfirmTeamMultiMessage message) throws UserFacingException, DataAccessException;
	 void handle(RegisterMessage message) throws UserFacingException, DataAccessException;
	 void handle(SetUpNormalModeMessage message) throws UserFacingException, DataAccessException;
	 void handle(SetUpTeamMessage message) throws UserFacingException, DataAccessException;
	 void handle(SetUpTowerModeMessage message) throws UserFacingException, DataAccessException;

	 // PLAYER INFO
	 void handle(GetPlayerMessage message) throws UserFacingException, DataAccessException;
	 void handle(GetPlayerInventoryMessage message) throws UserFacingException, DataAccessException;
	 void handle(GetPlayerTeamMessage message) throws UserFacingException, DataAccessException;
	 void handle(GetUserIdFromNameMessage message) throws UserFacingException, DataAccessException;

	// GAME INFO
	 void handle(CheckGameFinishedMessage message) throws UserFacingException, DataAccessException;
	 void handle(CheckUsableItemMessage message) throws UserFacingException, DataAccessException;
	 void handle(GetAbilityEffectivenessMessage message) throws UserFacingException, DataAccessException;
	 void handle(GetActiveBugemonsMessage message) throws UserFacingException, DataAccessException;
	 void handle(GetBattleEndInfoMessage message) throws UserFacingException, DataAccessException;
	 void handle(GetBattleStateMessage message) throws UserFacingException, DataAccessException;
	 void handle(GetLevelUpInfoMessage message) throws UserFacingException, DataAccessException;
	 void handle(GetLogsMessage message) throws UserFacingException, DataAccessException;
	 void handle(GetNextWindowMessage message) throws UserFacingException, DataAccessException;
	 void handle(GetTowerInfoMessage message) throws UserFacingException, DataAccessException;
	 void handle(GetTowerSavedInfoMessage message) throws UserFacingException, DataAccessException;

	// GAME ACTIONS
	 void handle(AbandonTowerMessage message) throws UserFacingException, DataAccessException;
	 void handle(ChooseAbilityRewardMessage message) throws UserFacingException, DataAccessException;
	 void handle(ChooseItemRewardMessage message) throws UserFacingException, DataAccessException;
	 void handle(ChooseLevelUpRewardMessage message) throws UserFacingException, DataAccessException;
	 void handle(ChooseStatRewardMessage message) throws UserFacingException, DataAccessException;
	 void handle(ChooseTowerRoomMessage message) throws UserFacingException, DataAccessException;
	 void handle(PickRandomActionMessage message) throws UserFacingException, DataAccessException;
	 void handle(RunMessage message) throws UserFacingException, DataAccessException;
	 void handle(StartMultiBattleMessage message) throws UserFacingException, DataAccessException;
	 void handle(SwapBugemonMessage message) throws UserFacingException, DataAccessException;
	 void handle(UseAbilityMessage message) throws UserFacingException, DataAccessException;
	 void handle(UseItemMessage message) throws UserFacingException, DataAccessException;

	// GAME DATA
	 void handle(GetAllBugemonSpeciesMessage message) throws UserFacingException, DataAccessException;
	 void handle(GetRandomAbilityMessage message) throws UserFacingException, DataAccessException;
	 void handle(GetRandomItemMessage message) throws UserFacingException, DataAccessException;

	// SOCIAL
	 void handle(AcceptBattleRequestMessage message) throws UserFacingException, DataAccessException;
	 void handle(AcceptFriendRequestMessage message) throws UserFacingException, DataAccessException;
	 void handle(DeclineBattleRequestMessage message) throws UserFacingException, DataAccessException;
	 void handle(DeclineFriendRequestMessage message) throws UserFacingException, DataAccessException;
	 void handle(GetBattleRequestsMessage message) throws UserFacingException, DataAccessException;
	 void handle(GetMultiBattleStatusMessage message) throws UserFacingException, DataAccessException;
	 void handle(GetChatMessagesMessage message) throws UserFacingException, DataAccessException;
	 void handle(GetFriendRequestsMessage message) throws UserFacingException, DataAccessException;
	 void handle(GetFriendsListMessage message) throws UserFacingException, DataAccessException;
	 void handle(GetLeaderboardMessage message) throws UserFacingException, DataAccessException;
	 void handle(SendBattleRequestMessage message) throws UserFacingException, DataAccessException;
	 void handle(SendChatMessageMessage message) throws UserFacingException, DataAccessException;
	 void handle(SendFriendRequestMessage message) throws UserFacingException, DataAccessException;

	 // TEAM SAVE
	 void handle(GetSavedTeamsMessage message) throws UserFacingException, DataAccessException;
	 void handle(SaveTeamMessage message) throws UserFacingException, DataAccessException;
}
