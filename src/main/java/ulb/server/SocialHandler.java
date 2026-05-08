package ulb.server;

import ulb.exceptions.DataAccessException;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import ulb.DTO.battle.MultiBattleStatusDTO;
import ulb.mapper.battle.MultiBattleStatusMapper;
import ulb.message.clientToServer.*;
import ulb.message.serverToClient.*;
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

	public void handle(AcceptBattleRequestMessage message) throws DataAccessException{
		int senderId = accountService.getUserId(message.getSenderUsername());
		int receiverId = accountService.getUserId(message.getReceiverUsername());

		accountService.acceptBattleRequest(senderId, receiverId);

		MultiBattleSession multiBattle = multiBattleService.getMultiBattle(senderId, receiverId);
		multiBattle.getParticipant(receiverId).accept();

		clientHandler.sendSuccessMessage();
	}

	public void handle(AcceptFriendRequestMessage message) throws DataAccessException{
		int senderId = accountService.getUserId(message.getSenderUsername());
		int receiverId = accountService.getUserId(message.getReceiverUsername());

		accountService.acceptFriendRequest(senderId, receiverId);
		clientHandler.sendSuccessMessage();
	}

	public void handle(DeclineBattleRequestMessage message) throws DataAccessException{
		int senderId = accountService.getUserId(message.getSenderUsername());
		int receiverId = accountService.getUserId(message.getReceiverUsername());

		MultiBattleSession session = multiBattleService.getMultiBattle(senderId, receiverId);
		MultiBattleParticipant multiSessionParticipant = session.getParticipant(receiverId);
		multiSessionParticipant.decline();

		accountService.declineBattleRequest(senderId, receiverId);
		clientHandler.sendSuccessMessage();
	}

	public void handle(DeclineFriendRequestMessage message) throws DataAccessException{
		int senderId = accountService.getUserId(message.getSenderUsername());
		int receiverId = accountService.getUserId(message.getReceiverUsername());
		accountService.declineFriendRequest(senderId, receiverId);
		clientHandler.sendSuccessMessage();
	}

	public void handle(GetBattleRequestsMessage message) throws DataAccessException{
		int userId = accountService.getUserId(message.getUsername());
		List<String> requests = accountService.getPendingBattleRequests(userId);
		clientHandler.sendMessage(new BattleRequestsMessage(requests));
	}

	public void handle(GetMultiBattleStatusMessage message) throws DataAccessException {
		MultiBattleStatusDTO status = new MultiBattleStatusDTO();

		try {
			MultiBattleSession multiBattle = multiBattleService.getMultiBattle(message.getUserId1(), message.getUserId2());
			status = MultiBattleStatusMapper.toDTO(multiBattle);
		}
		catch (NoSuchElementException e) {}

		MultiBattleStatusMessage response = new MultiBattleStatusMessage(status);
		clientHandler.sendMessage(response);
	}

	public void handle(GetChatMessagesMessage message) throws DataAccessException{
		clientHandler.sendMessage(new ChatMessagesMessage(chatService.getMessages(message.getUsernameA(), message.getUsernameB())));
	}

	public void handle(GetFriendRequestsMessage message) throws DataAccessException{
		int userId = accountService.getUserId(message.getUsername());
		List<String> requests = accountService.getPendingFriendRequests(userId);
		clientHandler.sendMessage(new FriendRequestsMessage(requests));
	}

	public void handle(GetFriendsListMessage message) throws DataAccessException{
		int userId = accountService.getUserId(message.getUsername());
		List<String> friends = accountService.getFriendsList(userId);
		clientHandler.sendMessage(new FriendsListMessage(friends));
	}

	public void handle(GetLeaderboardMessage message) throws DataAccessException{
		Map<String, Integer> leaderboard = accountService.getLeaderboard();
		clientHandler.sendMessage(new LeaderboardMessage(leaderboard));
	}

	public void handle(SendBattleRequestMessage message) throws DataAccessException{
		int senderId = accountService.getUserId(message.getSenderUsername());
		int receiverId = accountService.getUserId(message.getReceiverUsername());
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

	public void handle(SendChatMessageMessage message) throws DataAccessException{
		chatService.sendMessage(message.getSenderUsername(), message.getReceiverUsername(), message.getContent());
		clientHandler.sendSuccessMessage();
	}

	public void handle(SendFriendRequestMessage message) throws DataAccessException{
		int senderId = accountService.getUserId(message.getSenderUsername());
		int receiverId = accountService.getUserId(message.getReceiverUsername());
		if (senderId == -1 || receiverId == -1){
			clientHandler.sendErrorMessage("Utilisateur introuvable");
			return;
		}
		accountService.sendFriendRequest(senderId, receiverId);
		clientHandler.sendSuccessMessage();
	}
    
}
