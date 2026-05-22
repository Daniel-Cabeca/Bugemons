package ulb.server;

import ulb.DTO.battle.MultiBattleStatusDTO;
import ulb.DTO.battle.MultiBattleStatusDTO.Status;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.mapper.battle.MultiBattleStatusMapper;
import ulb.message.response.social.*;
import ulb.model.battle.MultiBattleParticipant;
import ulb.model.battle.MultiBattleSession;
import ulb.service.AccountService;
import ulb.service.ChatService;
import ulb.service.MultiBattleService;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles social features such as friend requests, battle requests, chat messaging and leaderboard.
 */
public class SocialHandler {
	private static final Logger LOGGER = Logger.getLogger(SocialHandler.class.getName());
	private final AccountService accountService;
	private final ChatService chatService;
	private final MultiBattleService multiBattleService;
	ClientHandler clientHandler;

	/**
	 * Creates a social handler for the given client and services.
	 *
	 * @param clientHandler the client handler owning this session
	 * @param accountService service for account and social operations
	 * @param chatService service for chat messaging
	 * @param multiBattleService service for multiplayer battle sessions
	 */
	public SocialHandler(ClientHandler clientHandler, AccountService accountService, ChatService chatService,
						 MultiBattleService multiBattleService) {
		this.clientHandler = clientHandler;
		this.accountService = accountService;
		this.chatService = chatService;
		this.multiBattleService = multiBattleService;
	}

	/**
	 * Accepts a battle request from the sender.
	 *
	 * @param senderUsername the username of the player who sent the battle request
	 * @param receiverUsername the username of the player accepting the request
	 * @throws DataAccessException if either player's ID cannot be retrieved
	 */
	public void acceptBattleRequest(String senderUsername, String receiverUsername) throws DataAccessException {
		int senderId, receiverId;
		try {
			senderId = accountService.getUserId(senderUsername);
			receiverId = accountService.getUserId(receiverUsername);
		} catch (Exception e) {
			throw new DataAccessException("Cannot get player Id");
		}

		accountService.acceptBattleRequest(senderId, receiverId);

		MultiBattleSession multiBattle = multiBattleService.getMultiBattle(senderId, receiverId);
		multiBattle.getParticipant(receiverId).accept();

		clientHandler.sendSuccessMessage();
	}

	/**
	 * Accepts a friend request from the sender.
	 *
	 * @param senderUsername the username of the player who sent the friend request
	 * @param receiverUsername the username of the player accepting the request
	 * @throws DataAccessException if either player's ID cannot be retrieved
	 */
	public void acceptFriendRequest(String senderUsername, String receiverUsername) throws DataAccessException {
		int senderId, receiverId;
		try {
			senderId = accountService.getUserId(senderUsername);
			receiverId = accountService.getUserId(receiverUsername);
		} catch (Exception e) {
			throw new DataAccessException("Cannot get player Id");
		}

		accountService.acceptFriendRequest(senderId, receiverId);
		clientHandler.sendSuccessMessage();
	}

	/**
	 * Declines a battle request from the sender.
	 *
	 * @param senderUsername the username of the player who sent the battle request
	 * @param receiverUsername the username of the player declining the request
	 * @throws DataAccessException if either player's ID cannot be retrieved
	 */
	public void declineBattleRequest(String senderUsername, String receiverUsername) throws DataAccessException {
		int senderId, receiverId;
		try {
			senderId = accountService.getUserId(senderUsername);
			receiverId = accountService.getUserId(receiverUsername);
		} catch (Exception e) {
			throw new DataAccessException("Cannot get player Id");
		}

		MultiBattleSession session = multiBattleService.getMultiBattle(senderId, receiverId);
		MultiBattleParticipant multiSessionParticipant = session.getParticipant(receiverId);
		multiSessionParticipant.decline();

		accountService.declineBattleRequest(senderId, receiverId);
		clientHandler.sendSuccessMessage();
	}

	/**
	 * Declines a friend request.
	 *
	 * @param senderUsername the username of the player who sent the friend request
	 * @param receiverUsername the username of the player declining the request
	 * @throws DataAccessException if either player's ID cannot be retrieved
	 */
	public void declineFriendRequest(String senderUsername, String receiverUsername) throws DataAccessException {
		int senderId, receiverId;
		try {
			senderId = accountService.getUserId(senderUsername);
			receiverId = accountService.getUserId(receiverUsername);
		} catch (Exception e) {
			throw new DataAccessException("Cannot get player Id");
		}
		accountService.declineFriendRequest(senderId, receiverId);
		clientHandler.sendSuccessMessage();
	}

	/**
	 * Retrieves all pending battle requests for the given player and sends them to the client.
	 *
	 * @param username the username of the player whose battle requests to retrieve
	 * @throws DataAccessException if the player's ID cannot be retrieved
	 */
	public void getBattleRequests(String username) throws DataAccessException {
		int userId;
		try {
			userId = accountService.getUserId(username);
		} catch (Exception e) {
			throw new DataAccessException("Cannot get player Id");
		}
		List<String> requests = accountService.getPendingBattleRequests(userId);
		clientHandler.sendMessage(new BattleRequestsResponse(requests));
	}

	/**
	 * Retrieves the current status of the multiplayer battle session between two players
	 * and sends it to the client.
	 *
	 * @param userId1 the ID of the first player
	 * @param userId2 the ID of the second player
	 */
	public void getMultiBattleStatus(int userId1, int userId2) {
		MultiBattleStatusDTO status = new MultiBattleStatusDTO(Status.NOT_CREATED);

		try {
			MultiBattleSession multiBattle = multiBattleService.getMultiBattle(userId1, userId2);
			status = MultiBattleStatusMapper.toDTO(multiBattle);
		} catch (NoSuchElementException e) {
			LOGGER.log(Level.FINE,
					"No multiplayer battle session found between users " + userId1 + " and " + userId2 + ". Returning "
							+ "NOT_CREATED status.");
		}

		MultiBattleStatusResponse response = new MultiBattleStatusResponse(status);
		clientHandler.sendMessage(response);
	}

