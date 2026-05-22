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

	/**
	 * Sets up a multiplayer battle session with the given opponent and team.
	 *
	 * @param opponent the opponent player
	 * @param bugemons the bugemons in the player's team
	 */
	void setupMultiBattle(PlayerDTO opponent, List<BugemonDTO> bugemons) throws UserFacingException,
			DataAccessException;

	/**
	 * Registers a new player or logs in an existing one.
	 *
	 * @param playerRegisterDTO the registration or login credentials
	 * @param isLogin {@code true} to log in, {@code false} to register
	 */
	void registerPlayer(PlayerRegisterDTO playerRegisterDTO, boolean isLogin) throws UserFacingException,
			DataAccessException;

	/** Initializes a normal (classic) battle mode against an AI opponent. */
	void setupNormalMode() throws UserFacingException, DataAccessException;

	/**
	 * Sets up the player's active team from a list of bugemon DTOs.
	 *
	 * @param bugemons the bugemons to include in the team
	 */
	void setupTeam(List<BugemonDTO> bugemons) throws UserFacingException, DataAccessException;

	/**
	 * Initializes tower mode for the connected player.
	 *
	 * @param isNewTower {@code true} to start a new tower run, {@code false} to continue a saved one
	 */
	void setupTowerMode(boolean isNewTower) throws UserFacingException, DataAccessException;

	// PLAYER INFO

	/**
	 * Retrieves basic profile information for the given username.
	 *
	 * @param username the username to look up
	 */
	void getPlayerInfo(String username) throws UserFacingException, DataAccessException;

	/**
	 * Retrieves the inventory of the player with the given username.
	 *
	 * @param username the username of the connected player
	 */
	void getPlayerInventory(String username) throws UserFacingException, DataAccessException;

	/**
	 * Retrieves the team of the player with the given username.
	 *
	 * @param username the username of the connected player
	 */
	void getPlayerTeam(String username) throws UserFacingException, DataAccessException;

	// GAME INFO

	/** Checks whether the current battle is finished and sends the result to the client. */
	void checkGameFinished() throws UserFacingException, DataAccessException;

	/**
	 * Checks which of the given items can be used in the current battle.
	 *
	 * @param items the items to check
	 */
	void checkUsableItems(List<ItemDTO> items) throws UserFacingException, DataAccessException;

	/**
	 * Sends effectiveness messages for the given abilities against the target bugemon.
	 *
	 * @param bugemonDTO the opponent's bugemon
	 * @param abilities the abilities to evaluate
	 */
	void getAbilityEffectiveness(BugemonDTO bugemonDTO, List<AbilityDTO> abilities) throws UserFacingException,
			DataAccessException;

	/** Sends the currently active bugemons of both teams to the client. */
	void getActiveBugemons() throws UserFacingException, DataAccessException;

	/** Collects and sends battle end information (win, XP gained) to the client. */
	void getBattleEndInfo() throws UserFacingException, DataAccessException;

	/** Sends the current battle state to the client. */
	void getBattleState() throws UserFacingException, DataAccessException;

	/** Sends level-up information (bugemon and reward choices) to the client. */
	void getLevelUpInfo() throws UserFacingException, DataAccessException;

	/**
	 * Sends battle log messages to the client.
	 *
	 * @param clearLogs whether battle logs should be cleared after sending
	 */
	void getLogs(boolean clearLogs) throws UserFacingException, DataAccessException;

	/** Resolves and sends the next window to display to the client. */
	void getNextWindow() throws UserFacingException, DataAccessException;

	/** Sends current tower state (floor, room, cleared rooms) to the client. */
	void getTowerInfo() throws UserFacingException, DataAccessException;

	/** Checks whether a tower save exists for the player and sends the result to the client. */
	void getTowerSavedInfo() throws UserFacingException, DataAccessException;

	// GAME ACTIONS

	/** Abandons the current tower run. */
	void abandonTower() throws UserFacingException, DataAccessException;

	/**
	 * Applies an ability reward by swapping an ability on a bugemon.
	 *
	 * @param bugemonDTO the bugemon receiving the new ability
	 * @param oldAbilityDTO the ability to replace
	 * @param newAbilityDTO the new ability to learn
	 */
	void chooseAbilityReward(BugemonDTO bugemonDTO, AbilityDTO oldAbilityDTO, AbilityDTO newAbilityDTO) throws UserFacingException, DataAccessException;

	/**
	 * Applies an item reward by adding the item to the player's inventory.
	 *
	 * @param itemDTO the item to add
	 */
	void chooseItemReward(ItemDTO itemDTO) throws UserFacingException, DataAccessException;

	/**
	 * Applies the chosen level-up reward from the pending reward choices.
	 *
	 * @param rewardDTO the reward chosen by the player
	 */
	void chooseLevelUpReward(RewardDTO rewardDTO) throws UserFacingException, DataAccessException;

	/**
	 * Applies a stat reward to the chosen bugemon.
	 *
	 * @param bugemonDTO the bugemon receiving the stat reward
	 */
	void chooseStatReward(BugemonDTO bugemonDTO) throws UserFacingException, DataAccessException;

	/**
	 * Moves the player to the specified room in the current tower floor.
	 *
	 * @param roomId the ID of the room to move to
	 */
	void chooseTowerRoom(int roomId) throws UserFacingException, DataAccessException;

	/** Picks a random action for the player's team in the current battle. */
	void chooseRandomAction() throws UserFacingException, DataAccessException;

	/** Registers a run action for the player's team. */
	void chooseRunAction() throws UserFacingException, DataAccessException;

	/**
	 * Joins or creates a multiplayer battle session with the specified opponent.
	 *
	 * @param opponentDTO the opponent player
	 */
	void startMultiBattle(PlayerDTO opponentDTO) throws UserFacingException, DataAccessException;

	/**
	 * Declines or withdraws from a pending multiplayer battle session.
	 *
	 * @param opponentDTO the opponent of the session to quit
	 */
	void quitMultiBattle(PlayerDTO opponentDTO) throws UserFacingException, DataAccessException;

	/**
	 * Registers a swap action for the player's team.
	 *
	 * @param bugemonDTOToSwap the bugemon to swap in
	 */
	void chooseSwapBugemonAction(BugemonDTO bugemonDTOToSwap) throws UserFacingException, DataAccessException;

	/**
	 * Registers a use ability action for the player's team.
	 *
	 * @param abilityDTO the ability to use
	 */
	void chooseUseAbilityAction(AbilityDTO abilityDTO) throws UserFacingException, DataAccessException;

	/**
	 * Registers a use item action for the player's team.
	 *
	 * @param itemDTO the item to use
	 */
	void chooseUseItemAction(ItemDTO itemDTO) throws UserFacingException, DataAccessException;

	// GAME DATA

	/** Retrieves all available bugemon species and sends them to the client. */
	void getAllBugemonSpecies() throws UserFacingException, DataAccessException;

	/**
	 * Retrieves a random ability compatible with the given bugemon and sends it to the client.
	 *
	 * @param bugemonDTO the bugemon for which a random ability is requested
	 */
	void getRandomAbility(BugemonDTO bugemonDTO) throws UserFacingException, DataAccessException;

	/** Retrieves a random item and sends it to the client. */
	void getRandomItem() throws UserFacingException, DataAccessException;

	// SOCIAL

	/**
	 * Accepts a battle request from the sender.
	 *
	 * @param senderUsername the username of the player who sent the request
	 * @param receiverUsername the username of the player accepting the request
	 */
	void acceptBattleRequest(String senderUsername, String receiverUsername) throws UserFacingException,
			DataAccessException;

	/**
	 * Accepts a friend request from the sender.
	 *
	 * @param senderUsername the username of the player who sent the request
	 * @param receiverUsername the username of the player accepting the request
	 */
	void acceptFriendRequest(String senderUsername, String receiverUsername) throws UserFacingException,
			DataAccessException;

	/**
	 * Declines a battle request from the sender.
	 *
	 * @param senderUsername the username of the player who sent the request
	 * @param receiverUsername the username of the player declining the request
	 */
	void declineBattleRequest(String senderUsername, String receiverUsername) throws UserFacingException,
			DataAccessException;

	/**
	 * Declines a friend request from the sender.
	 *
	 * @param senderUsername the username of the player who sent the request
	 * @param receiverUsername the username of the player declining the request
	 */
	void declineFriendRequest(String senderUsername, String receiverUsername) throws UserFacingException,
			DataAccessException;

	/**
	 * Retrieves all pending battle requests for the given player.
	 *
	 * @param username the username of the player
	 */
	void getBattleRequests(String username) throws UserFacingException, DataAccessException;

	/**
	 * Retrieves the status of the multiplayer battle session between two players.
	 *
	 * @param userId1 the ID of the first player
	 * @param userId2 the ID of the second player
	 */
	void getMultiBattleStatus(int userId1, int userId2) throws UserFacingException, DataAccessException;

	/**
	 * Retrieves the chat message history between two players.
	 *
	 * @param usernameA the username of the first participant
	 * @param usernameB the username of the second participant
	 */
	void getChatMessages(String usernameA, String usernameB) throws UserFacingException, DataAccessException;

	/**
	 * Retrieves all pending friend requests for the given player.
	 *
	 * @param username the username of the player
	 */
	void getFriendRequests(String username) throws UserFacingException, DataAccessException;

	/**
	 * Retrieves the friends list for the given player.
	 *
	 * @param username the username of the player
	 */
	void getFriendsList(String username) throws UserFacingException, DataAccessException;

	/** Retrieves the global leaderboard and sends it to the client. */
	void getLeaderboard() throws UserFacingException, DataAccessException;

	/**
	 * Sends a battle request from one player to another.
	 *
	 * @param senderUsername the username of the player sending the request
	 * @param receiverUsername the username of the player receiving the request
	 */
	void sendBattleRequest(String senderUsername, String receiverUsername) throws UserFacingException,
			DataAccessException;

	/**
	 * Sends a chat message from one player to another.
	 *
	 * @param senderUsername the username of the message sender
	 * @param receiverUsername the username of the message recipient
	 * @param content the message content
	 */
	void sendChatMessage(String senderUsername, String receiverUsername, String content) throws UserFacingException,
			DataAccessException;

	/**
	 * Sends a friend request from one player to another.
	 *
	 * @param senderUsername the username of the player sending the request
	 * @param receiverUsername the username of the player receiving the request
	 */
	void sendFriendRequest(String senderUsername, String receiverUsername) throws UserFacingException,
			DataAccessException;

	// TEAM SAVE

	/** 
	 * Retrieves all saved teams for the connected player. 
	*/
	void getSavedTeams() throws UserFacingException, DataAccessException;

	/**
	 * Saves the given team for the connected player.
	 *
	 * @param teamDTO the team to save
	 */
	void saveTeam(TeamDTO teamDTO) throws UserFacingException, DataAccessException;
}
