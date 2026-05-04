package ulb.server;

import ulb.message.clientToServer.*;

/**
 * Interface for classes that receive messages on the server side.
 */
 public interface ServerMessageHandler {
	// SETUP 
	 void handle(RegisterMessage message);
	 void handle(SetUpNormalModeMessage message);
	 void handle(SetUpTeamMessage message);
	 void handle(SetUpTowerModeMessage message);

	 // PLAYER INFO
	 void handle(GetPlayerMessage message);
	 void handle(GetPlayerInventoryMessage message);
	 void handle(GetPlayerTeamMessage message);
	 void handle(GetUserIdFromNameMessage message);

	// GAME INFO
	 void handle(CheckGameFinishedMessage message);
	 void handle(CheckUsableItemMessage message);
	 void handle(GetAbilityEffectivenessMessage message);
	 void handle(GetActiveBugemonsMessage message);
	 void handle(GetBattleEndInfoMessage message);
	 void handle(GetBattleStateMessage message);
	 void handle(GetLevelUpInfoMessage message);
	 void handle(GetLogsMessage message);
	 void handle(GetNextWindowMessage message);
	 void handle(GetTowerInfoMessage message);

	// GAME ACTIONS
	 void handle(AbandonTowerMessage message);
	 void handle(ChooseAbilityRewardMessage message);
	 void handle(ChooseItemRewardMessage message);
	 void handle(ChooseLevelUpRewardMessage message);
	 void handle(ChooseStatRewardMessage message);
	 void handle(ChooseTowerRoomMessage message);
	 void handle(PickRandomActionMessage message);
	 void handle(RunMessage message);
	 void handle(StartMultiBattleMessage message);
	 void handle(SwapBugemonMessage message);
	 void handle(UseAbilityMessage message);
	 void handle(UseItemMessage message);

	// GAME DATA
	 void handle(GetAllBugemonSpeciesMessage message);
	 void handle(GetRandomAbilityMessage message);
	 void handle(GetRandomItemMessage message);

	// SOCIAL
	 void handle(AcceptBattleRequestMessage message);
	 void handle(AcceptFriendRequestMessage message);
	 void handle(DeclineBattleRequestMessage message);
	 void handle(DeclineFriendRequestMessage message);
	 void handle(GetBattleRequestsMessage message);
	 void handle(GetMultiBattleStatusMessage message);
	 void handle(GetChatMessagesMessage message);
	 void handle(GetFriendRequestsMessage message);
	 void handle(GetFriendsListMessage message);
	 void handle(GetLeaderboardMessage message);
	 void handle(SendBattleRequestMessage message);
	 void handle(SendChatMessageMessage message);
	 void handle(SendFriendRequestMessage message);

	 // TEAM SAVE
	 void handle(ConfirmTeamMultiMessage message);
	 void handle(GetSavedTeamsMessage message);
	 void handle(SaveTeamMessage message);
}