	/**
	 * Retrieves the chat message history between two players and sends it to the client.
	 *
	 * @param usernameA the username of the first participant
	 * @param usernameB the username of the second participant
	 * @throws DataAccessException if the messages cannot be retrieved
	 */
	public void getChatMessages(String usernameA, String usernameB) throws DataAccessException {
		clientHandler.sendMessage(new ChatMessagesResponse(chatService.getMessages(usernameA, usernameB)));
	}

	/**
	 * Retrieves all pending friend requests for the given player and sends them to the client.
	 *
	 * @param username the username of the player whose friend requests to retrieve
	 * @throws DataAccessException if the player's ID cannot be retrieved
	 */
	public void getFriendRequests(String username) throws DataAccessException {
		int userId;
		try {
			userId = accountService.getUserId(username);
		} catch (Exception e) {
			throw new DataAccessException("Cannot get player Id");
		}
		List<String> requests = accountService.getPendingFriendRequests(userId);
		clientHandler.sendMessage(new FriendRequestsResponse(requests));
	}

	/**
	 * Retrieves the friends list for the given player and sends it to the client.
	 *
	 * @param username the username of the player whose friends list to retrieve
	 * @throws DataAccessException if the player's ID cannot be retrieved
	 */
	public void getFriendsList(String username) throws DataAccessException {
		int userId;
		try {
			userId = accountService.getUserId(username);
		} catch (Exception e) {
			throw new DataAccessException("Cannot get player Id");
		}
		List<String> friends = accountService.getFriendsList(userId);
		clientHandler.sendMessage(new FriendsListResponse(friends));
	}

	/**
	 * Retrieves the global leaderboard and sends it to the client.
	 *
	 * @throws DataAccessException if the leaderboard cannot be retrieved
	 */
	public void getLeaderboard() throws DataAccessException {
		Map<String, Integer> leaderboard = accountService.getLeaderboard();
		clientHandler.sendMessage(new LeaderboardResponse(leaderboard));
	}

	/**
	 * Sends a battle request from one player to another, creating a new multiplayer session
	 * and marking the sender as having accepted.
	 *
	 * @param senderUsername the username of the player sending the battle request
	 * @param receiverUsername the username of the player receiving the battle request
	 * @throws UserFacingException if either player is unknown or a battle request is already pending
	 * @throws DataAccessException if either player's ID cannot be retrieved or the session cannot be created
	 */
	public void sendBattleRequest(String senderUsername, String receiverUsername) throws UserFacingException,
			DataAccessException {
		List<Integer> ids = verifyUsers(senderUsername, receiverUsername);
		int senderId = ids.get(0);
		int receiverId = ids.get(1);
		if (accountService.hasPendingBattleRequestBetween(senderId, receiverId)) {
			throw new UserFacingException("A battle request is already pending with this friend");
		}
		MultiBattleSession multiBattle;
		try {
			multiBattle = multiBattleService.createMultiBattle(senderId, receiverId);
		} catch (Exception e) {
			throw new DataAccessException("Cannot create multiBattleSession");
		}

		multiBattle.getParticipant(senderId).accept();

		accountService.sendBattleRequest(senderId, receiverId);
		clientHandler.sendSuccessMessage();
	}

	/**
	 * Sends a chat message from one player to another.
	 *
	 * @param senderUsername the username of the message sender
	 * @param receiverUsername the username of the message recipient
	 * @param content the message content
	 * @throws DataAccessException if the message cannot be saved
	 */
	public void sendChatMessage(String senderUsername, String receiverUsername, String content) throws DataAccessException {
		chatService.sendMessage(senderUsername, receiverUsername, content);
		clientHandler.sendSuccessMessage();
	}

	/**
	 * Sends a friend request from one player to another.
	 *
	 * @param senderUsername the username of the player sending the friend request
	 * @param receiverUsername the username of the player receiving the friend request
	 * @throws UserFacingException if either player is unknown
	 * @throws DataAccessException if either player's ID cannot be retrieved
	 */
	public void sendFriendRequest(String senderUsername, String receiverUsername) throws UserFacingException,
			DataAccessException {
		List<Integer> ids = verifyUsers(senderUsername, receiverUsername);
		int senderId = ids.get(0);
		int receiverId = ids.get(1);
		accountService.sendFriendRequest(senderId, receiverId);
		clientHandler.sendSuccessMessage();
	}

	/**
	 * Resolves and validates the user IDs for a sender and receiver pair.
	 *
	 * @param senderUsername the sender's username
	 * @param receiverUsername the receiver's username
	 * @return a list containing {@code [senderId, receiverId]}
	 * @throws DataAccessException if either player's ID cannot be retrieved
	 * @throws UserFacingException if either player is unknown
	 */
	private List<Integer> verifyUsers(String senderUsername, String receiverUsername) throws DataAccessException, UserFacingException {
		int senderId, receiverId;
		try {
			senderId = accountService.getUserId(senderUsername);
			receiverId = accountService.getUserId(receiverUsername);
		} catch (Exception e) {
			throw new DataAccessException("Cannot get player Id");
		}

		if (senderId == -1 || receiverId == -1) {
			throw new UserFacingException("Unknown User");
		}
		return List.of(senderId, receiverId);
	}
}
