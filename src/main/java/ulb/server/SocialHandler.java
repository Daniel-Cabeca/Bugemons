package ulb.server;

import java.util.List;
import java.util.Map;

import ulb.message.clientToServer.*;
import ulb.message.serverToClient.*;
import ulb.service.AccountService;
import ulb.service.ChatService;

public class SocialHandler {
    ClientHandler clientHandler;
    AccountService accountService;
    ChatService chatService;

    public SocialHandler(ClientHandler clientHandler, AccountService accountService, ChatService chatService) {
        this.clientHandler = clientHandler;
        this.accountService = accountService;
        this.chatService = chatService;
    }

	public void handle(AcceptBattleRequestMessage message){
		int senderId = accountService.getUserId(message.getSenderUsername());
		int receiverId = accountService.getUserId(message.getReceiverUsername());
		accountService.acceptBattleRequest(senderId, receiverId);
		clientHandler.sendSuccessMessage();
	}

	public void handle(AcceptFriendRequestMessage message){
		int senderId = accountService.getUserId(message.getSenderUsername());
		int receiverId = accountService.getUserId(message.getReceiverUsername());
		accountService.acceptFriendRequest(senderId, receiverId);
		clientHandler.sendSuccessMessage();
	}

	public void handle(DeclineBattleRequestMessage message){
		int senderId = accountService.getUserId(message.getSenderUsername());
		int receiverId = accountService.getUserId(message.getReceiverUsername());
		accountService.declineBattleRequest(senderId, receiverId);
		clientHandler.sendSuccessMessage();
	}

	public void handle(DeclineFriendRequestMessage message){
		int senderId = accountService.getUserId(message.getSenderUsername());
		int receiverId = accountService.getUserId(message.getReceiverUsername());
		accountService.declineFriendRequest(senderId, receiverId);
		clientHandler.sendSuccessMessage();
	}

	public void handle(GetBattleRequestsMessage message){
		int userId = accountService.getUserId(message.getUsername());
		List<String> requests = accountService.getPendingBattleRequests(userId);
		clientHandler.sendMessage(new BattleRequestsMessage(requests));
	}

	public void handle(GetChatMessagesMessage message){
		clientHandler.sendMessage(new ChatMessagesMessage(chatService.getMessages(message.getUsernameA(), message.getUsernameB())));
	}

	public void handle(GetFriendRequestsMessage message){
		int userId = accountService.getUserId(message.getUsername());
		List<String> requests = accountService.getPendingFriendRequests(userId);
		clientHandler.sendMessage(new FriendRequestsMessage(requests));
	}

	public void handle(GetFriendsListMessage message){
		int userId = accountService.getUserId(message.getUsername());
		List<String> friends = accountService.getFriendsList(userId);
		clientHandler.sendMessage(new FriendsListMessage(friends));
	}

	public void handle(GetLeaderboardMessage message){
		Map<String, Integer> leaderboard = accountService.getLeaderboard();
		clientHandler.sendMessage(new LeaderboardMessage(leaderboard));
	}

	public void handle(SendBattleRequestMessage message){
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
		accountService.sendBattleRequest(senderId, receiverId);
		clientHandler.sendSuccessMessage();
	}

	public void handle(SendChatMessageMessage message){
		chatService.sendMessage(message.getSenderUsername(), message.getReceiverUsername(), message.getContent());
		clientHandler.sendSuccessMessage();
	}

	public void handle(SendFriendRequestMessage message){
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
