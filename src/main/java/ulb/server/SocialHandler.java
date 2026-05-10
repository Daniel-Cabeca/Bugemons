package ulb.server;

import ulb.exceptions.DataAccessException;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import ulb.DTO.battle.MultiBattleStatusDTO;
import ulb.mapper.battle.MultiBattleStatusMapper;
import ulb.message.response.social.*;
import ulb.model.battle.MultiBattleParticipant;
import ulb.model.battle.MultiBattleSession;
import ulb.service.AccountService;
import ulb.service.ChatService;
import ulb.service.MultiBattleService;

public class SocialHandler {
    ClientHandler clientHandler;
    private final AccountService accountService;
    private final ChatService chatService;
	private final MultiBattleService multiBattleService;

    public SocialHandler(ClientHandler clientHandler, AccountService accountService, ChatService chatService, MultiBattleService multiBattleService) {
        this.clientHandler = clientHandler;
        this.accountService = accountService;
        this.chatService = chatService;
		this.multiBattleService = multiBattleService;
    }

	public void acceptBattleRequest(String senderUsername, String receiverUsername) throws DataAccessException{
		int senderId = accountService.getUserId(senderUsername);
		int receiverId = accountService.getUserId(receiverUsername);

		accountService.acceptBattleRequest(senderId, receiverId);

		MultiBattleSession multiBattle = multiBattleService.getMultiBattle(senderId, receiverId);
		multiBattle.getParticipant(receiverId).accept();

		clientHandler.sendSuccessMessage();
	}

	public void acceptFriendRequest(String senderUsername, String receiverUsername) throws DataAccessException{
		int senderId = accountService.getUserId(senderUsername);
		int receiverId = accountService.getUserId(receiverUsername);

		accountService.acceptFriendRequest(senderId, receiverId);
		clientHandler.sendSuccessMessage();
	}

	public void declineBattleRequest(String senderUsername, String receiverUsername) throws DataAccessException{
		int senderId = accountService.getUserId(senderUsername);
		int receiverId = accountService.getUserId(receiverUsername);

		MultiBattleSession session = multiBattleService.getMultiBattle(senderId, receiverId);
		MultiBattleParticipant multiSessionParticipant = session.getParticipant(receiverId);
		multiSessionParticipant.decline();

		accountService.declineBattleRequest(senderId, receiverId);
		clientHandler.sendSuccessMessage();
	}

	public void declineFriendRequest(String senderUsername, String receiverUsername) throws DataAccessException{
		int senderId = accountService.getUserId(senderUsername);
		int receiverId = accountService.getUserId(receiverUsername);
		accountService.declineFriendRequest(senderId, receiverId);
		clientHandler.sendSuccessMessage();
	}

	public void getBattleRequests(String username) throws DataAccessException{
		int userId = accountService.getUserId(username);
		List<String> requests = accountService.getPendingBattleRequests(userId);
		clientHandler.sendMessage(new BattleRequestsResponse(requests));
	}

	public void getMultiBattleStatus(int userId1, int userId2) {
		MultiBattleStatusDTO status = new MultiBattleStatusDTO();

		try {
			MultiBattleSession multiBattle = multiBattleService.getMultiBattle(userId1, userId2);
			status = MultiBattleStatusMapper.toDTO(multiBattle);
		}
		catch (NoSuchElementException e) {}

		MultiBattleStatusResponse response = new MultiBattleStatusResponse(status);
		clientHandler.sendMessage(response);
	}

	public void getChatMessages(String usernameA, String usernameB) throws DataAccessException{
		clientHandler.sendMessage(new ChatMessagesResponse(chatService.getMessages(usernameA, usernameB)));
	}

	public void getFriendRequests(String username) throws DataAccessException{
		int userId = accountService.getUserId(username);
		List<String> requests = accountService.getPendingFriendRequests(userId);
		clientHandler.sendMessage(new FriendRequestsResponse(requests));
	}

	public void getFriendsList(String username) throws DataAccessException{
		int userId = accountService.getUserId(username);
		List<String> friends = accountService.getFriendsList(userId);
		clientHandler.sendMessage(new FriendsListResponse(friends));
	}

	public void getLeaderboard() throws DataAccessException{
		Map<String, Integer> leaderboard = accountService.getLeaderboard();
		clientHandler.sendMessage(new LeaderboardResponse(leaderboard));
	}

	public void sendBattleRequest(String senderUsername, String receiverUsername) throws DataAccessException{
		int senderId = accountService.getUserId(senderUsername);
		int receiverId = accountService.getUserId(receiverUsername);
		if (senderId == -1 || receiverId == -1){
			clientHandler.sendErrorMessage("Utilisateur introuvable");
			return;
		}
		if (accountService.hasPendingBattleRequestBetween(senderId, receiverId)) {
			clientHandler.sendErrorMessage("Un défi est déjà en attente avec cet ami");
			return;
		}

		MultiBattleSession multiBattle = multiBattleService.createMultiBattle(senderId, receiverId);
		multiBattle.getParticipant(senderId).accept();

		accountService.sendBattleRequest(senderId, receiverId);
		clientHandler.sendSuccessMessage();
	}

	public void sendChatMessage(String senderUsername, String receiverUsername, String content) throws DataAccessException{
		chatService.sendMessage(senderUsername, receiverUsername, content);
		clientHandler.sendSuccessMessage();
	}

	public void sendFriendRequest(String senderUsername, String receiverUsername) throws DataAccessException{
		int senderId = accountService.getUserId(senderUsername);
		int receiverId = accountService.getUserId(receiverUsername);
		if (senderId == -1 || receiverId == -1){
			clientHandler.sendErrorMessage("Utilisateur introuvable");
			return;
		}
		accountService.sendFriendRequest(senderId, receiverId);
		clientHandler.sendSuccessMessage();
	}
    
}
