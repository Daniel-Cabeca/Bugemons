package ulb.Server;

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
	public void handle(CheckGameFinishedMessage message);
	public void handle(GetBattleStateMessage message);
	public void handle(GetLogsMessage message);
	public void handle(CheckUsableItemMessage message);
	public void handle(GetAbilityEffectivenessMessage message);
	public void handle(GetActiveBugemonsMessage message);
	public void handle(GetTowerInfoMessage message);

	//ACTIONS
	public void handle(PickRandomActionMessage message);
	public void handle(RunMessage message);
	public void handle(SwapBugemonMessage message);
	public void handle(UseAbilityMessage message);
	public void handle(UseItemMessage message);
	
	// SPECIAL INFO
	public void handle(GetAllBugemonSpeciesMessage message);

}
