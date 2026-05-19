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

public class SocialHandler {
	private static final Logger LOGGER = Logger.getLogger(SocialHandler.class.getName());
	private final AccountService accountService;
	private final ChatService chatService;
	private final MultiBattleService multiBattleService;
	ClientHandler clientHandler;

	public SocialHandler(ClientHandler clientHandler, AccountService accountService, ChatService chatService,
						 MultiBattleService multiBattleService) {
		this.clientHandler = clientHandler;
		this.accountService = accountService;
		this.chatService = chatService;
		this.multiBattleService = multiBattleService;
	}

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

	public void getChatMessages(String usernameA, String usernameB) throws DataAccessException {
		clientHandler.sendMessage(new ChatMessagesResponse(chatService.getMessages(usernameA, usernameB)));
	}

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

	public void getLeaderboard() throws DataAccessException {
		Map<String, Integer> leaderboard = accountService.getLeaderboard();
		clientHandler.sendMessage(new LeaderboardResponse(leaderboard));
	}

	public void sendBattleRequest(String senderUsername, String receiverUsername) throws UserFacingException,
			DataAccessException {
		int senderId, receiverId;
		try {
			senderId = accountService.getUserId(senderUsername);
			receiverId = accountService.getUserId(receiverUsername);
		} catch (Exception e) {
			throw new DataAccessException("Cannot get player Id");
		}
		if (senderId == -1 || receiverId == -1) {
			throw new UserFacingException("Unkown User");
		}
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

	public void sendChatMessage(String senderUsername, String receiverUsername, String content) throws DataAccessException {
		chatService.sendMessage(senderUsername, receiverUsername, content);
		clientHandler.sendSuccessMessage();
	}

	public void sendFriendRequest(String senderUsername, String receiverUsername) throws UserFacingException,
			DataAccessException {
		int senderId, receiverId;
		try {
			senderId = accountService.getUserId(senderUsername);
			receiverId = accountService.getUserId(receiverUsername);
		} catch (Exception e) {
			throw new DataAccessException("Cannot get player Id");
		}

		if (senderId == -1 || receiverId == -1) {
			throw new UserFacingException("Unkown User");
		}
		accountService.sendFriendRequest(senderId, receiverId);
		clientHandler.sendSuccessMessage();
	}

}
