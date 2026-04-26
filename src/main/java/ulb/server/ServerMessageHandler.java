package ulb.server;

import ulb.message.clientToServer.*;

/**
 * Interface for classes that receive messages on the server side.
 */
public interface ServerMessageHandler {
	// SETUP 
	public void handle(RegisterMessage message);
	public void handle(SetUpTeamMessage message);
	public void handle(SetUpNormalModeMessage message);
	public void handle(SetUpTowerModeMessage message);

	// GAME INFO
	public void handle(GetPlayerMessage message);
	public void handle(GetPlayerInventory message);
	public void handle(CheckGameFinishedMessage message);
	public void handle(GetBattleStateMessage message);
	public void handle(GetLogsMessage message);
	public void handle(CheckUsableItemMessage message);
	public void handle(GetAbilityEffectivenessMessage message);
	public void handle(GetActiveBugemonsMessage message);
	public void handle(GetTowerInfoMessage message);
	public void handle(GetNextWindowMessage message);
	public void handle(GetBattleEndInfoMessage message);
	public void handle(GetLevelUpInfoMessage message);
	public void handle(GetPlayerTeamMessage message);

	//ACTIONS
	public void handle(PickRandomActionMessage message);
	public void handle(RunMessage message);
	public void handle(SwapBugemonMessage message);
	public void handle(UseAbilityMessage message);
	public void handle(UseItemMessage message);
	public void handle(ChooseAbilityRewardMessage message);
	public void handle(ChooseItemRewardMessage message);
	public void handle(ChooseStatRewardMessage message);
	public void handle(ChooseLevelUpRewardMessage message);
	public void handle(ChooseTowerRoomMessage message);
	
	// SPECIAL INFO
	public void handle(GetAllBugemonSpeciesMessage message);
	public void handle(GetRandomAbilityMessage message);
	public void handle(GetRandomItemMessage message);

	// ACCOUNT
	public void handle(GetUserIdFromNameMessage message);

	// FRIENDS
	public void handle(SendFriendRequestMessage message);
	public void handle(GetFriendRequestsMessage message);
	public void handle(AcceptFriendRequestMessage message);
	public void handle(DeclineFriendRequestMessage message);
	public void handle(GetFriendsListMessage message);

	// CHAT
	public void handle(SendChatMessageMessage message);
	public void handle(GetChatMessagesMessage message);

}
