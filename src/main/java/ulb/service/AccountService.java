package ulb.service;

import ulb.repository.LoadException;
import ulb.repository.AccountRepository;

import java.util.List;

public class AccountService {
    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public boolean register(String username, String password) throws LoadException {
        return repository.register(username, password);
    }

    public boolean login(String username, String password) throws LoadException {
        String storedPassword = repository.getPasswordHash(username);
        // null means the username doesn't exist
        if (storedPassword == null) return false;
        return storedPassword.equals(password);
    }

    public int getUserId(String username) throws LoadException {
        return repository.getUserId(username);
    }

	public List<String> getFriendsList(int userId) throws LoadException {
		return repository.getFriendsList(userId);
	}

	public void addFriend(int userId, int friendId) throws LoadException {
		repository.addFriend(userId, friendId);
	}

	public void sendFriendRequest(int senderId, int receiverId) throws LoadException {
		repository.sendFriendRequest(senderId, receiverId);
	}

	public List<String> getPendingFriendRequests(int receiverId) throws LoadException {
		return repository.getPendingFriendRequests(receiverId);
	}

	public void acceptFriendRequest(int senderId, int receiverId) throws LoadException {
		repository.acceptFriendRequest(senderId, receiverId);
	}

	public void declineFriendRequest(int senderId, int receiverId) throws LoadException {
		repository.declineFriendRequest(senderId, receiverId);
	}

	public void sendBattleRequest(int senderId, int receiverId) throws LoadException {
		repository.sendBattleRequest(senderId, receiverId);
	}

	public boolean hasPendingBattleRequestBetween(int userIdA, int userIdB) throws LoadException {
		return repository.hasPendingBattleRequestBetween(userIdA, userIdB);
	}

	public List<String> getPendingBattleRequests(int receiverId) throws LoadException {
		return repository.getPendingBattleRequests(receiverId);
	}

	public void acceptBattleRequest(int senderId, int receiverId) throws LoadException {
		repository.acceptBattleRequest(senderId, receiverId);
	}

	public void declineBattleRequest(int senderId, int receiverId) throws LoadException {
		repository.declineBattleRequest(senderId, receiverId);
	}
}
