package ulb.server;

import ulb.message.clientToServer.*;

/**
 * Interface for classes that receive messages on the server side.
 */
 public interface ServerMessageHandler {
	// SETUP 
	 void handle(RegisterMessage message);
	 void handle(SetUpTeamMessage message);
	 void handle(SetUpNormalModeMessage message);
	 void handle(SetUpTowerModeMessage message);
	 void handle(SaveTeamMessage saveTeamMessage);

	// GAME INFO
	 void handle(GetPlayerMessage message);
	 void handle(GetPlayerInventory message);
	 void handle(CheckGameFinishedMessage message);
	 void handle(GetBattleStateMessage message);
	 void handle(GetLogsMessage message);
	 void handle(CheckUsableItemMessage message);
	 void handle(GetAbilityEffectivenessMessage message);
	 void handle(GetActiveBugemonsMessage message);
	 void handle(GetTowerInfoMessage message);
	 void handle(GetNextWindowMessage message);
	 void handle(GetBattleEndInfoMessage message);
	 void handle(GetLevelUpInfoMessage message);
	 void handle(GetPlayerTeamMessage message);

	//ACTIONS
	 void handle(PickRandomActionMessage message);
	 void handle(RunMessage message);
	 void handle(SwapBugemonMessage message);
	 void handle(UseAbilityMessage message);
	 void handle(UseItemMessage message);
	 void handle(ChooseAbilityRewardMessage message);
	 void handle(ChooseItemRewardMessage message);
	 void handle(ChooseStatRewardMessage message);
	 void handle(ChooseLevelUpRewardMessage message);
	
	// SPECIAL INFO
	 void handle(GetAllBugemonSpeciesMessage message);
	 void handle(GetRandomAbilityMessage message);
	 void handle(GetRandomItemMessage message);
	 void handle(GetSavedTeamsMessage getSavedTeamsMessage);

	// ACCOUNT
	 void handle(GetUserIdFromNameMessage message);

	// FRIENDS
	 void handle(SendFriendRequestMessage message);
	 void handle(GetFriendRequestsMessage message);
	 void handle(AcceptFriendRequestMessage message);
	 void handle(DeclineFriendRequestMessage message);
	 void handle(GetFriendsListMessage message);

	// BATTLE
	 void handle(SendBattleRequestMessage message);
	 void handle(GetBattleRequestsMessage message);
	 void handle(AcceptBattleRequestMessage message);
	 void handle(DeclineBattleRequestMessage message);

	// CHAT
	 void handle(SendChatMessageMessage message);
	 void handle(GetChatMessagesMessage message);
}
